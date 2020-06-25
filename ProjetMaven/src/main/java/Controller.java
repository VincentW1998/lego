
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.DrawMode;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;


public class Controller {
   public Model model;
   public Cube temporaryCube;
   private double anchorX, anchorY;
   private double anchorAngleX = 0;
   private double anchorAngleY = 0;
   private final DoubleProperty angleX;
   private final DoubleProperty angleY;
   private boolean notYet = true;
   private Alert alert;

    @FXML
    public TextField email_value;
    @FXML
    public Pane selected_color;
    @FXML
    public ColorPicker colorPicker;
    @FXML
    public TextField length_value;
    @FXML
    public TextField width_value;
    @FXML
    public TextField height_value;
    @FXML
    public CheckBox mute_value;


    public Controller(Model m){
        model = m;
        anchorAngleX = 0;
        anchorAngleY = 0;
        angleX = new SimpleDoubleProperty(0);
        angleY = new SimpleDoubleProperty(0);
        alert = new Alert(Alert.AlertType.INFORMATION);
    }

    //***************FXML Methods***********

    @FXML
    private void changeColor(ActionEvent event) { // fonction permettant de choisir sa couleur
        Color choice = colorPicker.getValue();
        selected_color.setBackground(new Background(new BackgroundFill(Paint.valueOf(choice.toString()), CornerRadii.EMPTY, Insets.EMPTY)));
    }
    @FXML
    public void Undo(){ // retour en arriere
        model.save.undo(model);
    }
    //
    @FXML
    void newWindow(ActionEvent event) { // appel de la telecommande
        if(notYet){
            notYet = false;
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hotkeys.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                Stage stage = new Stage();
                stage.setTitle("HotKeys");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (Exception e) {
                System.out.println("ERROR");
            }
        }

    }

    @FXML
    public void createCube() { cubeCreation(null); } // creation de piece


    @FXML
    public void handleMuteBox(){ // box a cocher pour le son
        if(mute_value.isSelected())model.mute = true;
        else model.mute = false;
    }

    @FXML
    public void creationBrochure(ActionEvent actionEvent) { // bouton brochure
        try {
            if(model.group.getChildren().size()==1 && model.selection.PartiesSelection.isEmpty()) {
                displayAlert("Aucun lego creer", "Vous ne pouvez pas creer de brochure sans ajouter de cubes");
                return;
            }
            if(model.selection.PartiesSelection.isEmpty()) {
                Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Choix Algorithme");
                alert.setHeaderText("Choisissez l'aglorithme à utiliser");
                ButtonType Naif = new ButtonType("Naif");
                ButtonType UF = new ButtonType("UnionFind");
                ButtonType Cancel = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(Naif, UF, Cancel);
                Optional<ButtonType> result = alert.showAndWait();
                if(result.get() == Naif) {
                    graphAlgo();
                    CreateBrochure();
                }
                else if (result.get() == UF) {
                    graphAlgoUF();
                    Brochure.creationBrochureUF(model);
                    model.selection.Parties.clear();
                }
                    else if (result.get() == Cancel)
                        return;
            }
            else
                CreateBrochure();
        }
        catch(Exception e){

        }
    }

    @FXML
    public void sendEmail(ActionEvent actionEvent){ // envoie un mail avec la brochure courrante
        String to = email_value.getText();
        if(model.CurrentBrochure == null){
            displayAlert("Aucune brochure n'a ete creer", "veuillez creer votre brochure");
            return;
        }

        if(!SendEmail.mailChecker(to))
            displayAlert("Email incorect","Veuillez inserer un mail correct");
        else
            SendEmail.sendFileEmail(to,model.CurrentBrochure);
    }
    @FXML
    public void Exporter(ActionEvent actionEvent) { // exporter une construction
        Exporter.export((model));
    }
    @FXML
    public void Importer(ActionEvent actionEvent) { // importer une construction (fichier json)
        Importer.importe(model);
    }

