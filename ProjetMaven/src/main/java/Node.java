import java.util.LinkedList;

public class Node {
    Cube c; // noeud
    LinkedList<Node> arretesDown; // arretes pieces en dessous (liens)
    LinkedList<Node> arretesUp; // arretes pieces au dessus (liens)
    int ordreConstruction; // utilisation explicite
    int numeroPartie;


    public Node(){
        arretesDown = new LinkedList<Node>();
        arretesUp = new LinkedList<Node>();
    }

    public Node(Cube cb){
        this();
        c = cb;
        ordreConstruction = -1;
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
        int oc = 1;

        for (int i = 0; i < arretesUp.size(); i++) { // parcours chaque node superieur
            // si le Node n'a que 1 seul lien vers le bas
            // on lui attribut un numero
            if (arretesUp.get(i).hasHowManyDown(1)) {
                arretesUp.get(i).ordreConstruction = oc;
                oc++;
            }
            // sinon on check ses nodes autres nodes inferieur
            else {
                parcoursArreteDown(oc);
            }

        }
    }

    public  void parcoursArreteDown(int oc){
        if (hasHowManyDown(0)){ // si il n'y a plus aucun cube en dessous
            ordreConstruction = oc;
//            return oc + 1;
        }
        int n = 0;
        for (int i = 0; i < arretesDown.size(); i++) {
            if (arretesDown.get(i).isAlreadySee()){
                n ++;
            }
            else {
                arretesDown.get(i).parcoursArreteDown(oc);
            }
        }
//        if (n == arretesDown.size()) // cela veut dire que le noeud à tout ses autres noeud inferieur deja visites.

    }

    // Si le noeud a un ordre different de -1, cela veut dire qu'on l'a deja vu lors du parcours
    public boolean isAlreadySee(){
        return ordreConstruction != -1;
    }




}
