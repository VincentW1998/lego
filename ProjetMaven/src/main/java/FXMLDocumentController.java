import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
        System.out.println("Color RED : " + colorPicker.getValue().getRed()*255);
        System.out.println("Color GREEN : " + colorPicker.getValue().getGreen()*255);
        System.out.println("Color BLUE : " + colorPicker.getValue().getBlue()*255);
        System.out.println("Length : "+length_value.getText());
        System.out.println("Width : "+width_value.getText());
        System.out.println("Height : "+height_value.getText());
    }

    @FXML
    void newWindow(ActionEvent event) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hotkeys.fxml"));
            Parent root = (Parent)fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("HotKeys");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e){
            System.out.println("ERROR");
        }
    }

    public Cube createCube(){
        int red = (int) (colorPicker.getValue().getRed()*255);
        int green = (int) (colorPicker.getValue().getGreen()*255);
        int blue = (int) (colorPicker.getValue().getBlue()*255);
        double w = Double.parseDouble(width_value.getText());
        double h = Double.parseDouble(height_value.getText());
        double d = Double.parseDouble(length_value.getText());
        Color color = Color.rgb(red,green,blue);
        Cube c = new Cube(color,w,h,d);
        return c;
    }

}
