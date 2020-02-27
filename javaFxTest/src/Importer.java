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
            JSONObject cube = construction.getJSONObject(i);
        }
    }
}
