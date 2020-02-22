import java.util.LinkedList;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

public class SelectionModel {
	LinkedList <Box> selection;
	Rotate r;
	Transform t;
	
	public SelectionModel() {
		selection = new LinkedList<Box>();
		t = new Rotate();
	}
	
	public void add(Box b) {
		selection.add(b);
	}
	
	public void clear() {
		selection.clear();
	}
	
	public boolean contains(Box b) {
		return selection.contains(b);
	}
	
	public void W() {
		for(int i=0;i<selection.size();i++) {
			selection.get(i).translateZProperty().set(selection.get(i).getTranslateZ()+15);
		}
	}
	public void S() {
		for(int i=0;i<selection.size();i++) {
			selection.get(i).translateZProperty().set(selection.get(i).getTranslateZ()-15);
		}
	}
	public void A() {
		for(int i=0;i<selection.size();i++) {
			selection.get(i).translateXProperty().set(selection.get(i).getTranslateX()-10);
		}
	}
	public void D() {
		for(int i=0;i<selection.size();i++) {
			selection.get(i).translateXProperty().set(selection.get(i).getTranslateX()+10);
		}
	}
	
	public void Z() {
		for(int i=0;i<selection.size();i++) {
			selection.get(i).translateYProperty().set(selection.get(i).getTranslateY()+10);
		}//devrait monter et non descendre
	}
	
	public void X() {
		for(int i=0;i<selection.size();i++) {
			selection.get(i).translateYProperty().set(selection.get(i).getTranslateY()-10);
		}//devrait descendre et non monter
	}
	
	public void Q() {
		r = new Rotate(-10, Rotate.Y_AXIS);
		t = t.createConcatenation(r);
		for(int i=0;i<selection.size();i++) {
			selection.get(i).getTransforms().clear();
			selection.get(i).getTransforms().addAll(t);
		}
	}
	
	public void E() {
		r = new Rotate(+10, Rotate.Y_AXIS);
		t = t.createConcatenation(r);
		for(int i=0;i<selection.size();i++) {
			selection.get(i).getTransforms().clear();
			selection.get(i).getTransforms().addAll(t);
		}
	}
	
}
