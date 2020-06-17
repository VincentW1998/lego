public class Unionfind {
    //model
    private Model model;

    // nombre d'élément dans cette UnionFind
    private int size;

    // id[i] pointe vers le parent de i, si id[i] = i alors le i est un noeud racine
    private Coordunioncube[] id;

    private int numComponents;

    public Unionfind(int[] rootab) {
        if (rootab.length <= 0) throw new IllegalArgumentException("Taille incorrect");

        size = numComponents = rootab.length;
        id = new Coordunioncube[size];

        for (int i = 0; i < size; i++) {
            id[i] = new Coordunioncube(rootab[i],rootab[i]);
            id[i].sz  = 1; // originellement tout les élément sont de taille 1
        }
    }

    // trouve a quelle famille 'p' appartient, p pouvant etre un groupe de cubes
    public int find(int p) {

        // trouve la racine de p
        int root = getCube(p).getId();
        while (root != getCube(root).getId()) root = getCube(root).getId();

        // Compresse le chemin qui nous mene a la racine
        // cette opération est le "path compression"
        while (p != root) {
            int next = getCube(p).getId();
            getCube(p).setRootid(root);
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
            getCube(root1).setRootid(root2);
        } else {
            getCube(root1).sz += getCube(root2).sz;
            getCube(root2).setRootid(root1);
        }

        // Comme il y a eu une fusion de deux groupe de cubes le nombre d'élément est donc réduit de 1
        numComponents--;
    }

    public Coordunioncube[] getId(){return id;}

    public Coordunioncube getCube(int p){//renvoie la Coordunioncube ayant pour id 'p' null sinon
        for(int i = 0;i<id.length;i++)
            if(id[i].getId() == p)return id[i];
        return null;
    }

}