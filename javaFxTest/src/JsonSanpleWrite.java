import com.github.cliftonlabs.json_simple.Jsonable;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class JsonSanpleWrite implements Jsonable {

    public JsonSanpleWrite(){
        
    }



    public String toJson() {
        final StringWriter writable = new StringWriter();
        try{
            this.toJson(writable);
        }catch(final IOException caught){
            /* See java.io.StringWriter. */
        }
        return writable.toString();
    }

    public void toJson(Writer writer) throws IOException {

    }

}
