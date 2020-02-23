import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class Cube extends Box{
	Color color;
		
	public Cube(Color c, int w, int h, int d) {
		super(w,h,d);
		color = c;
		setMaterial(new PhongMaterial(c));
	}
	public Cube(Color c) {
		this(c,40,40,40);
	}
		
	public Cube() {
		super(40,40,40);
	}
		
	public void SetColor(Color c) {
		setMaterial(new PhongMaterial(c));
	}
}