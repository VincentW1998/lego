import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.DrawMode;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class Brochure {

    static LinkedList <File> fichierPng = new LinkedList<File>();

    /* Creer une brochure à partir du decoupage manuel */

    public static void creationBrochure(Scene scene, Group group, SelectionModel selection) {
        File parties = new File("src/main/resources/Brochures/Parties");
        File assemblage = new File("src/main/resources/Brochures/Assemblage");
        //creation du dossier Parties et Assemblage
        parties.mkdir();
        assemblage.mkdir();

        for (int i = 0; i < selection.PartiesSelection.size(); i++) {//crée les png des parties
            {
                File part = new File("src/main/resources/Brochures/Parties/Partie"+(i+1));
                part.mkdir();
            }
            while(group.getChildren().size() > 1)//vide le groupe en laissant le sol
                group.getChildren().remove(1);
            selection.PartiesSelection.get(i).addPartiesToGroup(i+1); // ajoute chaque partie dans le groupe
//
        }
        while(group.getChildren().size() > 1)
            group.getChildren().remove(1);

        for (int i = 0; i < selection.PartiesSelection.size(); i++) {// crée les png de l'assemblage des parties

            selection.PartiesSelection.get(i).addPiecesToGroup(); // Pour chaque parties, on ajoute toutes les pieces de la parties dans le groupe
            try {//creer l'image
                ImageIO.write(SwingFXUtils.fromFXImage(scene.snapshot(null), null), "png", new File("src/main/resources/Brochures/Assemblage/Etape " + (i+1)+".png"));
            } catch (IOException e) {
                System.out.println("error PNG");
            }
        }
        while(group.getChildren().size() > 1)
            group.getChildren().remove(1);
    }

    public static void creationBrochureAlgo(SelectionModel selection) {
        File assemblage = new File ("src/main/resources/Brochures/Assemblage");
        assemblage.mkdir();

        for(int i = 0; i < selection.listeCubeSelectionne.size(); i++){
            selection.group.getChildren().add(selection.listeCubeSelectionne.get(i));
            selection.listeCubeSelectionne.get(i).setDrawMode(DrawMode.FILL);
            File f = new File("src/main/resources/Brochures/Assemblage/" + "etape "+(i+1)+".png");
            try {//creer l'image
                ImageIO.write(SwingFXUtils.fromFXImage(selection.group.getScene().snapshot(null), null), "png", f);
                fichierPng.add(f);
            } catch (IOException e) {
                System.out.println("error PNG");
            }
        }
    }
}
