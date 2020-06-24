import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;


public class Model {

    private class Ground extends Cube{
        public Ground(int w, int d) {
            super(Color.rgb(200, 200, 200, 0.2),w,0.01,d);
            Cube.setId(this,-1);
        }
    }

    public Stage primaryStage;
    public Stage secondStage;
    public Group group;
    public Scene scene;
    public Camera camera;
    public SelectionModel selection;
    public Ground sol;
    public Save save;
    public Boolean mute;
    public Graph graphConstruction;
    final FileChooser fileChooser;
    private static final int DEPTH = 800;
    private static final int WIDTH = 1200;
    public File CurrentBrochure;

    public Controller controller;

    public Model(Stage primaryS, Stage secondS) {
        primaryStage = primaryS;
        secondStage = secondS;
        group = new Group();
        int [] rootab = new int[group.getChildren().size()];
        scene = new Scene(group, WIDTH, DEPTH, true);
        camera = new PerspectiveCamera(true);
        selection = new SelectionModel(group);
        sol = new Ground(WIDTH, DEPTH);
        save = new Save();
        graphConstruction = new Graph(0);
        fileChooser = new FileChooser();
        mute = false;

    }


    public void setup(){
        scene.setCamera(camera);
        group.getChildren().add(sol);
        sol.translateYProperty().set(0.004999999888241291);//deplacement du sol en dessous de 0
        camera.translateXProperty().set(0);
        camera.translateYProperty().set(-1.5);
        camera.translateZProperty().set(-15);

        camera.setNearClip(1);
        camera.setFarClip(1000);// definit la distance de vue de la camera
        scene.setFill(Color.WHITE);

    }

    public void reordonnerGroup(){
        Cube tmp;
        for(int i = 1; i < group.getChildren().size(); i++){
            tmp = (Cube) group.getChildren().get(i);
            Cube.setId(tmp,i - 1);
        }
    }




}
