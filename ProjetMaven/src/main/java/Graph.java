import javafx.scene.Group;

/* Classe qui creer le graphe : Une liste de noeud */

public class Graph {

    Node [] noeuds; // liste des noeud de la construction

    Group group;

    Unionfind unionfind;


   // initialisation de chaque noeud du graphe
    public Graph(int l){
        noeuds = new Node[l];
        for(int i=0;i<l;i++){
            noeuds[i] = new Node();
        }
    }

   public void add(Piece c){
        noeuds[c.getIdentifiant()].addCube(c); //ajoute le cube dans le Node []
   }
//ajoute une arrete au noeud src
   public void addArretes(Piece src, Piece dest ){ // ajoute une arrete
        noeuds[src.getIdentifiant()].addArretes(noeuds[dest.getIdentifiant()]);
   }

   public  void addArretesUp(Piece src, Piece dest){
       noeuds[src.getIdentifiant()].addArretesUp(noeuds[dest.getIdentifiant()]);
   }

    // creer le graphe
    public void createGraph(Group group){
        this.group = group;
        Piece tmp;
        for (int i = 1; i < group.getChildren().size(); i ++){
            tmp = (Piece) group.getChildren().get(i);
            add(tmp);
            attachedTo(tmp);
        }
    }

    public void createGraphUF(Group group, Model model){
        this.group = group;
        Piece tmp;
        for (int i = 1; i < group.getChildren().size(); i ++){
            tmp = (Piece) group.getChildren().get(i);
            add(tmp);
            attachedTo(tmp);
        }
        unionfind = new Unionfind(group,model);
        unionfind.makeUnionfindId();
    }

    public void attachedTo(Piece c){ // ajoute les cubes dans les arretes && arretesUP
        for(int i = 1; i < group.getChildren().size(); i ++){
            {
                Piece tmp = (Piece) group.getChildren().get(i);
                if (!tmp.equals(c) && c.checkPos(tmp)) { // checkPos == true if tmp est en dessous de c
                    addArretes(c, tmp);
                }
                if (!tmp.equals(c) && tmp.checkPos(c)) { // checkPos == true if tmp est au dessus de c
                    addArretesUp(c, tmp);
                }
            }
        }
    }

    public void printGraph(){ // affiche le graphe
        System.out.println("----- Affichage -------");
        System.out.println("Identifiant de la piece P : les autres pieces sur lesquels la piece P est posee");
        for (int i = 0; i < noeuds.length; i ++){
            System.out.println("\nPiece n??" + i + ": ");
            noeuds[i].print();
        }
    }

    // attribut un ordre au graphe
    public void giveOrderToGraph() {
        while (!FullOrder()){ // tant que tout les noeuds n'ont pas ete vu
            int debut = findBegin(); // on cherche un noeud de debut
            if(debut == -1) break;
            noeuds[debut].ordreConstruction = ++ Node.acc; // on lui donne un ordre
            noeuds[debut].parcoursArreteUp(); // puis on parcours les arretes superieur du noeud du debut
        }

    }

    // return l'indice du premier noued qui n'a pas d'arrete vers le bas (peut importe lequel)
    public int findBegin() {
        for (int i = 0; i < noeuds.length; i++) {
            if (noeuds[i].hasHowManyDown(0) && !noeuds[i].isAlreadySee()) // si le noeud n'a pas de liens vers le bas
                return i;
        }
        return -1;
    }

    // affiche l'ordre de consutrction
    public void printOrder() {
        sortOrder();
        System.out.println("Ordre de construction de la figure : ");
        for (int i = 0; i < noeuds.length; i ++){
            System.out.println(i + " -- cube " + noeuds[i].c.getIdentifiant());
        }
    }

    // Renvoie true si tout les noueds ont ete visites et ordonner
    public boolean FullOrder(){
        for (int i = 0; i < noeuds.length; i++) {
            if (!noeuds[i].isAlreadySee()) // si le noeud n'a pas ete visit??
                return false;
        }
        return true;
    }

    // trie par selection en fonction de l'ordre de construction
    public void sortOrder(){
        for (int i = 1; i < noeuds.length; i ++) {
            for (int j = i; j > 0; j --) {
                if(noeuds[j - 1].ordreConstruction > noeuds[j].ordreConstruction){
                    Node tmp = noeuds[j - 1];
                    noeuds[j - 1] = noeuds[j];
                    noeuds[j] = tmp;
                }
            }
        }
    }

    public void afficherCubes(){
        for(int i = 0;i<unionfind.getId().length;i++){
            System.out.println("id cube: "+unionfind.getId()[i].getId()+"  id root: "+unionfind.getId()[i].getRootid());
        }
    }

}
