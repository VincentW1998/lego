package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	public static void main(String[] args) {
		launch(args);
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("test.fxml"));
		primaryStage.setTitle("FXML testing");
		Scene scene = new Scene(root,300,275);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
}
