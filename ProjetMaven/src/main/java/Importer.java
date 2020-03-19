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
        JSONArray json = new JSONArray(FileUtils.readFileToString(f,"utf-8")); // recupere le fichier JSON
        for(int i = 0; i < json.length(); i++){
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
            Color color1 = Color.rgb(red,green,blue);
            Cube cube = new Cube(color1,w,h,d,id,x,y,z,a); // creation du cube 3D
            figure.add(cube);

        }
        return figure;
    }
}
