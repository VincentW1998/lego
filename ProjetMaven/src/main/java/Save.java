import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.DrawMode;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

import java.util.LinkedList;

public class Save {
	LinkedList<KeyEvent> moves;
	LinkedList<SelectionModel> remotes;


	public Save() {
		moves = new LinkedList<KeyEvent>();
		remotes = new LinkedList<SelectionModel>();
	}

	public void saveMoves(KeyEvent event){
		moves.add(event);
	}

	public void saveRemote(SelectionModel m){
		remotes.add(m);
	}

	public void undo(Model model){
		if(!moves.isEmpty() && !remotes.isEmpty()){
			//vide la selection (on utilise pas clear de selection car clear check les collisions
			while(!model.selection.listeCubeSelectionne.isEmpty()){
				model.selection.listeCubeSelectionne.getFirst().setDrawMode(DrawMode.FILL);
				model.selection.listeCubeSelectionne.removeFirst();
			}

			KeyEvent move = moves.pollLast();
			SelectionModel s = remotes.pollLast();
			Cube tmp;
			for(int i=0;i<s.listeCubeSelectionne.size();i++){
				for(int y=1;y<model.group.getChildren().size();y++){
					tmp = (Cube)model.group.getChildren().get(y);
					if(s.listeCubeSelectionne.get(i).SerialNb == tmp.SerialNb) {
						model.group.getChildren().remove(y);
						break;
					}
				}
			}
			if(move.getCode() != KeyCode.N) {
				for (int i = 0; i < s.listeCubeSelectionne.size(); i++) {
					tmp = s.listeCubeSelectionne.get(i).copy();
					model.group.getChildren().add(tmp);
					//ajoute le cube a la selection (on utilise pas la fonction add de selection car la fonction check les collisions
					model.selection.listeCubeSelectionne.add((Cube) model.group.getChildren().get(model.group.getChildren().size()-1));
					model.selection.listeCubeSelectionne.getLast().setDrawMode(DrawMode.LINE);
					tmp.translateXProperty().set(s.listeCubeSelectionne.get(i).x);
					tmp.translateYProperty().set(s.listeCubeSelectionne.get(i).y);
					tmp.translateZProperty().set(s.listeCubeSelectionne.get(i).z);
					tmp.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
						//verifie si la selection n'est pas en collision et si elle n'est pas en vol et affiche les messages d'erreur si c'est le cas
						if (model.selection.correctPos()) {
							if (!e.isShiftDown())
								model.selection.clear();
							model.selection.add((Cube) e.getSource());
						}
					});
				}
			}
		}
	}


}
