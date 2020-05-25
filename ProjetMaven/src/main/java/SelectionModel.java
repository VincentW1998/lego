import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.shape.DrawMode;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

import javax.imageio.ImageIO;

public class SelectionModel {
	LinkedList <Cube> listeCubeSelectionne;
	Rotate r;
	Transform t;
	Group group;
	LinkedList <LinkedList<Node>> Parties;
	LinkedList <SelectionModel> PartiesSelection;
	public SelectionModel(Group g) {
		listeCubeSelectionne = new LinkedList<Cube>();
		t = new Rotate();
		group = g;
		Parties = new LinkedList<LinkedList<Node>>();
		PartiesSelection = new LinkedList<SelectionModel>();
	}
	
	public SelectionModel copy() { 
// permet de creer une copie de la selection qui ne pointe pas vers l'objet actuel
		SelectionModel tmp = new SelectionModel(group);
		for(int i = 0; i< listeCubeSelectionne.size(); i++) {
			tmp.add(listeCubeSelectionne.get(i));
		}
	
		return tmp;
	}
	//ajoute un cube a la selection
	public void add(Cube b) {
		if(!contains(b)) {
			if(!isInCollision()) {
				b.setDrawMode(DrawMode.LINE); // change l'apparence d'un cube en (drawMode)
				listeCubeSelectionne.add(b);
			}
		}
	}

	// rajoute les parties dans le groupe
	public LinkedList<Image> addPartiesToGroup(){
		LinkedList <Image> creationPartie = new LinkedList<Image>();
		for(int i = 0; i< listeCubeSelectionne.size(); i++){
			group.getChildren().add(listeCubeSelectionne.get(i));
			listeCubeSelectionne.get(i).setDrawMode(DrawMode.FILL);
			File f = new File("src/main/resources/Brochures/etape.png");
			try {//creer l'image
				ImageIO.write(SwingFXUtils.fromFXImage(group.getScene().snapshot(null), null), "png", f);
				creationPartie.add(Image.getInstance(f.getPath()));
				f.delete();
			} catch (IOException | BadElementException e) {
				System.out.println("error PNG");
			}
		}
		return creationPartie;
	}

	// rajoute les pieces de la selection dans le groupe
	public void addPiecesToGroup(){
		for(int i = 0; i< listeCubeSelectionne.size(); i++){
			group.getChildren().add(listeCubeSelectionne.get(i));
			listeCubeSelectionne.get(i).setDrawMode(DrawMode.FILL);
		}
	}
	//vide la selection
	public void clear() {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		if(!isInCollision()){
			if(isFlying()){
				alert.setHeaderText("Placement incorect");
				alert.setContentText("Au moins un de vos cubes vole");
				alert.showAndWait();
			}
			else {
				while (!listeCubeSelectionne.isEmpty()) {
					// desactive le mode drawmode et redonne a un cube son apparence initial
					listeCubeSelectionne.getFirst().setDrawMode(DrawMode.FILL);
					listeCubeSelectionne.removeFirst();
				}
			}
		}
		else{
			alert.setHeaderText("Vous etes en collision");
			alert.setContentText("Veuillez deplacer votre selection dans une position correcte");
			alert.showAndWait();
		}
	}
	
	public boolean empty() {
		return listeCubeSelectionne.isEmpty();
	}
	//verifie si un cube b appartient a la selection
	public boolean contains(Cube b) {
		return listeCubeSelectionne.contains(b);
	}

	public void W(Boolean mute){// incremente de 1 la position du de la selection dans l'axe y
		sortSelectionModel('W');
		for(int i = 0; i < listeCubeSelectionne.size(); i++){
			if(!mute) Audio.soundMove();
			listeCubeSelectionne.get(i).translateZProperty().set(listeCubeSelectionne.get(i).getTranslateZ()+1);
		}
	}

	public void S(Boolean mute){ //decremente la position de la selection de 1 dans l'axe z
		sortSelectionModel('S');
		for(int i = 0; i < listeCubeSelectionne.size(); i++){
			if(!mute) Audio.soundMove();
			listeCubeSelectionne.get(i).translateZProperty().set(listeCubeSelectionne.get(i).getTranslateZ()-1);
		}
	}

	
	public void A(Boolean mute){ //decremente la position de la selection de 1 dans l'axe x
		sortSelectionModel('A');
		for(int i = 0; i < listeCubeSelectionne.size(); i++){
			if(!mute) Audio.soundMove();
			listeCubeSelectionne.get(i).translateXProperty().set(listeCubeSelectionne.get(i).getTranslateX()-1);
		}
	}

