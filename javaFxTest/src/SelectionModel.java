import java.util.LinkedList;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

public class SelectionModel {
	LinkedList <Cube> selection;
	Rotate r;
	Transform t;
	Group group;
	
	public SelectionModel(Group g) {
		selection = new LinkedList<Cube>();
		t = new Rotate();
		group = g;
	}
	
	public SelectionModel copy() { 
// permet de creer une copie de la selection qui ne pointe pas vers l'objet actuel
		SelectionModel tmp = new SelectionModel(group);
		for(int i=0;i<selection.size();i++) {
			tmp.add(selection.get(i));
		}
		return tmp;
	}
	
	public void add(Cube b) {
		if(!contains(b)) {
			Color c = b.getColor();
			b.setDrawMode(DrawMode.LINE);
		
			selection.add(b);
		}
	}
	
	public void clear() {
		while(!selection.isEmpty()) {
			Color c = selection.getFirst().getColor();
			selection.getFirst().setDrawMode(DrawMode.FILL);
			selection.removeFirst();
		}
	}
	
	public boolean contains(Cube b) {
		return selection.contains(b);
	}
	
	public void W() {
		for(int i=0;i<selection.size();i++) {
			selection.get(i).translateZProperty().set(selection.get(i).getTranslateZ()+1);
//			****************
		}
	}
	public void S() {
		for(int i=0;i<selection.size();i++) {
			selection.get(i).translateZProperty().set(selection.get(i).getTranslateZ()-1);
		}
	}
	public void A() {
		for(int i=0;i<selection.size();i++) {		
				selection.get(i).translateXProperty().set(selection.get(i).getTranslateX()-1);
//				****************
//				for(int j=0;j<group.getChildren().size();j++) {
//					if(group.getChildren().get(j).isPickOnBounds())
//						selection.get(i).translateXProperty().set(selection.get(i).getTranslateX()+1);
//				}
		}
	}
	public void D() {
		for(int i=0;i<selection.size();i++) {
				selection.get(i).translateXProperty().set(selection.get(i).getTranslateX()+1);
//				for(int j=0;j<group.getChildren().size();j++) {
//					if(group.getChildren().get(j).isPickOnBounds())
//						selection.get(i).translateXProperty().set(selection.get(i).getTranslateX()-1);
//				}
		}
		
	}
	
	public void Z() {
		for(int i=0;i<selection.size();i++) {
		
				selection.get(i).translateYProperty().set(selection.get(i).getTranslateY()-1);
		}
	}
	public void X() {
		for(int i=0;i<selection.size();i++) {
			if(selection.get(i).getTranslateY()<=-0.1 ) {
				
					selection.get(i).translateYProperty().set(selection.get(i).getTranslateY()+1);
			}
			else {
			
					selection.get(i).translateYProperty().set(0);
			}
		}
	}
	
	
	
//	****************ROTATION*****************
	
	public void Q() {
		r = new Rotate(+90, Rotate.Y_AXIS);
		t = t.createConcatenation(r);
		for(int i=0;i<selection.size();i++) {
			selection.get(i).getTransforms().clear();
			selection.get(i).getTransforms().addAll(t);
		}
	}
	
	public void E() {
		r = new Rotate(-90, Rotate.Y_AXIS);
		t = t.createConcatenation(r);
		for(int i=0;i<selection.size();i++) {
			selection.get(i).getTransforms().clear();
			selection.get(i).getTransforms().addAll(t);
		}
	}
	
//	*************CHECK***************
	
	
	//****************TEST
	public void changeColor() {
		for(int i=0;i<selection.size();i++) {
			selection.get(i).addRandomColor();
		}
	}
	
}
