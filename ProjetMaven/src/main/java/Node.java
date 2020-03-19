import java.util.LinkedList;

public class Node {
    Cube c;
    LinkedList<Node> arretes; // aretes

    public Node(){
        arretes = new LinkedList<Node>();
    }
    public Node(Cube cb){
        this();
        c = cb;
    }

    public void addCube(Cube cb){ //intialise le cube
        c = cb;
    }

    public void print(){
        for(int i = 0; i< arretes.size(); i++){
            System.out.println(arretes.get(i).c.getIdentifiant());
        }
    }

    public void addArretes(Node n){
        arretes.add(n);
    }
}
