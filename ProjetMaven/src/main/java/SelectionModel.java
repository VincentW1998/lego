import java.util.Collections;
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
	Graph graphe;
	
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

	public void W(){
		sortSelectionModel('W');
		for(int i = 0; i < selection.size(); i++){
			double x = selection.get(i).getTranslateX();
			double y = selection.get(i).getTranslateY();
			double z = selection.get(i).getTranslateZ();
			int compteur  = 0;
			while(hasCube(x, y, z+ compteur + 1)){
				compteur += 1;
			}
//			System.out.println("y :"+ y);
//			System.out.println("compteur :"+ compteur);
			selection.get(i).translateZProperty().set(selection.get(i).getTranslateZ()+compteur+1);
		}
	}

	public void S(){
		sortSelectionModel('S');
		for(int i = 0; i < selection.size(); i++){
			double x = selection.get(i).getTranslateX();
			double y = selection.get(i).getTranslateY();
			double z = selection.get(i).getTranslateZ();
			int compteur  = 0;
			while(hasCube(x, y, z-compteur-1)){
				compteur += 1;
			}

//			System.out.println("y :"+ y);
//			System.out.println("compteur :"+ compteur);
			selection.get(i).translateZProperty().set(selection.get(i).getTranslateZ()-compteur-1);
		}
	}

	
	public void A(){
		sortSelectionModel('A');
		for(int i = 0; i < selection.size(); i++){
			double x = selection.get(i).getTranslateX();
			double y = selection.get(i).getTranslateY();
			double z = selection.get(i).getTranslateZ();
			int compteur  = 0;
			while(hasCube(x - compteur - 1, y, z)){
				compteur += 1;
			}
//			System.out.println("y :"+ y);
//			System.out.println("compteur :"+ compteur);
			selection.get(i).translateXProperty().set(selection.get(i).getTranslateX()-compteur-1);
		}
	}

	public void D(){
		sortSelectionModel('D');
		for(int i = 0; i < selection.size(); i++){
			double x = selection.get(i).getTranslateX();
			double y = selection.get(i).getTranslateY();
			double z = selection.get(i).getTranslateZ();
			int compteur  = 0;
			while(hasCube(x + compteur + 1, y, z)){
				compteur += 1;
			}
//			System.out.println("y :"+ y);
//			System.out.println("compteur :"+ compteur);
			selection.get(i).translateXProperty().set(selection.get(i).getTranslateX()+compteur+1);
		}
	}


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
//			System.out.println("y :"+ y);
//			System.out.println("compteur :"+ compteur);
			selection.get(i).translateYProperty().set(selection.get(i).getTranslateY()-compteur-1);
		}
	}


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
//			System.out.println("y :"+ y);
//			System.out.println("compteur :"+ compteur);
			if(y+compteur+1 <= 0 && y !=0){
				selection.get(i).translateYProperty().set(selection.get(i).getTranslateY()+compteur+1);
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

	public void setColors(int x){
		for(int i = 0; i < selection.size(); i++){
			selection.get(i).setRange(x);
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
	//auxiliaire
	public void attachedTo(Graph g, Cube c){
		for(int i=1;i<group.getChildren().size();i++){
			{
				Cube tmp = (Cube) group.getChildren().get(i);
				if (!tmp.equals(c) && c.checkPos(tmp)) {
					g.addAjd(c, tmp);
				}
			}
		}
	}


	public void  createGraph(){
		Graph graph = new Graph(group.getChildren().size()-1);
		Cube tmp;
		for(int i=1;i<group.getChildren().size();i++){

				tmp = (Cube) group.getChildren().get(i);
				graph.add(tmp.copy());
				attachedTo(graph, tmp);

		}
		graphe = graph;
	}

	public void Print(){
		for(int i=0;i<graphe.nodes[selection.get(0).getIdentifiant()].adj.size();i++){
			graphe.nodes[selection.get(0).getIdentifiant()-1].print();
		}
	}





}
