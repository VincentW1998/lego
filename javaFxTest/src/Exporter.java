import javafx.scene.Group;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class Exporter {

    public static void saveToFile(Group group) throws IOException {
        File f = new File("name"+".json");
        FileWriter fw = new FileWriter(f);
        JSONObject json = new JSONObject();
        for (int i = 1; i < group.getChildren().size(); i++){
            Cube cube = (Cube) group.getChildren().get(i);
            json.put("id",cube.getIdentifiant());
        }



        fw.write(json.toString());
        fw.close();


    }

}
