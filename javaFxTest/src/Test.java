
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.Camera;
import javafx.scene.Group;

import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.DrawMode;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.scene.transform.Rotate;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;


public class Test extends Application{

	private static final int HEIGHT = 800;
	private static final int WIDTH = 1400;

	private double anchorX, anchorY;
	private double anchorAngleX=0;
	private double anchorAngleY=0;
	private final DoubleProperty angleX = new SimpleDoubleProperty(0);
	private final DoubleProperty angleY = new SimpleDoubleProperty(0);

	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void initMouseControl(Group group, Scene scene) {
		Rotate xRotate;
		Rotate yRotate;
		group.getTransforms().addAll(
			xRotate = new Rotate(0,Rotate.X_AXIS),
			yRotate = new Rotate(0,Rotate.Y_AXIS)
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

	
		Group group = new Group();
		Scene scene = new Scene(group, WIDTH, HEIGHT, true);

		Camera camera = new PerspectiveCamera(true);
		scene.setCamera(camera);
		Ground sol = new Ground(WIDTH,HEIGHT); 
		group.getChildren().add(sol);
		sol.translateYProperty().set(+0.51);
		
		camera.translateXProperty().set(0);
		camera.translateYProperty().set(0);
		camera.translateZProperty().set(-15);

		camera.setNearClip(1);
		camera.setFarClip(1000);
		
		SelectionModel selection = new SelectionModel(group);
		scene.setFill(Color.WHITE);
		
		Save save = new Save();

		final FileChooser fileChooser = new FileChooser();
		
		
		// *********************** KEYBOARD CONTROLS ****************************
		primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			if(!event.isMetaDown()) {
				switch (event.getCode()) {
			//seclection
				case ESCAPE:
					selection.clear();
					break;
			//Cube	
				case W:
					selection.W();// add 15 to the Z axis when the W key is pressed
					save.saveRemote(selection.copy());
					save.saveMoves((KeyEvent)event);
					break;
				case S:
					selection.S(); // substract 15 to Z axis
					save.saveRemote(selection.copy());
					save.saveMoves((KeyEvent)event);
					break;
				case A:

					selection.A();// substract 10 to X axis
					save.saveRemote(selection.copy());
					save.saveMoves((KeyEvent)event);
					break;
				case D:
					selection.D(); // add 10 to X axis
					save.saveRemote(selection.copy());
					save.saveMoves((KeyEvent)event);
					break;			
				case Z:
					selection.Z();
					save.saveRemote(selection.copy());
					save.saveMoves((KeyEvent)event);
					break;		
				case X:
					selection.X();
					save.saveRemote(selection.copy());
					save.saveMoves((KeyEvent)event);
					break;		
				case Q:

					selection.Q();
					save.saveRemote(selection.copy());
					save.saveMoves((KeyEvent)event);
					break;
				case E:

					selection.E();
					save.saveRemote(selection.copy());
					save.saveMoves((KeyEvent)event);
					break;
				case BACK_SPACE:
					if(!selection.empty()) {
						for(int i=0;i<selection.selection.size();i++) {
							selection.selection.get(i).setDrawMode(DrawMode.FILL);
							group.getChildren().remove(selection.selection.get(i));
						}
						save.saveRemote(selection.copy());
						selection.clear();
						save.saveMoves((KeyEvent)event);
					}
					break;
			//Camera
				case UP:
					camera.translateYProperty().set(camera.getTranslateY()-1);
					break;
				case DOWN:
					camera.translateYProperty().set(camera.getTranslateY()+1);
					break;
				case LEFT:
					camera.translateXProperty().set(camera.getTranslateX()-1);
					break;
				case RIGHT:				
					camera.translateXProperty().set(camera.getTranslateX()+1);
					break;



				}

			}
		});

