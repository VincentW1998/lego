import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class FXMLDocumentController extends Initializable {
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
}
