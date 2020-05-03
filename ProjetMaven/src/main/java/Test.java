import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.*;
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
import javafx.scene.transform.Transform;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.Scanner;


public class Test extends Application implements Initializable {

	static Cube tmp;

	public TextField email_value;

	@FXML
	private Pane selected_color;
	@FXML
	private ColorPicker colorPicker;
	@FXML
	private TextField length_value;
	@FXML
	private TextField width_value;
	@FXML
	private TextField height_value;


	@FXML
	private void changeColor(ActionEvent event) {
		Color choice = colorPicker.getValue();
		selected_color.setBackground(new Background(new BackgroundFill(Paint.valueOf(choice.toString()), CornerRadii.EMPTY, Insets.EMPTY)));
	}

//	@FXML
//	private void showDIM(ActionEvent event) {
//		System.out.println("Color RED : " + colorPicker.getValue().getRed() * 255);
//		System.out.println("Color GREEN : " + colorPicker.getValue().getGreen() * 255);
//		System.out.println("Color BLUE : " + colorPicker.getValue().getBlue() * 255);
//		System.out.println("Length : " + length_value.getText());
//		System.out.println("Width : " + width_value.getText());
//		System.out.println("Height : " + height_value.getText());
//	}

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
	public void createCube() {
		double w = Double.parseDouble(width_value.getText());
		double h = Double.parseDouble(height_value.getText());
		double d = Double.parseDouble(length_value.getText());
		Color color = colorPicker.getValue();
		tmp = new Cube(color, w, h, d);
	}

	private static final int HEIGHT = 800;
	private static final int WIDTH = 1400;

	private double anchorX, anchorY;
	private double anchorAngleX = 0;
	private double anchorAngleY = 0;
	private final DoubleProperty angleX = new SimpleDoubleProperty(0);
	private final DoubleProperty angleY = new SimpleDoubleProperty(0);

	public static void main(String[] args) {
		launch(args);
	}

	public void initMouseControl(Group group, Scene scene) {
		Rotate xRotate;
		Rotate yRotate;
		group.getTransforms().addAll(
				xRotate = new Rotate(0, Rotate.X_AXIS),
				yRotate = new Rotate(0, Rotate.Y_AXIS)
		);
		xRotate.angleProperty().bind(angleX);
		yRotate.angleProperty().bind(angleY);

		scene.setOnMousePressed(event -> {
			anchorX = event.getSceneX();
			anchorY = event.getSceneY();
			anchorAngleX = angleX.get();
			anchorAngleY = angleY.get();
		});

		scene.setOnMouseDragged(event -> {

			angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
			angleY.set(anchorAngleX + (anchorX - event.getSceneX()));
		});
		}
	
