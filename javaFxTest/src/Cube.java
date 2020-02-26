import javafx.scene.paint.Color;
import java.util.Random;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class Cube extends Box{
	Color color;
	static int numeroCube = 0;
	int id;
	
	
		
	public Cube(Color c, int w, int h, int d) {
		super(w,h,d);
		color = c;
		setMaterial(new PhongMaterial(c));
		numeroCube++;
		id = numeroCube;
		
		
		
		
	}
	public Cube(Color c) {
		this(c,1,1,1);
	}
		
	public Cube() {
		super(1,1,1);
	}
	public void addRandomColor() {
		Random rand = new Random();
		setColor(Color.rgb(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
	}
	public void setColor(Color c) {
		setMaterial(new PhongMaterial(c));
	}
	public Color getColor() {
		return color;
	}
	
	public boolean equals(Cube c) {
		return this.id == c.id;
	}
}