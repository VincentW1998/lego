import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import java.util.Random;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class Cube extends Box{
	Color color;
	static int numeroCube = 0;
	int id;
	double x;
	double y;
	double z;
	
		
	public Cube(Color c, double w, double h, double d) {
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
	
	public Cube copy() {
		Cube tmp = new Cube(color,this.getWidth(),this.getHeight(),this.getDepth());
		tmp.x =this.getTranslateX();
		tmp.y = this.getTranslateY();
		tmp.z = this.getTranslateZ();
		tmp.id = this.id;
		numeroCube-=1;
		return tmp;
	}
	public void giveLocation() {
		translateXProperty().set(x);
		translateYProperty().set(y);
		translateZProperty().set(z);
	}
	public boolean isColliding(Cube cube) {
		return getBoundsInParent().getMaxX()-0.01 >= cube.getBoundsInParent().getMinX()
				&& getBoundsInParent().getMinX()+0.01 <= cube.getBoundsInParent().getMaxX()
				&& getBoundsInParent().getMaxY()-0.01	>= cube.getBoundsInParent().getMinY()
				&& getBoundsInParent().getMinY()	+0.01<= cube.getBoundsInParent().getMaxY();
		
	}
}