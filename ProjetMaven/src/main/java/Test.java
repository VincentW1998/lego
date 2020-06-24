import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;



public class Test extends Application implements Initializable {
	public Model model;
	public Controller controller;


	public static void main(String[] args) {
		launch(args);
	}


	public void start(Stage primaryStage) throws Exception {
		Platform.setImplicitExit(false);

		// recupere la largeur d'ecran de l'utilisateur
		Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
		int longeur = screenDim.width;

		Stage secondStage = new Stage();


		model = new Model(primaryStage, secondStage);
		controller = new Controller(model);
		model.controller = controller;

		primaryStage.setTitle("Editeur LEGO"); // frame
		primaryStage.setScene(model.scene);
		primaryStage.setX(0);
		primaryStage.setY(150);
		primaryStage.setWidth(longeur - 312); // moins 312 car la telecommande fait 312 pixels

//		primaryStage.centerOnScreen();
		primaryStage.show();
		
		model.setup();

		primaryStage.setOnHidden(e -> Platform.exit());
		//ajout des EventHandlers

		controller.addKeyboardControls();
		controller.initMouseControl();
		double secondWidth = primaryStage.getX() + primaryStage.getWidth();
		double secondHeight = primaryStage.getY();
		controller.callTelecommande(controller, secondWidth, secondHeight);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

}

