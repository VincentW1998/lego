import javafx.scene.paint.Color;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class Importer {


    public static void loadFrom(File f) throws IOException {
        JSONObject json = new JSONObject(FileUtils.readFileToString(f,"utf-8"));
        JSONArray construction = json.getJSONArray("construction");
        for(int i = 0; i < construction.length(); i++){
            JSONObject content = construction.getJSONObject(i);
            int id = content.getInt("id");
            int w = content.getInt("width");
            int h = content.getInt("height");
            int d = content.getInt("depth");
            JSONObject color = content.getJSONObject("color");
            int red = color.getInt("red");
            int green = color.getInt("green");
            int blue = color.getInt("blue");
            Color color1 = Color.rgb(red,green,blue);
            Cube cube = new Cube(color1,w,h,d);

        }
    }
}
