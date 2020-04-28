public class Graph {

   Node [] noeuds;

    public Graph(int l){
        noeuds = new Node[l];
        for(int i=0;i<l;i++){
            noeuds[i] = new Node();
        }

    }

   public void add(Cube c){
        noeuds[c.getIdentifiant()].addCube(c); //ajoute le cube dans le Node []
   }

   public void addArretes(Cube src, Cube dest ){ // ajoute une arrete
        noeuds[src.getIdentifiant()].addArretes(noeuds[dest.getIdentifiant()]);
   }


}
