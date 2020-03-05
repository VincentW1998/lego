import java.util.Collections;
import java.util.LinkedList;
import javafx.geometry.Bounds;
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
//			tmp.add(selection.get(i).copy());
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
		Cube tmp;
		for(int i=0;i<selection.size();i++) {
			selection.get(i).translateZProperty().set(selection.get(i).getTranslateZ()+1);
			for(int j=1;j<group.getChildren().size();j++) {
				if(!(selection.get(i).equals(group.getChildren().get(j)))){
					tmp = (Cube)group.getChildren().get(j);
					if(tmp.isColliding(selection.get(i))) {
						selection.get(i).translateZProperty().set(selection.get(i).getTranslateZ()+1);
						j=0;
					}

				}
			}
		}
	}
	public void S() {
		Cube tmp;
		for(int i=0;i<selection.size();i++) {
			selection.get(i).translateZProperty().set(selection.get(i).getTranslateZ()-1);
			for(int j=1;j<group.getChildren().size();j++) {
				if(!(selection.get(i).equals(group.getChildren().get(j)))){
					tmp = (Cube)group.getChildren().get(j);
					if(tmp.isColliding(selection.get(i))) {
						selection.get(i).translateZProperty().set(selection.get(i).getTranslateZ()-1);
						j=0;
					}

				}
			}
		}
	}
	
//	*****
	public void A() {
		Cube tmp;
		int x = 0;
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

	public void Z(){
		sortSelectionModel('Z');
		for(int i = 0; i < selection.size(); i++){
			double x = selection.get(i).getTranslateX();
			double y = selection.get(i).getTranslateY();
			double z = selection.get(i).getTranslateZ();
			int compteur  = 0;
			while(hasCube(x,y- compteur - 1,z)){
				compteur += 1;
			}
			System.out.println("y :"+ y);
			System.out.println("compteur :"+ compteur);
			selection.get(i).translateYProperty().set(selection.get(i).getTranslateY()-compteur-1);
		}
	}

//	public void Z() {
//		Cube tmp;
//		for(int i=0;i<selection.size();i++) {
//
//				selection.get(i).translateYProperty().set(selection.get(i).getTranslateY()-1);
//				for(int j=1;j<group.getChildren().size();j++) {
//					if(!(selection.get(i).equals(group.getChildren().get(j)))){
//						tmp = (Cube)group.getChildren().get(j);
//						if(tmp.isColliding(selection.get(i))) {
//							selection.get(i).translateYProperty().set(selection.get(i).getTranslateY()-1);
//							j=0;
//						}
//
//					}
//				}
//		}
//	}

	public void X(){
		sortSelectionModel('X');
		for(int i = 0; i < selection.size(); i++){
			double x = selection.get(i).getTranslateX();
			double y = selection.get(i).getTranslateY();
			double z = selection.get(i).getTranslateZ();
			int compteur  = 0;
			while(hasCube(x,y+compteur + 1,z)){
				compteur += 1;
			}
			System.out.println("y :"+ y);
			System.out.println("compteur :"+ compteur);
			if(y+compteur+1 <= 0 && y !=0){
				selection.get(i).translateYProperty().set(selection.get(i).getTranslateY()+compteur+1);
			}
		}
	}

//	public void X(){
//		Cube tmp;
//		for(int i = 0; i < selection.size(); i++){
//			if(selection.get(i).getTranslateY() < 0){
//				selection.get(i).translateYProperty().set(selection.get(i).getTranslateY()+1);
//				for (int j = 1; j< group.getChildren().size(); j++){
//					if(!(selection.get(i).equals(group.getChildren().get(j)))){
//						tmp = (Cube)group.getChildren().get(j);
//						if(tmp.isColliding(selection.get(i))) {
//							selection.get(i).translateYProperty().set(selection.get(i).getTranslateY()+1);
//							j=0;
//						}
//
//					}
//				}
//			}
//		}
//	}

	
	
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

	public boolean hasCube(double x, double y, double z){
		Cube tmp;
		for(int i = 1; i < group.getChildren().size(); i ++){
			tmp = (Cube) group.getChildren().get(i);
//			System.out.println(group.getChildren().get(i));
			if(tmp.equalsPosition(x,y,z))
				return true;
		}
		return false;
	}

	public void sortSelectionModel(char command){
		switch (command){
			case 'X' :
				for(int i = 1; i < selection.size(); i++)
					for(int j = i; j > 0; j--)
						if (selection.get(j-1).getTranslateY() < selection.get(j).getTranslateY()){
							Cube tmp = selection.get(j-1);
							Collections.swap(selection,j-1,j);
						}
				break;
			case 'Z' :
				for(int i = 1; i < selection.size(); i++)
					for(int j = i; j > 0; j--)
						if (selection.get(j-1).getTranslateY() > selection.get(j).getTranslateY()){
							Cube tmp = selection.get(j-1);
							Collections.swap(selection,j-1,j);
						}
				break;
			case 'D' :
				for(int i = 1; i < selection.size(); i++)
					for(int j = i; j > 0; j--)
						if (selection.get(j-1).getTranslateX() < selection.get(j).getTranslateX()){
							Cube tmp = selection.get(j-1);
							Collections.swap(selection,j-1,j);
						}
				break;
			case 'A' :
				for(int i = 1; i < selection.size(); i++)
					for(int j = i; j > 0; j--)
						if (selection.get(j-1).getTranslateX() > selection.get(j).getTranslateX()){
							Cube tmp = selection.get(j-1);
							Collections.swap(selection,j-1,j);
						}
				break;
			case 'W' :
				for(int i = 1; i < selection.size(); i++)
					for(int j = i; j > 0; j--)
						if (selection.get(j-1).getTranslateZ() < selection.get(j).getTranslateZ()){
							Cube tmp = selection.get(j-1);
							Collections.swap(selection,j-1,j);
						}
				break;
			case 'S' :
				for(int i = 1; i < selection.size(); i++)
					for(int j = i; j > 0; j--)
						if (selection.get(j-1).getTranslateZ() > selection.get(j).getTranslateZ()){
							Cube tmp = selection.get(j-1);
							Collections.swap(selection,j-1,j);
						}
				break;
		}
	}



}