		// Creation d'un nouveau cube
		primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			if(event.isMetaDown() && event.getCode()== KeyCode.N) {
				{
					Cube c = new Cube();
					c.addRandomColor();
					c.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {//ajout d'un bouton plus tard test pour creer nv lego
						if(!e.isShiftDown())
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
			KeyCode [] kc = {KeyCode.DIGIT0,KeyCode.DIGIT1,KeyCode.DIGIT2,KeyCode.DIGIT3,KeyCode.DIGIT4,KeyCode.DIGIT5,
					KeyCode.DIGIT6,KeyCode.DIGIT7,KeyCode.DIGIT8,KeyCode.DIGIT9};
			for(int i=0;i<kc.length;i++){
				if(e.getCode()==kc[i])
					selection.setColors(i);
			}
		});
		
		//********Undo*********
		
		primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event ->{
			if(event.isMetaDown() && event.getCode()== KeyCode.Z) {
//				SelectionModel m;
//				if(!save.Empty()) {
//					m = save.pop();
//					for(int i=0;i<m.selection.size();i++) {
//						group.getChildren().add(m.selection.get(i));
//					}
//
//				}
				selection.Undo(save);

			}
		});
		//Zoom 
		primaryStage.addEventHandler(ScrollEvent.SCROLL, event -> {
			double zoomY = event.getDeltaY();
			double zoomX = event.getDeltaX();
			if(zoomY!=0) {
				if(zoomY>0) 
					camera.translateZProperty().set(camera.getTranslateZ()+0.2);	
				else 
					camera.translateZProperty().set(camera.getTranslateZ()-0.2);
			}
		//for trackpad users only
			else {
				if(zoomX>0) 
					camera.translateXProperty().set(camera.getTranslateX()-0.2);
				else 
					camera.translateXProperty().set(camera.getTranslateX()+0.2);
			}
		});

		// importer un fichier de sauvegarde
		primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			if(event.isMetaDown() && event.getCode() == KeyCode.I){
				configureFileChooser(fileChooser);

				File file = fileChooser.showOpenDialog(primaryStage);
				String path = "Data/";
				if (file != null) {
					path += file.getName();
					group.getChildren().clear();
					group.getChildren().add(sol);
				}
				try {
					LinkedList<Cube> construction = Importer.loadFrom(new File(path));
					for(int i = 0; i < construction.size(); i++){
						construction.get(i).addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
							if(!e.isShiftDown())
								selection.clear();
							selection.add((Cube) e.getSource());
						});
						group.getChildren().add(construction.get(i));
						construction.get(i).translateXProperty().set(construction.get(i).x);
						construction.get(i).translateYProperty().set(construction.get(i).y);
						construction.get(i).translateZProperty().set(construction.get(i).z);
					}

				} catch (IOException e) {

					e.printStackTrace();
				}

			}
		});

		// exporter la construction dans un fichier de sauvegarde
		primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			if(event.isMetaDown() && event.getCode() == KeyCode.S){
				configureFileSave(fileChooser);
				File file = fileChooser.showSaveDialog(primaryStage);
				try {
					Exporter.saveToFile(group,file);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});

		primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event ->{
			if(event.getCode()== KeyCode.G) {
				for(int i = 0; i < group.getChildren().size(); i ++){
					System.out.println(group.getChildren().get(i));
				}
			}
		});


		
// *********************** MOUSE CONTROLS ****************************
		
		//******* 
		
		initMouseControl(group,scene);
		primaryStage.setTitle("Editeur LEGO"); // frame
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	// Filtre les fichiers importes sous le format .json
	private static void configureFileChooser(final FileChooser fileChooser) {
		fileChooser.setTitle("Import");
		fileChooser.setInitialDirectory(new File("Data")
		);
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter( "fichier json","*.json"));
	}


	// Filtre les fichiers de sauvegade sous le format .json
	private static void configureFileSave(final FileChooser fileChooser){
		fileChooser.setTitle("Save");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("fichier json", "*.json"));
	}

//		*******SAVEMOVE
//	public void saveMove(Save save, Group group) {
//		SelectionModel tmp = new SelectionModel(group);
//		for(int i=0; i<group.getChildren().size();i++) {
//			tmp.add((Cube)group.getChildren().get(i));
//		}
//		save.add(tmp.copy());
//		tmp.clear();
//	}
}


