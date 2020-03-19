import java.util.LinkedList;

public class Graph {

   Node [] nodes;

    public Graph(int l){
        nodes = new Node[l];
        for(int i=0;i<l;i++){
            nodes[i] = new Node();
        }

    }

   public void add(Cube c){
        nodes[c.getIdentifiant()-2].addCube(c);
   }



   public void addAjd(Cube src, Cube dest ){ // ajoute une arrete
        nodes[src.getIdentifiant()-2].addAdj(nodes[dest.getIdentifiant()-2]);
   }


}
