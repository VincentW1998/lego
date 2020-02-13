import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.shape.Box;



public class TestFx extends Application{

	private static final int ROTATION = 65;
	private static final int HEIGHT = 800;
	private static final int WIDTH = 1400;

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage primaryStage) throws Exception{
//		Sphere sphere = new Sphere(50);
		Box box = new Box(150,150,150);
		SmartObject group = new SmartObject(); // container
//		group.getChildren().add(sphere);
		group.getChildren().add(box);

		Camera camera = new PerspectiveCamera(); // cree une camera


		Scene scene = new Scene(group, WIDTH, HEIGHT); // panel
		scene.setFill(Color.GREY);
//		sphere.translateXProperty().set(WIDTH/2); // set x axis to the center of the screen
//		sphere.translateYProperty().set(HEIGHT/2);// set y axis to the center
		box.translateXProperty().set(WIDTH/2); // set x axis to the center of the screen
		box.translateYProperty().set(HEIGHT/2);// set y axis to the center
		box.translateZProperty().set(-1200);

		scene.setCamera(camera); // add camera
//		Transform transform = new Rotate(ROTATION,new Point3D(0,1,0));
//		box.getTransforms().add(transform);
		primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			switch (event.getCode()) {
			case W:
				box.translateZProperty().set(box.getTranslateZ()+15); // add 10 to the Z axis when the W key is pressed
				break;
			case S:
				box.translateZProperty().set(box.getTranslateZ()-15); // substract 10 to Z axis
				break;
			case A:
				box.translateXProperty().set(box.getTranslateX()-10); // substract 10 to X axis
				break;
			case D:
				box.translateXProperty().set(box.getTranslateX()+10); // substract 10 to X axis
				break;
			case UP:
				group.RotateByX(10);
				break;
			case DOWN:
				group.RotateByX(-10);
				break;
			case LEFT:
				group.RotateByY(-10);
				break;
			case RIGHT:
				group.RotateByY(10);
				break;
			}

		});

		 primaryStage.setTitle("FxTest"); // frame
		 primaryStage.setScene(scene);
		 primaryStage.show();

	}

	class SmartObject extends Group{
		Rotate r;
		Transform t = new Rotate();

		void RotateByX(int angle) {
			r = new Rotate(angle, Rotate.X_AXIS);
			t = t.createConcatenation(r);
			this.getTransforms().clear();
			this.getTransforms().addAll(t);
		}
		void RotateByY(int angle) {
			r = new Rotate(angle, Rotate.Y_AXIS);
			t = t.createConcatenation(r);
			this.getTransforms().clear();
			this.getTransforms().addAll(t);
		}
	}
}
