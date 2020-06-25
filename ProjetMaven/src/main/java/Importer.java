import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.FileChooser;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.util.LinkedList;

public class Importer {

    private static LinkedList<Cube> figure;

    public static LinkedList<Cube> loadFrom(File f)  {
        try{
            JSONArray json = new JSONArray(FileUtils.readFileToString(f, "utf-8")); // recupere le fichier JSON
            JSONObject maxSNB = json.getJSONObject(0);
            Cube.numeroCube = maxSNB.getInt("MaxSNB");
        }
        catch(Exception e){
            return loadFromOld(f);
        }
        try {
            JSONArray json = new JSONArray(FileUtils.readFileToString(f, "utf-8")); // recupere le fichier JSON
            figure = new LinkedList<Cube>();
            for (int i = 1; i < json.length(); i++) {
                JSONObject content = json.getJSONObject(i); // recupere l'objet contenu dans le JSON array qu'on a creer auparavant
                int id = content.getInt("id");
                int sNb = content.getInt("SerialNB");
                double w = content.getDouble("width");
                double h = content.getDouble("height");
                double d = content.getDouble("depth");
                double x = content.getDouble("x");
                double y = content.getDouble("y");
                double z = content.getDouble("z");
                double a = content.getDouble("angle");
                JSONObject color = content.getJSONObject("color"); // une couleur est un objet contenant 3 int (red,green,blue)
                int red = color.getInt("red");
                int green = color.getInt("green");
                int blue = color.getInt("blue");
                Color color1 = Color.rgb(red, green, blue);
                Cube cube = new Cube(color1, w, h, d, id, x, y, z, a); // creation du cube 3D
                cube.SerialNb = sNb;
                figure.add(cube);
            }

            return figure;
        }
        catch(Exception e){
            return null;
        }
    }

    public static LinkedList<Cube> loadFromOld(File f){
        try {
            JSONArray json = new JSONArray(FileUtils.readFileToString(f, "utf-8")); // recupere le fichier JSON
            figure = new LinkedList<Cube>();
            for (int i = 0; i < json.length(); i++) {
                JSONObject content = json.getJSONObject(i); // recupere l'objet contenu dans le JSON array qu'on a creer auparavant
                int id = content.getInt("id");
                double w = content.getDouble("width");
                double h = content.getDouble("height");
                double d = content.getDouble("depth");
                double x = content.getDouble("x");
                double y = content.getDouble("y");
                double z = content.getDouble("z");
                double a = content.getDouble("angle");
                JSONObject color = content.getJSONObject("color"); // une couleur est un objet contenant 3 int (red,green,blue)
                int red = color.getInt("red");
                int green = color.getInt("green");
                int blue = color.getInt("blue");
                Color color1 = Color.rgb(red, green, blue);
                Cube cube = new Cube(color1, w, h, d, id, x, y, z, a); // creation du cube 3D
                cube.SerialNb = i+1;
                figure.add(cube);
            }
            Cube.numeroCube = json.length();
            return figure;
        }
        catch(Exception e){
            return null;
        }
    }

    private static void configureFileChooser(final FileChooser fileChooser) {
        fileChooser.setTitle("Import");
        fileChooser.setInitialDirectory(new File("src/main/resources/Data/")
        );
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("fichier json", "*.json"));
    }

    public static void importe(Model model){
        configureFileChooser(model.fileChooser);

        File file = model.fileChooser.showOpenDialog(model.primaryStage);
        if (file != null) {
            model.group.getChildren().clear();
            model.group.getChildren().add(model.sol);
        }
        try {
            LinkedList<Cube> construction = Importer.loadFrom(file);

            for (int i = 0; i < construction.size(); i++) {
                Rotate r;
                Transform t = new Rotate();
                construction.get(i).addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                    if (!e.isShiftDown())
                        model.selection.clear();
                    model.selection.add((Cube) e.getSource());
                });
                model.group.getChildren().add(construction.get(i));
                Cube.moveToLoc(construction.get(i));
            }

        } catch (Exception e) {
//					e.printStackTrace();
        }
    }

}
