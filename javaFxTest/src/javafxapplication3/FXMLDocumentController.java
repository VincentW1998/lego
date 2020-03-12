package javafxapplication3;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class FXMLDocumentController implements Initializable {
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
    private Button hotkeys;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void changeColor(ActionEvent event) {
        Color choice = colorPicker.getValue();
        selected_color.setBackground(new Background(new BackgroundFill(Paint.valueOf(choice.toString()), CornerRadii.EMPTY, Insets.EMPTY)));
    }

    @FXML
    private void showDIM(ActionEvent event) {
        System.out.println("Length : "+length_value.getText());
        System.out.println("Width : "+width_value.getText());
        System.out.println("Height : "+height_value.getText());
    }

    @FXML
    void newWindow(ActionEvent event, Stage stage) throws IOException {
        AnchorPane root =(AnchorPane) FXMLLoader.load(getClass().getResource("hotkeys.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
}
