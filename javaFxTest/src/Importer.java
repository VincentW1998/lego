import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class Importer {

    public static void loadFrom(File f) throws IOException {
        JSONObject json = new JSONObject(FileUtils.readFileToString(f,"utf-8"));

    }
}
