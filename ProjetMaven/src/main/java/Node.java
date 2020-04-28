import java.util.LinkedList;

public class Node {
    Cube c;
    LinkedList<Node> arretes; // aretes
    LinkedList<Node> arretesUp;

    public Node(){
        arretes = new LinkedList<Node>();
        arretesUp = new LinkedList<Node>();
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
            System.out.print(arretes.get(i).c.getIdentifiant()+" / ");
        }
        System.out.println();
    }

    public void addArretes(Node n){
        arretes.add(n);
    }
    public void addArretesUp(Node n){
        arretesUp.add(n);
    }
}
