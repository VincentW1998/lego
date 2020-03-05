import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import java.util.Random;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class Cube extends Box{
	Color color;
	static int numeroCube = 0;
	private int identifiant;
	double x;
	double y;
	double z;

	public void setId(int id){
		this.identifiant = id;
	}

	public int getIdentifiant() {
		return identifiant;
	}


	public Cube(Color c, double w, double h, double d) {
		super(w,h,d);
		color = c;
		setMaterial(new PhongMaterial(c));
		numeroCube++;
		identifiant = numeroCube;
	}

	public Cube(Color c, double w, double h, double d, int id, double x, double y, double z){
		super(w,h,d);
		color = c;
		setMaterial(new PhongMaterial(c));
		this.identifiant = id;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Cube(Color c) {
		this(c,1,1,1);
	}

	public Cube() {
		super(1,1,1);
		numeroCube++;
		identifiant = numeroCube;
	}
	
	public void addRandomColor() {
		Random rand = new Random();
		setColor(Color.rgb(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
	}
	public void setColor(Color c) {
		this.color = c;
		setMaterial(new PhongMaterial(c));
	}

	public Color getColor() {
		return color;
	
	}
	
	public boolean equals(Cube c) {
		return this.identifiant == c.identifiant;
	}
	
	public Cube copy() {
		Cube tmp = new Cube(color,this.getWidth(),this.getHeight(),this.getDepth());
		tmp.x =this.getTranslateX();
		tmp.y = this.getTranslateY();
		tmp.z = this.getTranslateZ();
		tmp.identifiant = this.identifiant;
		numeroCube-=1;
		return tmp;
	}
	public void giveLocation() {
		translateXProperty().set(x);
		translateYProperty().set(y);
		translateZProperty().set(z);
	}


//	public boolean isColliding(Cube cube) {
//		return getBoundsInParent().getMaxX()-0.01 >= cube.getBoundsInParent().getMinX()
//				&& getBoundsInParent().getMinX()+0.01 <= cube.getBoundsInParent().getMaxX()
//				&& getBoundsInParent().getMaxY()-0.01 >= cube.getBoundsInParent().getMinY()
//				&& getBoundsInParent().getMinY()+0.01<= cube.getBoundsInParent().getMaxY();
//
//	}


	public boolean equalsPosition(double x, double y, double z){
		return this.getTranslateX() == x && this.getTranslateY() == y && this.getTranslateZ() == z;
	}

	@Override
	public String toString() {
		return "Cube{" +
				"color=" + color +
				", identifiant=" + identifiant +
				", x=" + this.getTranslateX() +
				", y=" + this.getTranslateY() +
				", z=" + this.getTranslateZ() +
				'}';
	}
}