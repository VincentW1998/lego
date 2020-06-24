public class Coordunioncube {
    private int id;
    private int rootid;
    public int sz;
    public boolean rootlock;

    public Coordunioncube(int id){
        this.id= id;
        this.rootid = id ;
        rootlock = false;
    }

    public int getId(){return id;}

    public int getRootid(){return rootid;}

    public void setRootid(int a){this.rootid = a;}
}
