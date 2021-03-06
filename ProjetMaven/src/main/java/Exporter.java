import javafx.scene.Group;
import javafx.stage.FileChooser;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/* Classe permettant d'exporter une figure (une liste de piece), on s'est servi des fichiers Json pour enregistrer les datas.*/

public class Exporter {

    // Fonction qui exporte la construction dans un fichier .JSON
    public static void saveToFile(Group group, File f) throws IOException {
        FileWriter fw = new FileWriter(f);
        JSONArray construction = new JSONArray();
        JSONObject maxVals = new JSONObject();
        maxVals.put("MaxSNB", Piece.numeroCube);
        construction.put(maxVals);
        for (int i = 1; i < group.getChildren().size(); i++){
            Piece cube = (Piece) group.getChildren().get(i);
            JSONObject json = new JSONObject();
            JSONObject color = new JSONObject();
            json.put("id",cube.getIdentifiant());
            json.put("SerialNB",cube.SerialNb);
            color.put("red", cube.getColor().getRed()*255);
            color.put("green", cube.getColor().getGreen()*255);
            color.put("blue", cube.getColor().getBlue()*255);
            json.put("color", color);
            json.put("width", cube.getWidth());
            json.put("height", cube.getHeight());
            json.put("depth", cube.getDepth());
            json.put("x", cube.getTranslateX());
            json.put("y", cube.getTranslateY());
            json.put("z", cube.getTranslateZ());
            construction.put(json);
        }
        fw.write(construction.toString());
        fw.close();
    }

    // boite de dialogue lorsqu'on veut sauvegarder une construction
    private static void configureFileSave(final FileChooser fileChooser) {
        fileChooser.setTitle("Save"); // titre
        fileChooser.setInitialDirectory(new File("src/main/resources/Data/")); // path de base
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("fichier json", "*.json")); // filtre que les fichiers json
    }

    public static void export(Model model){
        configureFileSave(model.fileChooser); // appelle la boite de dialogue
        File file = model.fileChooser.showSaveDialog(model.primaryStage);
        try {
            Exporter.saveToFile(model.group, file);
        } catch (Exception e) {
        }

    }


}
