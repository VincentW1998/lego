import java.util.LinkedList;

public class Node {
    Cube c;
    LinkedList<Node> arretes; // arretes


    public Node(){
        arretes = new LinkedList<Node>();
    }

    public Node(Cube cb){
        this();
        c = cb;
    }

    // Pas sur de cette fonction ?
    public void addCube(Cube cb){ //intialise le cube
        c = cb;
    }


    public void print(){
        for(int i = 0; i< arretes.size(); i++){
//            System.out.print(arretes.get(i).c.getIdentifiant()+" / ");
            System.out.println(arretes);
        }
        System.out.println();
    }

    public void addArretes(Node n){
        arretes.add(n);
    }
}
