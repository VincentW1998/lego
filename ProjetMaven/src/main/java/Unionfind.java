import javafx.scene.Group;
import java.util.LinkedList;

public class Unionfind {

    //model
    private Model model;

    //groupe
    private Group groupe;

    // nombre d'élément dans cette UnionFind
    private int size;

    // id[i] pointe vers le parent de i, si id[i] = i alors le i est un noeud racine
    private Coordunioncube[] id;

    private int numComponents;

    public Unionfind(Group group,Model model) {
        groupe = group;
        this.model = model;
        int[] rootab = new int[groupe.getChildren().size()-1];
        for(int i = 1;i<groupe.getChildren().size();i++){
            Cube current = (Cube)groupe.getChildren().get(i);
            rootab[i-1] = current.getIdentifiant();
        }
        size = numComponents = rootab.length;
        id = new Coordunioncube[size];

        for (int i = 0; i < size; i++) {
            id[i] = new Coordunioncube(rootab[i]);
            id[i].sz  = 1; // originellement tout les élément sont de taille 1
        }
    }

    // trouve a quelle famille 'p' appartient, p pouvant etre un groupe de cubes
    public int find(int p) {

        // trouve la racine de p
        int root = p;
        while (root != getCube(root).getRootid()) {
            root = getCube(root).getRootid();
        }
        // Compresse le chemin qui nous mene a la racine
        // cette opération est le "path compression"
        while (p != root) {
            int next = getCube(p).getRootid();
            setRootid(p,root);
            p = next;
        }

        return root;
    }

    // alternative recursive de la methode find
    // public int find(int p) {
    //   if (p == id[p]) return p;
    //   return id[p] = find(id[p]);
    // }

    // Retourne si 'p' et 'q' appartiennent à la même famille (groupe de cubes)
    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }

    // Retourne la taille du groupe de cubes associé à 'p'
    public int componentSize(int p) {
        return getCube(p).sz;
    }

    // Retourne le nombre d'élément dans cette Unionfind
    public int size() {
        return size;
    }

    // Retourne le nombre restant d'élément dans cette UnionFind
    public int components() {
        return numComponents;
    }

    // Regroupe les groupes de cubes contenant les éléments 'p' et 'q'
    public void unify(int p, int q) {

        int root1 = find(p);
        int root2 = find(q);

        // Ces élément sont déjà dans le même groupe
        if (root1 == root2) return;

        // Unifie le plus petit groupe de bloc dans le plus grand
        if (getCube(root1).sz < getCube(root2).sz) {
            getCube(root2).sz += getCube(root1).sz;
            setRootid(root1,root2);
        } else {
            getCube(root1).sz += getCube(root2).sz;
            setRootid(root2,root1);
        }

        // Comme il y a eu une fusion de deux groupe de cubes le nombre d'élément est donc réduit de 1
        numComponents--;
    }

    public Coordunioncube[] getId(){return id;}

    public void setRootid(int ind, int id){//permet de changer le Rootid du coordunioncube a l'id "ind" a "id"
        for(int i =0; i < this.id.length ; i++){//On parcours toute la liste des coordunioncube
            if(this.id[i].getId() == ind) // On trouve la coordunioncube ayant pour id "ind"
                this.id[i].setRootid(id);
        }
    }

    public void setRootlock(int ind){//vérouille la valeur rootid d'un coordunioncube
        for(int i =0; i < this.id.length ; i++){
            if(this.id[i].getId() == ind)
                this.id[i].rootlock = true;
        }
    }

    public Coordunioncube getCube(int p){//renvoie la Coordunioncube ayant pour id 'p' null sinon
        for(int i = 0;i<id.length;i++)
            if(id[i].getId() == p)return id[i];
        return null;
    }

    public LinkedList getListe(){//renvoie la liste de tout les roots / nom de groupe
        LinkedList<Integer> listePivot = new LinkedList<Integer>();
        for(int i =0;i<id.length;i++)
            if(!listePivot.contains(id[i].getRootid()))
                listePivot.add(id[i].getRootid());

        return listePivot;
    }

    public LinkedList getList(int r,LinkedList<Coordunioncube> l){
        LinkedList<Node> list = new LinkedList<Node>();
        for(int i=0;i<l.size();i++) {
            if (l.get(i).getRootid() == r) {
                list.add(model.graphConstruction.noeuds[l.get(i).getId()]);
                l.remove(i);
                i--;
            }
        }
        return list;
    }


    public void setPartie(){
        LinkedList<Coordunioncube> newId = new LinkedList<Coordunioncube>();
         for(int i=0;i<this.id.length;i++){
             newId.add(id[i]);
         }
         int y=1;
         while(!newId.isEmpty()){
             LinkedList<Node> list = getList(newId.getFirst().getRootid(),newId);
             model.selection.Parties.add(list);

         }
    }

    public void makeUnionfindId(){//
        for(int i = 1; i < groupe.getChildren().size(); i ++){
            Cube tmp = (Cube) groupe.getChildren().get(i);
            Cube tmpb = tmp;
            int idtmpb = -1;
            int cval = -1;
            LinkedList<Integer> liste = new LinkedList<Integer>();//liste contenant les id des cubes étant en dessous de tmp
            for(int j = 1;j< groupe.getChildren().size();j++) {
                tmpb = (Cube)groupe.getChildren().get(j);
                if (!tmp.equals(tmpb) && tmpb.checkPos(tmp)) { // checkPos == true if tmp est en dessous de tmpb
                    idtmpb = tmpb.getIdentifiant();//change la valeur de idtmpb pour dire qu'il existe une piece au dessus
                    unify(tmpb.getIdentifiant(), tmp.getIdentifiant());
                }

                if (!tmp.equals(tmpb) && tmp.checkPos(tmpb))  // checkPos == true if tmp est au dessus de tmpb
                    if(!liste.contains(tmpb.getIdentifiant())) // vérifie que le cube n'est pas déjà dans la liste
                        liste.add(tmpb.getIdentifiant());
            }
            cval = tmp.getIdentifiant();
            if(liste.size() > 1) {
                setRootid(cval, cval);//le bloc est un root
                setRootlock(cval);//on vérouille le cube
            }
            if(liste.size() == 1 && idtmpb == -1) // si le cube est au dessus d'un cube mais en dessous de personne alors
                setRootid(cval, liste.get(0));//on initialise son groupe au root du cube en dessous

        }
        LinkedList<Integer> end = getListe();//on récupere la liste des groupes
        for(int i = 0;i<id.length;i++){//on remet les bons id au cubes root
            if(end.contains(id[i].getId()))
                setRootid(i,id[i].getId());
        }
    }

}