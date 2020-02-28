import java.util.LinkedList;

public class Save {
	LinkedList<SelectionModel> lastElements;
	
	public Save() {
		lastElements = new LinkedList<SelectionModel>();
	}
	
	public void add(SelectionModel m) {
		lastElements.add(m);
	}
	
	public SelectionModel pop() {
		return lastElements.pollLast();
	}
	
	public boolean Empty() {
		return lastElements.isEmpty();
	}
}
