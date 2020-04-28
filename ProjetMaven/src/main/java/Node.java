import java.util.LinkedList;

public class Node {
    Cube c;
    LinkedList<Node> arretes; // arretes pieces en dessous
    LinkedList<Node> arretesUp; // arretes pieces au dessus

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

// affiche les attaches d'un cube
    public void print(){
        System.out.print("[");
        for(int i = 0; i< arretes.size() ; i++){
            if (i == 0)
                System.out.print(arretes.get(i).c.getIdentifiant());
            else
                System.out.print(", "+arretes.get(i).c.getIdentifiant());
        }
        System.out.println("]");
    }

    public void addArretes(Node n){
        arretes.add(n);
    }
    public void addArretesUp(Node n){
        arretesUp.add(n);
    }
}
