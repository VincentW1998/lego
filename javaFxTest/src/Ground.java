import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;

public class Ground extends Box{
	public Ground(int w, int h) {
		super(w,0.01,h);
		
		setMaterial(new PhongMaterial(Color.rgb(200, 200, 200, 0.2)));
		
	}
}
