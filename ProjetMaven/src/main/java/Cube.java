import javafx.scene.paint.Color;
import java.util.Random;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class Cube extends Box{
	Color color;
	static int numeroCube = -2;
	private int identifiant;
	double x;
	double y;
	double z;
	double angle;
	Color [] colorRange = {Color.BLACK,Color.YELLOW,Color.ORANGE,Color.RED,Color.PINK,Color.PURPLE,
			Color.BLUE,Color.CYAN,Color.GREEN,Color.BROWN};

	public void setId(int id){
		this.identifiant = id;
		
	}

	public int getIdentifiant() {
		return identifiant;
	}

	public void setRange(int x){
		setColor(colorRange[x]);
	}

	public Cube(Color c, double w, double h, double d) {
		super(w,h,d);
		color = c;
		setMaterial(new PhongMaterial(c));
		numeroCube++;
		identifiant = numeroCube;
	}

	public Cube(Color c, double w, double h, double d, int id, double x, double y, double z, double a){
		super(w,h,d);
		color = c;
		setMaterial(new PhongMaterial(c));
		this.identifiant = id;
		this.x = x;
		this.y = y;
		this.z = z;
		this.angle = a;
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


	public void angleChange(double a){
		angle += a;
		if(angle >=360)
			angle -= 360;
		else if(angle < 0)
			angle += 360;
	}
	
public boolean inBounds(double AMin, double AMax, double BMin, double BMax){
	return (AMin<BMax&&BMax<=AMax)||(AMin<=BMin&&BMin<AMax)||(AMin==BMin&&AMax==BMax);
}
	public boolean checkXpos(Cube c){ // verifie si c.minX<= this.minX<=c.MaxX ou this.minX<= c.minX<=this.MaxX
		return(inBounds(c.getBoundsInParent().getMinX(),c.getBoundsInParent().getMaxX(),getBoundsInParent().getMinX(),getBoundsInParent().getMaxX())
		);
	}

	public boolean checkZpos(Cube c){
		return(inBounds(c.getBoundsInParent().getMinZ(),c.getBoundsInParent().getMaxZ(),getBoundsInParent().getMinZ(),getBoundsInParent().getMaxZ())
		);
	}
	public boolean checkYpos(Cube c){
		return getBoundsInParent().getMaxY()==c.getBoundsInParent().getMinY();
	}

	public boolean IsCollidingInY(Cube c){
		return(inBounds(c.getBoundsInParent().getMinY(),c.getBoundsInParent().getMaxY(),getBoundsInParent().getMinY(),getBoundsInParent().getMaxY()));
	}
	public boolean checkPos(Cube c){
		return (checkYpos(c)&& ((checkXpos(c)&&(checkZpos(c)||c.checkZpos(this)))|| (c.checkXpos(this)&&(checkZpos(c)||c.checkZpos(this)))));
	}
	//verifie si le cube this est en collision avec le cube c
	public boolean isColliding(Cube c){
		if (checkXpos(c) && IsCollidingInY(c) && checkZpos(c)){
			return true;
		}
		return false;
	}


	//deplace le cube vers le point d'origine de la scene
	public void moveToOrigin(){
		double w = this.getWidth()/2;
		double h = (this.getHeight()/2);
		double d = this.getDepth()/2;
		this.translateXProperty().set(w);
		this.translateYProperty().set(-h);
		this.translateZProperty().set(d);
	}






}