import javafx.scene.paint.Color;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class Importer {

    private static LinkedList<Cube> figure;

    public static LinkedList<Cube> loadFrom(File f) throws IOException {
        figure = new LinkedList<Cube>();
        JSONObject json = new JSONObject(FileUtils.readFileToString(f,"utf-8")); // recupere le fichier JSON
        JSONArray construction = json.getJSONArray("construction"); // creer un JSON Array de la clé "construction"
        for(int i = 0; i < construction.length(); i++){
            JSONObject content = construction.getJSONObject(i); // recupere l'objet contenu dans le JSON array qu'on a creer auparavant
            int id = content.getInt("id");
            double w = content.getDouble("width");
            double h = content.getDouble("height");
            double d = content.getDouble("depth");
            double x = content.getDouble("x");
            double y = content.getDouble("y");
            double z = content.getDouble("z");
            JSONObject color = content.getJSONObject("color"); // une couleur est un objet contenant 3 int (red,green,blue)
            int red = color.getInt("red");
            int green = color.getInt("green");
            int blue = color.getInt("blue");
            Color color1 = Color.rgb(red,green,blue);
            Cube cube = new Cube(color1,w,h,d,id,x,y,z); // creation du cube 3D
            figure.add(cube);

        }
        return figure;
    }
}