    public void displayAlert(String header, String content){//cree une alert personalise et l'affiche
        alert.setHeaderText(header);
        if(content!=null)
            alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    public void reset(ActionEvent actionEvent){ // creer un nouvel espace de travail
        model.selection.clear();
        Cube tmp;
        for(int i = 1;i < model.group.getChildren().size();i++){
            tmp = (Cube)model.group.getChildren().get(i);
            model.selection.add(tmp);
            tmp.setDrawMode(DrawMode.FILL);
        }
        model.save.saveRemote(model.selection.copy());
        model.selection.clear();
        KeyEvent k = new KeyEvent(model.primaryStage, null, KeyEvent.KEY_PRESSED, "BACK_SPACE","", KeyCode.BACK_SPACE,false,false,false,false);
        model.save.saveMoves(k);
        model.group.getChildren().remove(1,model.group.getChildren().size());
    }

    // Commandes pour l'editeur
    public void addKeyboardControls(){
        model.primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (!event.isControlDown()) {

                switch (event.getCode()) {
                    //seclection
                    case ESCAPE: // deselectionne une selection
                        if(model.selection.correctPos())
                            model.selection.clear();
                        break;
                    //**********************Cube Movement**********************
                    case W: // + axe Z
                        model.save.saveRemote(model.selection.copy());// sauvegarde le dernier mouvement realiser
                        model.save.saveMoves((KeyEvent) event);
                        model.selection.W(model.mute);// add 15 to the Z axis when the W key is pressed

                        break;
                    case S: // - axe Z
                        model.save.saveRemote(model.selection.copy());
                        model.save.saveMoves((KeyEvent) event);
                        model.selection.S(model.mute); // substract 15 to Z axis
                        break;
                    case A: // - axe X
                        model.save.saveRemote(model.selection.copy());
                        model.save.saveMoves((KeyEvent) event);
                        model.selection.A(model.mute);// substract 10 to X axis
                        break;
                    case D: // + axe X
                        model.save.saveRemote(model.selection.copy());
                        model.save.saveMoves((KeyEvent) event);
                        model.selection.D(model.mute); // add 10 to X axis
                        break;
                    case Z: // - axe Y
                        model.save.saveRemote(model.selection.copy());
                        model.save.saveMoves((KeyEvent) event);
                        model.selection.Z(model.mute);
                        break;
                    case X: // + axe Y
                        model.save.saveRemote(model.selection.copy());
                        model.save.saveMoves((KeyEvent) event);
                        model.selection.X(model.mute);
                        break;

                    case O: // affiche l'idendifiant de la piece
                        if(model.selection.listeCubeSelectionne.size() == 1)
                            System.out.println("id : "+model.selection.listeCubeSelectionne.get(0).getIdentifiant());
                            break;
                    // deplacement de la camera
                    case UP:
                       model.camera.translateYProperty().set(model.camera.getTranslateY() - 1);
                        break;
                    case DOWN:
                        model.camera.translateYProperty().set(model.camera.getTranslateY() + 1);
                        break;
                    case LEFT:
                        model.camera.translateXProperty().set(model.camera.getTranslateX() - 1);
                        break;
                    case RIGHT:
                        model.camera.translateXProperty().set(model.camera.getTranslateX() + 1);
                        break;
                    case SPACE:
                        model. camera.translateXProperty().set(0);
                        model. camera.translateYProperty().set(-1.5);
                        angleX.set(0);
                        angleY.set(0);
                        break;
//                        *****************miscellaneous********
                    case BACK_SPACE: // supprime une piece ou une selection de piece
                        if (!model.selection.empty()) {
                            if(model.mute == false)Audio.soundDelete();
                            model.save.saveRemote(model.selection.copy());
                            model.save.saveMoves((KeyEvent) event);
                            removeSelection();
                            if(model.selection.isFlying()) {
                                model.save.undo(model);
                                displayAlert("Suppression impossible","");
                            }
                            model.selection.clear();
                        }
                        break;
                    case ENTER://separe automatiquement une structure en plusieurs parties
                        graphAlgo();
                        break;
                    case U://creer la brochure
                        CreateBrochure();
                        break;
                    case Y: // Appelle la telecommande
                        double w = model.primaryStage.getWidth() + model.primaryStage.getX();
                        double h = model.primaryStage.getY();
                        callTelecommande(this, w, h);
                        break;
                }
            }
            else{
                switch(event.getCode()) {
                    case N://creer un nouveau lego
                        cubeCreation(event);
                        break;

                    case Z:
                        model.save.undo(model);
                        break;
                    case I:// importer un fichier de sauvegarde
                        Importer.importe(model);
                        break;
                    case S:// exporter la construction dans un fichier de sauvegarde
                        Exporter.export(model);
                        break;
                    case ENTER:
                        model.reordonnerGroup(); // reordonner le groupe
                        model.graphConstruction = new Graph(model.group.getChildren().size() - 1); // initialisation du graphe
                        model.graphConstruction.createGraph(model.group); // creation du graphe
                        model.selection.separation(model.graphConstruction);
                        break;

                }
            }
        });
    }

    public void removeSelection(){
        while(!model.selection.listeCubeSelectionne.isEmpty()){
            model.group.getChildren().remove(model.selection.listeCubeSelectionne.pop());
        }
    }

    public void initTemporaryCube(){
        if (!width_value.getText().equals("") && !height_value.getText().equals("") && !length_value.getText().equals("")) {
            try {
                double w = Double.parseDouble(width_value.getText());
                double h = Double.parseDouble(height_value.getText());
                double d = Double.parseDouble(length_value.getText());
                Color color = colorPicker.getValue();
                temporaryCube = new Cube(color, w, h, d);
            }
            catch(Exception e){
               displayAlert("ERREUR"," Veuillez remplir correctement les champs \n Les caracteres autres que des chiffres ne sont pas autorisés");
            }
        }
        else displayAlert("ERREUR"," Veuillez remplir tout les champs");
    }

    public void cubeCreation(KeyEvent event){
        if (model.selection.correctPos()) {
            {
                try {
                    initTemporaryCube();
                    Cube c = temporaryCube;
                    Cube.setId(c,model.group.getChildren().size()-1);
                    c.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                        //verifie si la selection n'est pas en collision et si elle n'est pas en vol et affiche les messages d'erreur si c'est le cas
                        if (model.selection.correctPos()) {
                            if (!e.isShiftDown())
                                model.selection.clear();
                           model.selection.add((Cube) e.getSource());
                        }
                    });
                    model.selection.clear();
                    model.selection.add(c);
                    model.group.getChildren().add(c);
                    c.moveToOrigin();
                    model.save.saveRemote(model.selection.copy());
                }
                catch(Exception e){
//                    displayAlert("Aucun Cube n'a été créé", "Veuillez appuyer sur le bouton \"creation de la piece\" avant d'appuyer sur CTRL+N");
                }
                if(event !=null) {
                    model.save.saveMoves(event);
                }
                else{
                    KeyEvent k = new KeyEvent(model.primaryStage, null, KeyEvent.KEY_PRESSED, "n","", KeyCode.N,false,true,false,false);
                    model.save.saveMoves(k);
                }
            }
        }
    }

    public void graphAlgo() {//appel l'algo pour creer une brochure
        model.reordonnerGroup(); // reordonner le groupe
        model.graphConstruction = new Graph(model.group.getChildren().size() - 1); // initialisation du graphe
        model.graphConstruction.createGraph(model.group); // creation du graphe
        model.graphConstruction.printGraph(); // affichage du graphe
        model.graphConstruction.giveOrderToGraph(); // attribut un ordre de consutrction
        model.graphConstruction.printOrder(); // affiche l'ordre de construction
    }

    public void graphAlgoUF() {//appel l'algo pour creer une brochure
        model.reordonnerGroup(); // reordonner le groupe
        model.graphConstruction = new Graph(model.group.getChildren().size() - 1); // initialisation du graphe
        model.graphConstruction.createGraphUF(model.group,model); // creation du graphe
        model.graphConstruction.afficherCubes();
        model.graphConstruction.unionfind.setPartie();
        model.graphConstruction.printGraph(); // affichage du graphe
    }

    public void CreateBrochure(){
        try {
            if(!model.selection.PartiesSelection.isEmpty()) {
                model.group.getChildren().remove(1, model.group.getChildren().size() - 1);
                Brochure.creationBrochure(model);
                model.selection.PartiesSelection.clear();
            }
            else{
                for(int i=0;i < model.graphConstruction.noeuds.length;i++){
                    model.selection.add(model.graphConstruction.noeuds[i].c);
                }
                SelectionModel tmp = model.selection.copy();
                while(model.group.getChildren().size() > 1){
                    model.group.getChildren().remove(1);
                }
                Brochure.creationBrochureAlgo(tmp,model);
            }
            model.selection.clear();
        }
        catch(Exception e){
//            System.out.println("CreateBrochure Error");
        }
    }



