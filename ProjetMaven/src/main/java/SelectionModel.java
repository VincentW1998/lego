import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.shape.DrawMode;

import javax.imageio.ImageIO;

public class SelectionModel {
	LinkedList <Piece> listeCubeSelectionne;
	Group group;
	LinkedList <LinkedList<Node>> Parties;
	LinkedList <SelectionModel> PartiesSelection;
	public SelectionModel(Group g) {
		listeCubeSelectionne = new LinkedList<Piece>();
		group = g;
		Parties = new LinkedList<LinkedList<Node>>();
		PartiesSelection = new LinkedList<SelectionModel>();
	}
	
	public SelectionModel copy() {
// permet de creer une copie de la selection qui ne pointe pas vers l'objet actuel
		SelectionModel tmp = new SelectionModel(group);
		for(int i = 0; i< listeCubeSelectionne.size(); i++) {
			// ne fonctionne pas avec la fonction add de SelectionModel
			tmp.listeCubeSelectionne.add(listeCubeSelectionne.get(i).copy());
			tmp.listeCubeSelectionne.getLast().setDrawMode(DrawMode.LINE);
		}
		return tmp;
	}

	//ajoute un cube a la selection
	public void add(Piece b) {
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
			Piece.moveToLoc(listeCubeSelectionne.get(i));
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
	public boolean contains(Piece b) {
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
	
	public void X(Boolean mute){//incremente de 1 la position des cube de la selection dans l'axe y
		sortSelectionModel('X');
		if(listeCubeSelectionne.get(0).getBoundsInParent().getMaxY()+1<=0)
			for(int i = 0; i < listeCubeSelectionne.size(); i++){
				if(!mute) Audio.soundMove();
				listeCubeSelectionne.get(i).translateYProperty().set(listeCubeSelectionne.get(i).getTranslateY()+1);
			}
	}

	public boolean isInCollision(){
		Piece tmp;
		for(int i=0;i<listeCubeSelectionne.size();i++) {
			for (int y = 1; y < group.getChildren().size(); y++) {
				tmp = (Piece) group.getChildren().get(y);
				if ((!tmp.equals(listeCubeSelectionne.get(i))) && listeCubeSelectionne.get(i).isColliding(tmp)) {
					return true;
				}
			}
		}
		return false;
	}

	public void sortSelectionModel(char command){
		switch (command){
			case 'X' :
				for(int i = 1; i < listeCubeSelectionne.size(); i++)
					for(int j = i; j > 0; j--)
						if (listeCubeSelectionne.get(j-1).getTranslateY() < listeCubeSelectionne.get(j).getTranslateY()){
							Collections.swap(listeCubeSelectionne,j-1,j);
						}
				break;
			case 'Z' :
				for(int i = 1; i < listeCubeSelectionne.size(); i++)
					for(int j = i; j > 0; j--)
						if (listeCubeSelectionne.get(j-1).getTranslateY() > listeCubeSelectionne.get(j).getTranslateY()){
							Collections.swap(listeCubeSelectionne,j-1,j);
						}
				break;
			case 'D' :
				for(int i = 1; i < listeCubeSelectionne.size(); i++)
					for(int j = i; j > 0; j--)
						if (listeCubeSelectionne.get(j-1).getTranslateX() < listeCubeSelectionne.get(j).getTranslateX()){
							Collections.swap(listeCubeSelectionne,j-1,j);
						}
				break;
			case 'A' :
				for(int i = 1; i < listeCubeSelectionne.size(); i++)
					for(int j = i; j > 0; j--)
						if (listeCubeSelectionne.get(j-1).getTranslateX() > listeCubeSelectionne.get(j).getTranslateX()){
							Collections.swap(listeCubeSelectionne,j-1,j);
						}
				break;
			case 'W' :
				for(int i = 1; i < listeCubeSelectionne.size(); i++)
					for(int j = i; j > 0; j--)
						if (listeCubeSelectionne.get(j-1).getTranslateZ() < listeCubeSelectionne.get(j).getTranslateZ()){
							Collections.swap(listeCubeSelectionne,j-1,j);
						}
				break;
			case 'S' :
				for(int i = 1; i < listeCubeSelectionne.size(); i++)
					for(int j = i; j > 0; j--)
						if (listeCubeSelectionne.get(j-1).getTranslateZ() > listeCubeSelectionne.get(j).getTranslateZ()){
							Collections.swap(listeCubeSelectionne,j-1,j);
						}
				break;
		}
	}


	//separe la selection de la structure (creation d'une partie) et la supprime
	public void separation(Graph grapheSelection){
		grapheSelection.createGraph(group);
		LinkedList <Node> tmp = new LinkedList<Node>();
		if(listeCubeSelectionne.size()!=0){
			PartiesSelection.add(this.copy());
			if(grapheSelection!=null){
				for(int i = 0; i< listeCubeSelectionne.size(); i++){
					tmp.add(grapheSelection.noeuds[listeCubeSelectionne.get(i).getIdentifiant()]);
					listeCubeSelectionne.get(i).setVisible(false);
				}
				Parties.add(tmp);
			}
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
		Piece tmp;
		for(int i=1;i<group.getChildren().size();i++){
			tmp = (Piece) group.getChildren().get(i);
			if(fly(tmp))
				return true;
		}
		return false;
	}

	public boolean fly(Piece c){
		Piece tmp;
		boolean up = false;
		boolean down = false;
		for(int i=1;i<group.getChildren().size();i++){
			tmp = (Piece)group.getChildren().get(i);
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
