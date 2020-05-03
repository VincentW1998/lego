import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.DrawMode;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

import javax.imageio.ImageIO;

public class SelectionModel {
	LinkedList <Cube> selection;
	Rotate r;
	Transform t;
	Group group;
	LinkedList <LinkedList<Node>> Parties;
	LinkedList <SelectionModel> PartiesSelection;
	public SelectionModel(Group g) {
		selection = new LinkedList<Cube>();
		t = new Rotate();
		group = g;
		Parties = new LinkedList<LinkedList<Node>>();
		PartiesSelection = new LinkedList<SelectionModel>();
	}
	
	public SelectionModel copy() { 
// permet de creer une copie de la selection qui ne pointe pas vers l'objet actuel
		SelectionModel tmp = new SelectionModel(group);
		for(int i=0;i<selection.size();i++) {
			tmp.add(selection.get(i));

		}
	
		return tmp;
	}
	//ajoute un cube a la selection
	public void add(Cube b) {
		if(!contains(b)) {
			if(!isInCollision()) {
				b.setDrawMode(DrawMode.LINE); // change l'apparence d'un cube en (drawMode)
				selection.add(b);
			}
		}
	}

	public void addToGroup(int part){
		for(int i=0;i<selection.size();i++){
			group.getChildren().add(selection.get(i));
			selection.get(i).setDrawMode(DrawMode.FILL);
			try {//creer l'image
				ImageIO.write(SwingFXUtils.fromFXImage(group.getScene().snapshot(null), null), "png", new File("src/main/resources/Brochures/Parties/Partie"+part+"/" + "etape "+(i+1)+".png"));
			} catch (IOException e) {
				System.out.println("error PNG");
			}
		}
	}

	public void addToGroup(){
		for(int i=0;i<selection.size();i++){
			group.getChildren().add(selection.get(i));
			selection.get(i).setDrawMode(DrawMode.FILL);
		}
	}
	//vide la selection
	public void clear() {
		if(!isInCollision())
			while (!selection.isEmpty()) {
				// desactive le mode drawmode et redonne a un cube son apparence initial
				selection.getFirst().setDrawMode(DrawMode.FILL);
				selection.removeFirst();
			}
		else{
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText("Vous etes en collision");
			alert.setContentText("Veuillez deplacer votre selection dans une position correcte");
			alert.showAndWait();
		}
	}
	
	public boolean empty() {
		return selection.isEmpty();
	}
	//verifie si un cube b appartient a la selection
	public boolean contains(Cube b) {
		return selection.contains(b);
	}

	public void W(){// incremente de 1 la position du de la selection dans l'axe y
		sortSelectionModel('W');
		for(int i = 0; i < selection.size(); i++){
			selection.get(i).translateZProperty().set(selection.get(i).getTranslateZ()+1);
		}
	}

	public void S(){ //decremente la position de la selection de 1 dans l'axe z
		sortSelectionModel('S');
		for(int i = 0; i < selection.size(); i++){
			selection.get(i).translateZProperty().set(selection.get(i).getTranslateZ()-1);
		}
	}

	
	public void A(){ //decremente la position de la selection de 1 dans l'axe x
		sortSelectionModel('A');
		for(int i = 0; i < selection.size(); i++){
			selection.get(i).translateXProperty().set(selection.get(i).getTranslateX()-1);
		}
	}

	public void D(){ // incremente la position de la selection de 1 dans l'axe x
		sortSelectionModel('D');
		for(int i = 0; i < selection.size(); i++){
			selection.get(i).translateXProperty().set(selection.get(i).getTranslateX()+1);
		}
	}

	public void Z(){//decremente la position d'un cube dans l'axe y
		sortSelectionModel('Z');
		for(int i = 0; i < selection.size(); i++){
			selection.get(i).translateYProperty().set(selection.get(i).getTranslateY()-1);
		}
	}

//a finir
	public void X(){//incremente de 1 la position des cube de la selection dans l'axe y
		sortSelectionModel('X');
		if(selection.get(0).getBoundsInParent().getMaxY()+1<=0)
			for(int i = 0; i < selection.size(); i++){
				selection.get(i).translateYProperty().set(selection.get(i).getTranslateY()+1);
			}
	}

	public boolean isInCollision(){
		Cube tmp;
		for(int i=0;i<selection.size();i++) {
			for (int y = 1; y < group.getChildren().size(); y++) {
				tmp = (Cube) group.getChildren().get(y);
				if ((!tmp.equals(selection.get(i))) && selection.get(i).isColliding(tmp)) {
					return true;
				}
			}
		}
		return false;
	}

	
	
//	****************ROTATION*****************
	
	public void Q() {//tourne la selection dans l'axe x de -1
		r = new Rotate(+90, Rotate.Y_AXIS);
		t = t.createConcatenation(r);
		for(int i=0;i<selection.size();i++) {
			selection.get(i).getTransforms().clear();
			selection.get(i).getTransforms().addAll(t);
			selection.get(i).angleChange(90);
		}
	}

	public void E() {//tourne la selection dans l'axe x de -1
		r = new Rotate(-90, Rotate.Y_AXIS);
		t = t.createConcatenation(r);
		for(int i=0;i<selection.size();i++) {
			selection.get(i).getTransforms().clear();
			selection.get(i).getTransforms().addAll(t);
			selection.get(i).angleChange(-90);
		}
	}
	
//	*************UNDO***************
	public void Undo(Save save){ // annule le mouvement realiser
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
	// prend la couleur a la position x dans le tableau de couleur et l'affecte au cube this
	public void setColors(int x){
		for(int i = 0; i < selection.size(); i++){
			selection.get(i).setRange(x);
		}
	}
	// prend la selection du dernier mouvement realiser et l'ajoute a la selection actuelle
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

	// pareil a revoir
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

	public void getId(){
		for(int i=0;i<selection.size();i++){
			System.out.println(selection.get(i).getIdentifiant());
		}
	}

	public void printParties(){
		System.out.println("Affichage des differentes parties selectionnees :");
		for(int i = 0; i < Parties.size(); i++){
			System.out.println("Partie " + i + ":");
			for(int y = 0; y < Parties.get(i).size(); y++) {
				System.out.println("         " + "Piece "+Parties.get(i).get(y).c.getIdentifiant());
			}
		}
		System.out.println();
	}

//separe la selection de la structure (creation d'une partie) et la supprime
	public void separation(Graph grapheSelection){
		LinkedList <Node> tmp = new LinkedList<Node>();
		if(selection.size()!=0)
			PartiesSelection.add(this.copy());
		if(grapheSelection!=null && selection.size()!=0){
			for(int i=0;i<selection.size();i++){
				tmp.add(grapheSelection.noeuds[selection.get(i).getIdentifiant()]);
				group.getChildren().remove(selection.get(i));
			}
			Parties.add(tmp);
		}
	}
}
