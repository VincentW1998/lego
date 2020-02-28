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



        fw.write(json.toString());
        fw.close();


    }

}
