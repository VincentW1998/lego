import java.util.LinkedList;

public class Node {
    Cube c;
    LinkedList<Node> adj; // aretes

    public Node(){
        adj = new LinkedList<Node>();
    }
    public Node(Cube cb){
        this();
        c = cb;
    }

    public void addCube(Cube cb){ //intialise le cube
        c = cb;
    }

    public void addAdj(Node n){
        adj.add(n);
    }
}
