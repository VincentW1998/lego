
import javafx.application.Application;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.stage.Stage;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.shape.Box;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;


public class Test extends Application{

	private static final int HEIGHT = 800;
	private static final int WIDTH = 1400;

	public static void main(String[] args) {
		launch(args);
	}
	
	public void start(Stage primaryStage) throws Exception{
		Cube box = new Cube(Color.DARKRED);
		Cube box2 = new Cube(Color.DARKBLUE);
	
		//setup
		SmartGrp group = new SmartGrp();
		Scene scene = new Scene(group, WIDTH, HEIGHT, true);
		group.getChildren().add(box);	
		group.getChildren().add(box2);	
		Camera camera = new PerspectiveCamera(true);
		scene.setCamera(camera);
		
		
		camera.translateXProperty().set(0);
		camera.translateYProperty().set(0);
		camera.translateZProperty().set(-500);
//		group.translateXProperty().set(WIDTH/2); // set x axis to the center of the screen
//		group.translateYProperty().set(HEIGHT/2);// set y axis to the center
//		group.translateZProperty().set(0);
//		
//		box2.translateXProperty().set(WIDTH/2); // set x axis to the center of the screen
//		box2.translateYProperty().set(HEIGHT/2);// set y axis to the center
//		box2.translateZProperty().set(0);

		camera.setNearClip(1);
		camera.setFarClip(1000);
		
		SelectionModel selection = new SelectionModel();
		scene.setFill(Color.GREY);

		box.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			if(!event.isShiftDown())
				selection.clear();
			selection.add((Box) event.getSource());
		});
		box2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			if(!event.isShiftDown())
				selection.clear();
			selection.add((Box) event.getSource());	
		});
		
		
		primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			switch (event.getCode()) {
		//seclection
			case ESCAPE:
				selection.clear();
				break;
		//Cube	
			case W:
				selection.W();// add 15 to the Z axis when the W key is pressed
				break;
			case S: 
				selection.S(); // substract 15 to Z axis
				break;
			case A:
				selection.A();// substract 10 to X axis
				break;
			case D:
				selection.D(); // add 10 to X axis
				break;			
			case Z:
				selection.Z();
				break;		
			case X:
				selection.X();
				break;		
			case Q:
				selection.Q();
				break;
			case E:
				selection.E();				
				break;
		//Camera
			case UP:
				camera.translateYProperty().set(camera.getTranslateY()-15);
				break;
			case DOWN:
				camera.translateYProperty().set(camera.getTranslateY()+15);
				break;
			case LEFT:
				camera.translateXProperty().set(camera.getTranslateX()-10);
				break;
			case RIGHT:				
				camera.translateXProperty().set(camera.getTranslateX()+10);
				break;
			}	
		});
		//Zoom 
		primaryStage.addEventHandler(ScrollEvent.SCROLL, event -> {
			double zoomY = event.getDeltaY();
			double zoomX = event.getDeltaX();
			if(zoomY!=0) {
				if(zoomY>0) 
					camera.translateZProperty().set(camera.getTranslateZ()+10);	
				else 
					camera.translateZProperty().set(camera.getTranslateZ()-10);
			}
		//for trackpad users only
			else {
				if(zoomX>0) 
					camera.translateXProperty().set(camera.getTranslateX()-10);	
				else 
					camera.translateXProperty().set(camera.getTranslateX()+10);
			}
		});
	
		primaryStage.setTitle("FxTest"); // frame
		primaryStage.setScene(scene);
		primaryStage.show();
	}
		
}


