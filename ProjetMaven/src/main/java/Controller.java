
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
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
import javafx.scene.control.Alert;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class Controller {
   public Model model;
   public Cube temporaryCube;
   private double anchorX, anchorY;
   private double anchorAngleX = 0;
   private double anchorAngleY = 0;
   private final DoubleProperty angleX;
   private final DoubleProperty angleY;
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
    private void changeColor(ActionEvent event) {
        Color choice = colorPicker.getValue();
        selected_color.setBackground(new Background(new BackgroundFill(Paint.valueOf(choice.toString()), CornerRadii.EMPTY, Insets.EMPTY)));
    }

    //
    @FXML
    void newWindow(ActionEvent event) {
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

    @FXML
    public void createCube() { cubeCreation(null); }


    @FXML
    public void handleMuteBox(){
        if(mute_value.isSelected())model.mute = true;
        else model.mute = false;
    }

    @FXML
    public void creationBrochure(ActionEvent actionEvent) {
        graphAlgo();
        CreateBrochure();
    }

    @FXML
    public void sendEmail(ActionEvent actionEvent){
        String to = email_value.getText();
        File tmp = new File("src/main/resources/Brochures/brochure.pdf");
        if(!tmp.exists()) {
            graphAlgo();
            CreateBrochure();
        }
        if(!SendEmail.mailChecker(to))
            displayAlert("Email incorect","Veuillez inserer un mail correct");
        else
            SendEmail.sendFileEmail(to);
    }
    @FXML
    public void Exporter(ActionEvent actionEvent) {
        Exporter.export((model));
    }
    @FXML
    public void Importer(ActionEvent actionEvent) {
        Importer.importe(model);
    }

    public void displayAlert(String header, String content){//cree une alert personalise et l'affiche
        alert.setHeaderText(header);
        if(content!=null)
            alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    public void reset(ActionEvent actionEvent){
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
                    case ESCAPE:
                        if(model.selection.correctPos())
                            model.selection.clear();
                        break;
                    //**********************Cube Movement**********************
                    case W:
                        model.selection.W(model.mute);// add 15 to the Z axis when the W key is pressed
                        model.save.saveRemote(model.selection.copy());// sauvegarde le dernier mouvement realiser
                        model.save.saveMoves((KeyEvent) event);
                        break;
                    case S:
                        model.selection.S(model.mute); // substract 15 to Z axis
                        model.save.saveRemote(model.selection.copy());
                        model.save.saveMoves((KeyEvent) event);
                        break;
                    case A:
                        model.selection.A(model.mute);// substract 10 to X axis
                        model.save.saveRemote(model.selection.copy());
                        model.save.saveMoves((KeyEvent) event);
                        break;
                    case D:
                        model.selection.D(model.mute); // add 10 to X axis
                        model.save.saveRemote(model.selection.copy());
                        model.save.saveMoves((KeyEvent) event);
                        break;
                    case Z:
                        model.selection.Z(model.mute);
                        model.save.saveRemote(model.selection.copy());
                        model.save.saveMoves((KeyEvent) event);
                        break;
                    case X:
                        model.selection.X(model.mute);
                        model.save.saveRemote(model.selection.copy());
                        model.save.saveMoves((KeyEvent) event);
                        break;
                    case Q:
                        model.selection.Q();
                        model.save.saveRemote(model.selection.copy());
                        model.save.saveMoves((KeyEvent) event);
                        break;
                    case E:
                        model.selection.E();
                        model.save.saveRemote(model.selection.copy());
                        model.save.saveMoves((KeyEvent) event);
                        break;

                    //Camera
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
                    case BACK_SPACE:
                        if (!model.selection.empty()) {
                            for (int i = 0; i < model.selection.listeCubeSelectionne.size(); i++) {
                                model.selection.listeCubeSelectionne.get(i).setDrawMode(DrawMode.FILL);
                                model.group.getChildren().remove(model.selection.listeCubeSelectionne.get(i));
                                if(model.mute == false)Audio.soundDelete();
                            }
                            model.save.saveRemote(model.selection.copy());
                            model.selection.clear();
                            model.save.saveMoves((KeyEvent) event);
                        }
                        break;
                    case ENTER://separe automatiquement une structure en plusieurs parties
                        graphAlgo();
                        break;
                    case U://creer la brochure
                        CreateBrochure();
                        break;
                    case Y: // Appelle la telecommande
                        callTelecommande(this);
                        break;

                }
            }
            else{
                switch(event.getCode()) {
                    case N://creer un nouveau lego
                        cubeCreation(event);
                        break;

                    case Z:
                        model.selection.Undo(model.save);
                        break;
                    case I:// importer un fichier de sauvegarde
                        Importer.importe(model);
                        break;
                    case S:// exporter la construction dans un fichier de sauvegarde
                        Exporter.export(model);
                        break;
                    case ENTER:
                        model.selection.separation(model.graphConstruction);
                        break;
                }
            }
        });
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
                    c.setId(model.group.getChildren().size()-2);
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
                    model.save.newCube(c);
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

    public void CreateBrochure(){
        if(model.selection.PartiesSelection.size() != 0)
            Brochure.creationBrochure(model.scene, model.group, model.selection);
        else {
            for (int i = 0 ; i < model.graphConstruction.noeuds.length; i ++) {
                model.selection.add(model.graphConstruction.noeuds[i].c);
            }
            SelectionModel tmp = model.selection.copy();
            //vide le groupe en laissant le sol
            while(model.group.getChildren().size() > 1) {
                model.group.getChildren().remove(1);
            }
            Brochure.creationBrochureAlgo(tmp);
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
    public void callTelecommande(Controller c){

        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("FXMLDocument.fxml"));
            loader.setController(c);
            AnchorPane Apane= loader.load();
            Scene secondScene = new Scene(Apane);
            model.secondStage.setResizable(false); // Ne permet pas de redimensionner la telecommande
            model.secondStage.setScene(secondScene);
            model.secondStage.setTitle("Lego");
            model.secondStage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }




}