	public void start(Stage primaryStage) throws Exception{
		Stage secondStage = new Stage();
		Group group = new Group();
		Scene scene = new Scene(group, WIDTH, HEIGHT, true);

		Camera camera = new PerspectiveCamera(true);
		scene.setCamera(camera);
		Ground sol = new Ground(WIDTH, HEIGHT);
		group.getChildren().add(sol);
		sol.translateYProperty().set(+0.51);

		camera.translateXProperty().set(0);
		camera.translateYProperty().set(0);
		camera.translateZProperty().set(-15);

		camera.setNearClip(1);
		camera.setFarClip(1000);// definit la distance de vue de la camera

		SelectionModel selection = new SelectionModel(group);
		final Graph[] graphConstruction = {new Graph(0)};
		scene.setFill(Color.WHITE);
		Save save = new Save();
		
		final FileChooser fileChooser = new FileChooser();


		// *********************** KEYBOARD CONTROLS ****************************
		primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			if (!event.isControlDown()) {
				switch (event.getCode()) {
					//seclection
					case ESCAPE:
						selection.clear();
						break;
					//Cube
					case W:
						selection.W();// add 15 to the Z axis when the W key is pressed
						save.saveRemote(selection.copy());// sauvegarde le dernier mouvement realiser
						save.saveMoves((KeyEvent) event);
						break;
					case S:
						selection.S(); // substract 15 to Z axis
						save.saveRemote(selection.copy());
						save.saveMoves((KeyEvent) event);
						break;
					case A:

						selection.A();// substract 10 to X axis
						save.saveRemote(selection.copy());
						save.saveMoves((KeyEvent) event);
						break;
					case D:
						selection.D(); // add 10 to X axis
						save.saveRemote(selection.copy());
						save.saveMoves((KeyEvent) event);
						break;
					case Z:
						selection.Z();
						save.saveRemote(selection.copy());
						save.saveMoves((KeyEvent) event);
						break;
					case X:
						selection.X();
						save.saveRemote(selection.copy());
						save.saveMoves((KeyEvent) event);
						break;
					case Q:

						selection.Q();
						save.saveRemote(selection.copy());
						save.saveMoves((KeyEvent) event);
						break;
					case E:

						selection.E();
						save.saveRemote(selection.copy());
						save.saveMoves((KeyEvent) event);
						break;
					case BACK_SPACE:
						if (!selection.empty()) {
							for (int i = 0; i < selection.listeCubeSelectionne.size(); i++) {
								selection.listeCubeSelectionne.get(i).setDrawMode(DrawMode.FILL);
								group.getChildren().remove(selection.listeCubeSelectionne.get(i));
							}
							save.saveRemote(selection.copy());
							selection.clear();
							save.saveMoves((KeyEvent) event);
						}
						break;

					//Camera
					case UP:
						camera.translateYProperty().set(camera.getTranslateY() - 1);
						break;
					case DOWN:
						camera.translateYProperty().set(camera.getTranslateY() + 1);
						break;
					case LEFT:
						camera.translateXProperty().set(camera.getTranslateX() - 1);
						break;
					case RIGHT:
						camera.translateXProperty().set(camera.getTranslateX() + 1);
						break;
				}

			}
		});

		// Creation d'un nouveau cube
		primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			if (event.isControlDown() && event.getCode() == KeyCode.N) {
				{
					Cube c = tmp;
					c.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
						if (!e.isShiftDown())
							selection.clear();
						selection.add((Cube) e.getSource());
					});
					group.getChildren().add(c);
					save.newCube(c);
				}
				save.saveMoves(event);
			}
		});

		primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, e -> {//couleur Num
			KeyCode[] kc = {KeyCode.DIGIT0, KeyCode.DIGIT1, KeyCode.DIGIT2, KeyCode.DIGIT3, KeyCode.DIGIT4, KeyCode.DIGIT5,
					KeyCode.DIGIT6, KeyCode.DIGIT7, KeyCode.DIGIT8, KeyCode.DIGIT9};
			for (int i = 0; i < kc.length; i++) {
				if (e.getCode() == kc[i])
					selection.setColors(i);
			}
		});

		//********Undo*********

		primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			if (event.isControlDown() && event.getCode() == KeyCode.Z) {
				selection.Undo(save);

			}
		});

		//Zoom 
		primaryStage.addEventHandler(ScrollEvent.SCROLL, event -> {
			double zoomY = event.getDeltaY();
			double zoomX = event.getDeltaX();
			if (zoomY != 0) {
				if (zoomY > 0)
					camera.translateZProperty().set(camera.getTranslateZ() + 0.2);
				else
					camera.translateZProperty().set(camera.getTranslateZ() - 0.2);
			}
			//for trackpad users only
			else {
				if (zoomX > 0)
					camera.translateXProperty().set(camera.getTranslateX() - 0.2);
				else
					camera.translateXProperty().set(camera.getTranslateX() + 0.2);
			}
		});

		// importer un fichier de sauvegarde
		primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			if (event.isControlDown() && event.getCode() == KeyCode.I) {
				configureFileChooser(fileChooser);

				File file = fileChooser.showOpenDialog(primaryStage);
//				String path = "src/main/resources/Data/";
				if (file != null) {
//					path += file.getName();
					group.getChildren().clear();
					group.getChildren().add(sol);
				}
				try {
//					LinkedList<Cube> construction = Importer.loadFrom(new File(path));
					LinkedList<Cube> construction = Importer.loadFrom(file);

					for (int i = 0; i < construction.size(); i++) {
						Rotate r;
						Transform t = new Rotate();
						construction.get(i).addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
							if (!e.isShiftDown())
								selection.clear();
							selection.add((Cube) e.getSource());
						});
						group.getChildren().add(construction.get(i));
						construction.get(i).translateXProperty().set(construction.get(i).x);
						construction.get(i).translateYProperty().set(construction.get(i).y);
						construction.get(i).translateZProperty().set(construction.get(i).z);
						r = new Rotate(construction.get(i).angle, Rotate.Y_AXIS);
						t = t.createConcatenation(r);
						construction.get(i).getTransforms().addAll(t);
					}

				} catch (IOException e) {

					e.printStackTrace();
				}

			}
		});

		// exporter la construction dans un fichier de sauvegarde
		primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			if (event.isControlDown() && event.getCode() == KeyCode.S) {
				configureFileSave(fileChooser);
				File file = fileChooser.showSaveDialog(primaryStage);
				try {
					Exporter.saveToFile(group, file);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});

		// affiche la composition de la figure, avec les donnees de chaques cubes
		primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			if (event.getCode() == KeyCode.G) {
				for (int i = 1; i < group.getChildren().size(); i++) {
					System.out.println(group.getChildren().get(i));
				}
			}
		});

		// printPartie fonctionne
		primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			if (event.getCode()==KeyCode.ENTER){
				// valider une partie et supprime la partie selectionner dans l'editeur
				// transform graph construction into final one element array ([0]) a comprendre
				if(event.isControlDown()){ // quand control n'est pas maintenu
					selection.separation(graphConstruction[0]);
				}
				else {
					reordonner(group); // reordonner le groupe
					graphConstruction[0] = new Graph(group.getChildren().size() - 1); // initialisation du graphe
					graphConstruction[0].createGraph(group); // creation du graphe
					graphConstruction[0].printGraph(); // affichage du graphe
					graphConstruction[0].giveOrderToGraph(); // attribut un ordre de consutrction
					graphConstruction[0].printOrder(); // affiche l'ordre de construction
				}

				// validation de la construction et la creation du graphe
			}
		});

		primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event ->{
			if(event.getCode()== KeyCode.P) {
				//affiche les differentes parties apres separation dans le terminal
				if(event.isControlDown()){
					selection.printParties(); // afficher les parties qu'on a selectionnee a la main
				}
			}
		});

		primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			if (event.getCode() == KeyCode.U) {
				if(selection.PartiesSelection.size() != 0)
					Brochure.creationBrochure(scene, group, selection);
				else {
					for (int i = 0 ; i < graphConstruction[0].noeuds.length; i ++) {
						selection.add(graphConstruction[0].noeuds[i].c);
					}
					SelectionModel tmp = selection.copy();
					//vide le groupe en laissant le sol
					while(group.getChildren().size() > 1) {
						group.getChildren().remove(1);
					}
					Brochure.creationBrochureAlgo(tmp);
				}
			}
		});

