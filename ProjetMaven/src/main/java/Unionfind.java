public class Unionfind {
    //model
    private Model model;

    // nombre d'élément dans cette UnionFind
    private int size;

    // pointeur vers la taille de chaque élément
    private int[] sz;

    // id[i] pointe vers le parent de i, si id[i] = i alors le i est un noeud racine
    private int[] id;

    private int numComponents;

    public Unionfind() {

        if (model.group.getChildren().size() <= 0) throw new IllegalArgumentException("Taille incorrect");

        size = numComponents = model.group.getChildren().size();
        sz = new int[size];
        id = new int[size];

        for (int i = 0; i < size; i++) {
            Cube current = (Cube)model.group.getChildren().get(i);
            id[current.getIdentifiant()] = current.getIdentifiant(); // pointe vers lui même
            sz[i] = 1; // originellement tout les élément sont de taille 1
        }
    }

    // trouve a quelle famille 'p' appartient, p pouvant etre un groupe de cubes
    public int find(int p) {

        // trouve la racine de p
        int root = p;
        while (root != id[root]) root = id[root];

        // Compresse le chemin qui nous mene a la racine
        // cette opération est le "path compression"
        while (p != root) {
            int next = id[p];
            id[p] = root;
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
        return sz[find(p)];
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
        if (sz[root1] < sz[root2]) {
            sz[root2] += sz[root1];
            id[root1] = root2;
        } else {
            sz[root1] += sz[root2];
            id[root2] = root1;
        }

        // Comme il y a eu une fusion de deux groupe de cubes le nombre d'élément est donc réduit de 1
        numComponents--;
    }
}