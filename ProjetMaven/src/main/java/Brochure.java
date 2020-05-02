import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.DrawMode;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

public class Brochure {

    static LinkedList <File> fichierPng = new LinkedList<File>(); // Liste de file

    /* Creer une brochure à partir du decoupage manuel */

    public static void creationBrochure(Scene scene, Group group, SelectionModel selection) {
    LinkedList<LinkedList<File>> Parties = new LinkedList<LinkedList<File>>();
    LinkedList<File> Assemblage = new LinkedList<File>();
        for(int i = 0;i<selection.PartiesSelection.size();i++){
           { File part = new File("src/main/resources/Brochures/Partie"+(i+1));
                part.mkdir();
           }
            while(group.getChildren().size() > 1)//vide le groupe en laissant le sol
                group.getChildren().remove(1);
            Parties.add(selection.PartiesSelection.get(i).addPartiesToGroup(i+1));
        }
        while(group.getChildren().size() > 1)
            group.getChildren().remove(1);

        for (int i = 0; i < selection.PartiesSelection.size(); i++) {// crée les png de l'assemblage des parties
            selection.PartiesSelection.get(i).addPiecesToGroup(); // Pour chaque parties, on ajoute toutes les pieces de la parties dans le groupe
            try {//creer l'image
                Assemblage.add(new File("src/main/resources/Brochures/Etape " + (i+1)+".png"));
                ImageIO.write(SwingFXUtils.fromFXImage(scene.snapshot(null), null), "png", Assemblage.getLast());
            } catch (IOException e) {
                System.out.println("error PNG");
            }
        }
        while(group.getChildren().size() > 1)
            group.getChildren().remove(1);
    }


    // creer une brochure PDF à partir de l'alog
    public static void creationBrochureAlgo(SelectionModel selection) {
        // On creer le dossier assemblage
        File assemblage = new File ("src/main/resources/Brochures/Assemblage");
        assemblage.mkdir();

        // on parcourt la selection de cube
        for(int i = 0; i < selection.listeCubeSelectionne.size(); i++){
            // on ajoute chaque cube de la selection dans le groupe
            selection.group.getChildren().add(selection.listeCubeSelectionne.get(i));
            selection.listeCubeSelectionne.get(i).setDrawMode(DrawMode.FILL);
            // on initialise un fichier
            File f = new File("src/main/resources/Brochures/Assemblage/" + "etape "+(i+1)+".png");
            try {//creer un fichier png à partir d'un screenshot de la scene
                ImageIO.write(SwingFXUtils.fromFXImage(selection.group.getScene().snapshot(null), null), "png", f);
                fichierPng.add(f); // on ajoute à la linkedlist de File
            } catch (IOException e) {
                System.out.println("error PNG");
            }
        }
        pngToPdf(); // On transforme la linkedlist de File en un document pdf
    }

    // fonction qui permet de creer un document pdf
    public static void pngToPdf() {
        Document document = new Document();

       String input = null;
        String output = "src/main/resources/Brochures/brochure.pdf"; // path de la brochure
        try {
            FileOutputStream fos = new FileOutputStream(output);
            PdfWriter writer = PdfWriter.getInstance(document, fos);
            writer.open();
            document.open();
            // on parcout la liste de file, on creer une image et on l'ajoute au document pdf
            for(int i = 0; i < fichierPng.size(); i++) {
                input = fichierPng.get(i).getPath();
                Image image = Image.getInstance(input);
                // redimensionne l'image en fonction du document
                float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                        - document.rightMargin()) / image.getWidth()) * 100;
                image.scalePercent(scaler);
                document.add(Image.getInstance(image));
            }
            document.close();
            writer.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
