import javafx.scene.Group;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Exporter {

    public static void saveToFile(Group group, File f) throws IOException {
        FileWriter fw = new FileWriter(f);
        JSONArray construction = new JSONArray();
        for (int i = 1; i < group.getChildren().size(); i++){
            Cube cube = (Cube) group.getChildren().get(i);
            JSONObject json = new JSONObject();
            JSONObject color = new JSONObject();
            json.put("id",cube.getIdentifiant());
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
            json.put("angle", cube.angle);
            construction.put(json);
        }
        fw.write(construction.toString());
        fw.close();
    }

}
