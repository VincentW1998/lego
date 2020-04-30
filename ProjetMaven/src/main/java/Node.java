import java.util.LinkedList;

public class Node {
    Cube c; // noeud
    LinkedList<Node> arretesDown; // arretes pieces en dessous (liens)
    LinkedList<Node> arretesUp; // arretes pieces au dessus (liens)
    int ordreConstruction = -1; // utilisation explicite
    static int acc = -1;
    int numeroPartie;


    public Node(){
        arretesDown = new LinkedList<Node>();
        arretesUp = new LinkedList<Node>();
    }

    public Node(Cube cb){
        this();
        c = cb;

    }

    public void addCube(Cube cb){ //initialise le cube
        c = cb;
    }

    // affiche la liste des arretes et arreteUP
    public void print(){
       afficheArrete();
       affichageArreteUp();
    }

    // affiche liste arrete
    public void afficheArrete()
    {
        System.out.print("Liste des pieces en dessous : [");
        for(int i = 0; i< arretesDown.size() ; i++){
            if (i == 0)
                System.out.print(arretesDown.get(i).c.getIdentifiant());
            else
                System.out.print(", " + arretesDown.get(i).c.getIdentifiant());
        }
        System.out.println("]");
    }

    // affiche liste arretesUp
    public void affichageArreteUp() {
        System.out.print("Liste des pieces au dessus : [");
        for(int i = 0; i< arretesUp.size() ; i++){
            if (i == 0)
                System.out.print(arretesUp.get(i).c.getIdentifiant());
            else
                System.out.print(", " + arretesUp.get(i).c.getIdentifiant());
        }
        System.out.println("]");
    }


    public void addArretes(Node n){
        arretesDown.add(n);
    }
    public void addArretesUp(Node n){
        arretesUp.add(n);
    }



    // renvoie true si l'arrete à au plus n Nodes
    public boolean hasHowManyDown(int n){
        return arretesDown.size() <= n;
    }



    public void parcoursArreteUp(){

        for (int i = 0; i < arretesUp.size(); i++) { // parcours chaque node superieur
            // si le Node n'a que 1 seul lien vers le bas ou si le Node a plusieurs arretes down, mais qu'ils ont deja ete tous visite
            // on lui attribut un numero
            if(!arretesUp.get(i).isAlreadySee()){
                if (arretesUp.get(i).hasHowManyDown(1) || arretesUp.get(i).fullUnderSee()) {
                    acc ++;
                    arretesUp.get(i).ordreConstruction = acc;
                    arretesUp.get(i).parcoursArreteUp();
                }
                // sinon on check ses nodes autres nodes inferieur
                else {
                    arretesUp.get(i).parcoursArreteDown();
                    break;
                }
            }

        }
    }

    public  void parcoursArreteDown(){
        // si il n'y a plus aucun cube en dessous on lui donne un ordre ou si tout les noeds on ete vistes
        if (hasHowManyDown(0) || fullUnderSee()){
            acc ++;
            ordreConstruction = acc;
            parcoursArreteUp();
            return;
        }
        for (int i = 0; i < arretesDown.size(); i++) {
            // si le noeud n'a pas ete visite ou le visite et on parcours ses noeuds inf
            if (!arretesDown.get(i).isAlreadySee()){
                arretesDown.get(i).parcoursArreteDown();
            }

        }

    }

    // Si le noeud a un ordre different de -1, cela veut dire qu'on l'a deja vu lors du parcours
    public boolean isAlreadySee(){
        return ordreConstruction != -1;
    }

    // renvoie true si toutes les noeuds en dessous du this ont ete visites.
    public boolean fullUnderSee(){
        for(int i = 0; i < arretesDown.size(); i++){
            if(!arretesDown.get(i).isAlreadySee())
                return false;
        }
        return true;
    }

    // affiche l'ordre de construction
    public void printNodeOrder(){
        System.out.println(ordreConstruction);
    }




}