	public void D(Boolean mute){ // incremente la position de la selection de 1 dans l'axe x
		sortSelectionModel('D');
		for(int i = 0; i < listeCubeSelectionne.size(); i++){
			if(!mute) Audio.soundMove();
			listeCubeSelectionne.get(i).translateXProperty().set(listeCubeSelectionne.get(i).getTranslateX()+1);
		}
	}

	public void Z(Boolean mute){//decremente la position d'un cube dans l'axe y
		sortSelectionModel('Z');
		for(int i = 0; i < listeCubeSelectionne.size(); i++){
			if(!mute) Audio.soundMove();
			listeCubeSelectionne.get(i).translateYProperty().set(listeCubeSelectionne.get(i).getTranslateY()-1);
		}
	}

//a finir
	public void X(Boolean mute){//incremente de 1 la position des cube de la selection dans l'axe y
		sortSelectionModel('X');
		if(listeCubeSelectionne.get(0).getBoundsInParent().getMaxY()+1<=0)
			for(int i = 0; i < listeCubeSelectionne.size(); i++){
				if(!mute) Audio.soundMove();
				listeCubeSelectionne.get(i).translateYProperty().set(listeCubeSelectionne.get(i).getTranslateY()+1);
			}
	}

	public boolean isInCollision(){
		Cube tmp;
		for(int i=0;i<listeCubeSelectionne.size();i++) {
			for (int y = 1; y < group.getChildren().size(); y++) {
				tmp = (Cube) group.getChildren().get(y);
				if ((!tmp.equals(listeCubeSelectionne.get(i))) && listeCubeSelectionne.get(i).isColliding(tmp)) {
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
		for(int i = 0; i< listeCubeSelectionne.size(); i++) {
			listeCubeSelectionne.get(i).getTransforms().clear();
			listeCubeSelectionne.get(i).getTransforms().addAll(t);
			listeCubeSelectionne.get(i).angleChange(90);
		}
	}

	public void E() {//tourne la selection dans l'axe x de -1
		r = new Rotate(-90, Rotate.Y_AXIS);
		t = t.createConcatenation(r);
		for(int i = 0; i< listeCubeSelectionne.size(); i++) {
			listeCubeSelectionne.get(i).getTransforms().clear();
			listeCubeSelectionne.get(i).getTransforms().addAll(t);
			listeCubeSelectionne.get(i).angleChange(-90);
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
						this.S(true);
						break;
					case S:
						remote(save);
						this.W(true);
						break;
					case A:
						remote(save);
						this.D(true);
						break;
					case D:

						remote(save);
						this.A(true);
						break;
					case Z:
						remote(save);
						this.X(true);
						break;
					case X:
						remote(save);
						this.Z(true);
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
						for(int i = 0; i< listeCubeSelectionne.size(); i++) {
							group.getChildren().add(listeCubeSelectionne.get(i));
						}
						break;
					}


			}
		}
	}
	// prend la couleur a la position x dans le tableau de couleur et l'affecte au cube this
	public void setColors(int x){
		for(int i = 0; i < listeCubeSelectionne.size(); i++){
			listeCubeSelectionne.get(i).setRange(x);
		}
	}
	// prend la selection du dernier mouvement realiser et l'ajoute a la selection actuelle
	public void remote(Save save){
		LinkedList <Cube> s;
			listeCubeSelectionne.clear();
			s = save.remotes.pollLast().listeCubeSelectionne;
			for(int i=0;i<s.size();i++){
				this.add(s.get(i));
			}

	}
	//****************TEST
	public void changeColor() {
		for(int i = 0; i< listeCubeSelectionne.size(); i++) {
			listeCubeSelectionne.get(i).addRandomColor();
		}
	}

	// pareil a revoir
	public void sortSelectionModel(char command){
		switch (command){
			case 'X' :
				for(int i = 1; i < listeCubeSelectionne.size(); i++)
					for(int j = i; j > 0; j--)
						if (listeCubeSelectionne.get(j-1).getTranslateY() < listeCubeSelectionne.get(j).getTranslateY()){
							Cube tmp = listeCubeSelectionne.get(j-1);
							Collections.swap(listeCubeSelectionne,j-1,j);
						}
				break;
			case 'Z' :
				for(int i = 1; i < listeCubeSelectionne.size(); i++)
					for(int j = i; j > 0; j--)
						if (listeCubeSelectionne.get(j-1).getTranslateY() > listeCubeSelectionne.get(j).getTranslateY()){
							Cube tmp = listeCubeSelectionne.get(j-1);
							Collections.swap(listeCubeSelectionne,j-1,j);
						}
				break;
			case 'D' :
				for(int i = 1; i < listeCubeSelectionne.size(); i++)
					for(int j = i; j > 0; j--)
						if (listeCubeSelectionne.get(j-1).getTranslateX() < listeCubeSelectionne.get(j).getTranslateX()){
							Cube tmp = listeCubeSelectionne.get(j-1);
							Collections.swap(listeCubeSelectionne,j-1,j);
						}
				break;
			case 'A' :
				for(int i = 1; i < listeCubeSelectionne.size(); i++)
					for(int j = i; j > 0; j--)
						if (listeCubeSelectionne.get(j-1).getTranslateX() > listeCubeSelectionne.get(j).getTranslateX()){
							Cube tmp = listeCubeSelectionne.get(j-1);
							Collections.swap(listeCubeSelectionne,j-1,j);
						}
				break;
			case 'W' :
				for(int i = 1; i < listeCubeSelectionne.size(); i++)
					for(int j = i; j > 0; j--)
						if (listeCubeSelectionne.get(j-1).getTranslateZ() < listeCubeSelectionne.get(j).getTranslateZ()){
							Cube tmp = listeCubeSelectionne.get(j-1);
							Collections.swap(listeCubeSelectionne,j-1,j);
						}
				break;
			case 'S' :
				for(int i = 1; i < listeCubeSelectionne.size(); i++)
					for(int j = i; j > 0; j--)
						if (listeCubeSelectionne.get(j-1).getTranslateZ() > listeCubeSelectionne.get(j).getTranslateZ()){
							Cube tmp = listeCubeSelectionne.get(j-1);
							Collections.swap(listeCubeSelectionne,j-1,j);
						}
				break;
		}
	}

	public void getId(){
		for(int i = 0; i< listeCubeSelectionne.size(); i++){
			System.out.println(listeCubeSelectionne.get(i).getIdentifiant());
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
		if(listeCubeSelectionne.size()!=0)
			PartiesSelection.add(this.copy());
		if(grapheSelection!=null && listeCubeSelectionne.size()!=0){
			for(int i = 0; i< listeCubeSelectionne.size(); i++){
				tmp.add(grapheSelection.noeuds[listeCubeSelectionne.get(i).getIdentifiant()]);
				group.getChildren().remove(listeCubeSelectionne.get(i));
			}
			Parties.add(tmp);
		}
	}

	public LinkedList<Integer> getIdParties() {
		LinkedList <Integer> listeId = new LinkedList<Integer>();
		for(int i = 0; i< listeCubeSelectionne.size(); i++){
			listeId.add(listeCubeSelectionne.get(i).getIdentifiant());
		}
		return listeId;
	}

	public boolean isFlying(){
		Cube tmp;
		for(int i=1;i<group.getChildren().size();i++){
			tmp = (Cube) group.getChildren().get(i);
			if(fly(tmp))
				return true;
		}
		return false;
	}

	public boolean fly(Cube c){
		Cube tmp;
		boolean up = false;
		boolean down = false;
		for(int i=1;i<group.getChildren().size();i++){
			tmp = (Cube)group.getChildren().get(i);
			if (!tmp.equals(c) && c.checkPos(tmp)) { // checkPos == true if tmp est en dessous de c
				up = true;
			}
			if (!tmp.equals(c) && tmp.checkPos(c)) { // checkPos == true if tmp est au dessus de c
				down = true;
			}
		}
		if(!up && !down && c.getBoundsInParent().getMaxY()!=0)
			return true;
		return false;
	}

	public boolean correctPos (){
		if(isInCollision()){
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText("Vous etes en collision");
			alert.setContentText("Veuillez deplacer votre selection dans une position correcte");
			alert.showAndWait();
			return false;
		}
		if(isFlying()){
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText("Placement incorect");
			alert.setContentText("Au moins un de vos cubes vole");
			alert.showAndWait();
			return false;
		}

		return true;
	}

}
