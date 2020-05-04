import javafx.event.Event;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.LinkedList;

public class Save {
	LinkedList<SelectionModel> lastElements;
	LinkedList<KeyEvent> moves;
	LinkedList<SelectionModel> remotes;
	LinkedList<Cube> cubes;

	public Save() {
		lastElements = new LinkedList<SelectionModel>();
		moves = new LinkedList<KeyEvent>();
		remotes = new LinkedList<SelectionModel>();

		cubes = new LinkedList<Cube>();
	}

	public void newCube(Cube c){
		cubes.add(c);
	}

	public void saveMoves(KeyEvent event){
		moves.add(event);
	}

	public void saveRemote(SelectionModel m){
		remotes.add(m);
	}


//	public void saveRemove(SelectionModel m) {
//		lastElements.add(m);
//	}
//
//	public SelectionModel pop() {
//		return lastElements.pollLast();
//	}
//
//	public boolean Empty() {
//		return lastElements.isEmpty();
//	}
}