//  **************Mouse Controls****************
    public void initMouseControl() {
        Rotate xRotate;
        Rotate yRotate;
        model.group.getTransforms().addAll(
                xRotate = new Rotate(0, Rotate.X_AXIS),
                yRotate = new Rotate(0, Rotate.Y_AXIS)
        );
        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        model.scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
        });

        model.scene.setOnMouseDragged(event -> {

            angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
            angleY.set(anchorAngleX + (anchorX - event.getSceneX()));
        });

        //prise en charge du zoom
        model.primaryStage.addEventHandler(ScrollEvent.SCROLL, event -> {
            double zoomY = event.getDeltaY();
            double zoomX = event.getDeltaX();
            if (zoomY != 0) {
                if (zoomY > 0)
                    model.camera.translateZProperty().set(model.camera.getTranslateZ() + 0.2);
                else
                    model.camera.translateZProperty().set(model.camera.getTranslateZ() - 0.2);
            }
            //for trackpad users only
            else {
                if (zoomX > 0)
                    model.camera.translateXProperty().set(model.camera.getTranslateX() - 0.2);
                else
                    model.camera.translateXProperty().set(model.camera.getTranslateX() + 0.2);
            }
        });
    }





    // Fait apparaitre la telecommande
    public void callTelecommande(Controller c, double d, double h){

        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("FXMLDocument.fxml"));
            loader.setController(c);
            AnchorPane Apane= loader.load();
            Scene secondScene = new Scene(Apane);
            model.secondStage.setResizable(false); // Ne permet pas de redimensionner la telecommande
            model.secondStage.setX(d); // a revoir !!!!!
            model.secondStage.setY(h);
            model.secondStage.setScene(secondScene);
            model.secondStage.setTitle("Lego");
            model.secondStage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }




}