//		primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
//			if (event.getCode() == KeyCode.B) {
//				System.out.println("Quel est votre mail ? ");
//				Scanner sc = new Scanner(System.in);
//				String to = sc.nextLine();
//				SendEmail.sendFileEmail(to);
//
//			}
//		});


// *********************** MOUSE CONTROLS ****************************

		//******* 

		initMouseControl(group, scene);
		primaryStage.setTitle("Editeur LEGO"); // frame
		primaryStage.setScene(scene);
		primaryStage.show();


		AnchorPane secondRoot = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
		Scene secondScene = new Scene(secondRoot);
		secondStage.setScene(secondScene);
		secondStage.setTitle("Lego");
		secondStage.show();
		primaryStage.setOnHidden(e -> Platform.exit());

	} // FIN de la fonction start ---------------------------------------------


	// Filtre les fichiers importes sous le format .json
	private static void configureFileChooser(final FileChooser fileChooser) {
		fileChooser.setTitle("Import");
		fileChooser.setInitialDirectory(new File("src/main/resources/Data/")
		);
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("fichier json", "*.json"));
	}


	// Filtre les fichiers de sauvegade sous le format .json
	private static void configureFileSave(final FileChooser fileChooser) {
		fileChooser.setTitle("Save");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("fichier json", "*.json"));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}


	// Initialise les attaches bas de toutes les pieces du groupe
	public void setAttache(Group group) {
		Cube bas;
		Cube haut;
		for (int i = 1; i < group.getChildren().size(); i++) {
			haut = (Cube) group.getChildren().get(i);
			haut.setAttacheDown(null);
			for (int j = 1; j < group.getChildren().size(); j++) {
				bas = (Cube) group.getChildren().get(j);
				if (haut.checkPos(bas)) {
					haut.setAttacheDown(bas);
				}
			}
		}
	}

	// reordonner les identifiants des pieces
	public void reordonner(Group group){
		Cube tmp;
		for(int i = 1; i < group.getChildren().size(); i++){
			tmp = (Cube) group.getChildren().get(i);
			tmp.setId(i - 1);
		}
	}

	public void Exporter(ActionEvent actionEvent) {
	}

	public void Importer(ActionEvent actionEvent) {
	}

	public void creationBrochure(ActionEvent actionEvent) {
	}

	public void sendEmail(ActionEvent actionEvent) {
		String to = email_value.getText();
		SendEmail.sendFileEmail(to);
	}
}

