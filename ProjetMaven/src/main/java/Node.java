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

    public void print(){
        for(int i=0;i<adj.size();i++){
            System.out.println(adj.get(i).c.getIdentifiant());
        }
    }

    public void addAdj(Node n){
        adj.add(n);
    }
}
