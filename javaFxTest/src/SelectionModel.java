import java.util.LinkedList;

import javafx.scene.Group;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

public class SelectionModel {
	LinkedList <Cube> selection;
	Rotate r;
	Transform t;
	
	public SelectionModel() {
		selection = new LinkedList<Cube>();
		t = new Rotate();
	}
	
	public void add(Cube b) {
		if(!contains(b)) {
			b.setDrawMode(DrawMode.LINE);
			selection.add(b);
		}
	}
	
	public void clear() {
		while(!selection.isEmpty()) {
			selection.getFirst().setDrawMode(DrawMode.FILL);
			selection.removeFirst();
		}
	}
	
	public boolean contains(Cube b) {
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
			selection.get(i).translateYProperty().set(selection.get(i).getTranslateY()-10);
		}
	}
	
	public void X() {
		for(int i=0;i<selection.size();i++) {
			if(selection.get(i).getTranslateY()<=-10 ) {
				selection.get(i).translateYProperty().set(selection.get(i).getTranslateY()+10);
			}
			else {
				selection.get(i).translateYProperty().set(0);
			}
		}
	}
	
	
	
//	****************ROTATION*****************
	
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
