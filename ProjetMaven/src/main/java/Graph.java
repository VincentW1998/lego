import javafx.scene.Group;

public class Graph {

    Node [] noeuds; // liste des noeud de la construction
    Group group;

   // initialisation de chaque noeud du graphe
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

   public  void addArretesUp(Cube src, Cube dest){
       noeuds[src.getIdentifiant()].addArretesUp(noeuds[dest.getIdentifiant()]);
   }


//   public void parcoursGraphe() {
//        for (int i = 0 ; i < noeuds.length; i ++) {
//            if (noeuds[i].poser == false)
//                System.out.println(i);
//        }
//   }

   public void createGraph(Group group){
        this.group = group;
        Cube tmp;
        for (int i = 1; i < group.getChildren().size(); i ++){
            tmp = (Cube) group.getChildren().get(i);
            add(tmp);
            attachedTo(tmp);
        }
   }



    public void attachedTo(Cube c){
        for(int i = 1; i < group.getChildren().size(); i ++){
            {
                Cube tmp = (Cube) group.getChildren().get(i);
                if (!tmp.equals(c) && c.checkPos(tmp)) { // checkPos == true if tmp est en dessous de c
                    addArretes(c, tmp);
                }
                if (!tmp.equals(c) && tmp.checkPos(c)) { // checkPos == true if tmp est au dessus de c
                    addArretesUp(c, tmp);
                }
            }
        }
    }

    public void printGraph(){
        System.out.println("----- Affichage -------");
        System.out.println("Identifiant de la piece P : les autres pieces sur lesquels la piece P est posee");
        for (int i = 0; i < noeuds.length; i ++){
            System.out.println("\nPiece n°" + i + ": ");
            noeuds[i].print();
        }
    }
}
