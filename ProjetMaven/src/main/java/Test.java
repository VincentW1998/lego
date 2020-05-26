import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;



public class Test extends Application implements Initializable {
	public Model model;
	public Controller controller;


	public static void main(String[] args) {
		launch(args);
	}


	public void start(Stage primaryStage) throws Exception {

		Stage secondStage = new Stage();


		model = new Model(primaryStage, secondStage);
		controller = new Controller(model);
		model.controller = controller;

		primaryStage.setTitle("Editeur LEGO"); // frame
		primaryStage.setScene(model.scene);
		primaryStage.show();

		model.setup();

		primaryStage.setOnHidden(e -> Platform.exit());
		//ajout des EventHandlers

		controller.addKeyboardControls();
		controller.initMouseControl();
		controller.callTelecommande(controller);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

}

