import java.util.LinkedList;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
	
	public boolean empty() {
		return selection.isEmpty();
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
	
//	*****
	public void A() {
		Cube tmp;
		for(int i=0;i<selection.size();i++) {		
				selection.get(i).translateXProperty().set(selection.get(i).getTranslateX()-1);
				for(int j=1;j<group.getChildren().size();j++) {
					if(!(selection.get(i).equals(group.getChildren().get(j)))){
						tmp = (Cube)group.getChildren().get(j);
						if(tmp.isColliding(selection.get(i))) {
							selection.get(i).translateXProperty().set(selection.get(i).getTranslateX()-1);
							j=0;
						}
						
					}
				}
		}
	}
	public void D() {
		Cube tmp;
		for(int i=0;i<selection.size();i++) {
				selection.get(i).translateXProperty().set(selection.get(i).getTranslateX()+1);
				for(int j=1;j<group.getChildren().size();j++) {
					if(!(selection.get(i).equals(group.getChildren().get(j)))){
						tmp = (Cube)group.getChildren().get(j);
						if(tmp.isColliding(selection.get(i))) {
							selection.get(i).translateXProperty().set(selection.get(i).getTranslateX()+1);
							j=0;
						}
						
					}
				}
		}
		
	}
//	*****
	

	public void Z() {
		Cube tmp;
		for(int i=0;i<selection.size();i++) {
		
				selection.get(i).translateYProperty().set(selection.get(i).getTranslateY()-1);
				for(int j=1;j<group.getChildren().size();j++) {
					if(!(selection.get(i).equals(group.getChildren().get(j)))){
						tmp = (Cube)group.getChildren().get(j);
						if(tmp.isColliding(selection.get(i))) {
							selection.get(i).translateYProperty().set(selection.get(i).getTranslateY()-1);
							j=0;
						}
						
					}
				}
		}
	}
	public void X() {
		Cube tmp;
		for(int i=0;i<selection.size();i++) {
			if(selection.get(i).getTranslateY()<0 ) {
				
					selection.get(i).translateYProperty().set(selection.get(i).getTranslateY()+1);
					for(int j=1;j<group.getChildren().size();j++) {
						if(!(selection.get(i).equals(group.getChildren().get(j)))){
							tmp = (Cube)group.getChildren().get(j);
							if(tmp.isColliding(selection.get(i))) {
								selection.get(i).translateYProperty().set(selection.get(i).getTranslateY()+1);
								if(selection.get(i).getTranslateY()>-1)
									selection.get(i).translateYProperty().set(selection.get(i).getTranslateY()-2);
//								j=0; cree une boucle est infinie


							}
							
						}
					}
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
	
//	*************UNDO***************
	public void Undo(Save save){
		KeyEvent e ;
		if(!save.moves.isEmpty()){
			e = save.moves.pollLast();
			if(e.getEventType().equals(KeyEvent.KEY_PRESSED)) {
				if(e.isMetaDown() && e.getCode() == KeyCode.N){
					Cube tmp;
					for(int i=1;i<group.getChildren().size();i++){
						tmp = (Cube) group.getChildren().get(i);
						if(tmp.equals(save.cubes.getLast())){
							group.getChildren().remove(i);
							save.cubes.pollLast();
							return;
						}
					}
				}
				switch (e.getCode()) {

					case W:
						remote(save);
						this.S();
						break;
					case S:
						remote(save);
						this.W();
						break;
					case A:
						remote(save);
						this.D();
						break;
					case D:

						remote(save);
						this.A();
						break;
					case Z:
						remote(save);
						this.X();
						break;
					case X:
						remote(save);
						this.Z();
						break;
					case Q:
						remote(save);
						this.E();
						break;
					case E:
						remote(save);
						this.Q();
						break;
					case BACK_SPACE:
						remote(save);
						for(int i=0;i<selection.size();i++) {
							group.getChildren().add(selection.get(i));
						}
						break;
					}


			}
		}
	}

	public void remote(Save save){
		LinkedList <Cube> s;
			selection.clear();
			s = save.remotes.pollLast().selection;
			for(int i=0;i<s.size();i++){
				this.add(s.get(i));
			}

	}
	//****************TEST
	public void changeColor() {
		for(int i=0;i<selection.size();i++) {
			selection.get(i).addRandomColor();
		}
	}
	
}
