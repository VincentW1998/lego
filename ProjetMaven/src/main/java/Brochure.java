import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.DrawMode;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

public class Brochure {

    static LinkedList <Image> listeImages = new LinkedList<Image>();

    /* Creer une brochure à partir du decoupage manuel */
    public static void creationBrochure(Scene scene, Group group, SelectionModel selection) {
        LinkedList <Image> tmp  = new LinkedList<Image>();
        for(int i = 0;i<selection.PartiesSelection.size();i++){
            while(group.getChildren().size() > 1)//vide le groupe en laissant le sol
                group.getChildren().remove(1);

            tmp = selection.PartiesSelection.get(i).addPartiesToGroup(); // renvoie une liste d'image de l'assemblage de chaque partie

            // ajoute chaque image a la liste d'image
            listeImages.addAll(tmp);
        }
        while(group.getChildren().size() > 1)
            group.getChildren().remove(1);

        for (int i = 0; i < selection.PartiesSelection.size(); i++) {// crée les png de l'assemblage des parties
            selection.PartiesSelection.get(i).addPiecesToGroup(); // Pour chaque parties, on ajoute toutes les pieces de la parties dans le groupe
            try {//creer l'image
                File f = new File("src/main/resources/Brochures/etape.png");
                ImageIO.write(SwingFXUtils.fromFXImage(scene.snapshot(null), null), "png", f);
                listeImages.add(Image.getInstance(f.getPath()));
                f.delete();
            } catch (IOException | BadElementException e) {
                System.out.println("error PNG");
            }
        }
        while(group.getChildren().size() > 1)
            group.getChildren().remove(1);
        imagesToPdf();
    }


    // creer une brochure PDF à partir de l'alog
    public static void creationBrochureAlgo(SelectionModel selection) {
        // on parcourt la selection de cube
        for(int i = 0; i < selection.listeCubeSelectionne.size(); i++){
            // on ajoute chaque cube de la selection dans le groupe
            selection.group.getChildren().add(selection.listeCubeSelectionne.get(i));
            selection.listeCubeSelectionne.get(i).setDrawMode(DrawMode.FILL);
            // on initialise un fichier
            File f = new File("src/main/resources/Brochures/" + "etape.png");
            //creer un fichier png à partir d'un screenshot de la scene
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(selection.group.getScene().snapshot(null), null), "png", f);
                // on ajoute à la Linkedlist d'Image
                listeImages.add(Image.getInstance(f.getPath()));
                f.delete(); // supprimer le fichier png
            } catch (IOException | BadElementException e) {
                System.out.println("error PNG");
            }
        }
        imagesToPdf(); // On transforme la linkedlist de File en un document pdf
    }

    // fonction qui permet de creer un document pdf
    public static void imagesToPdf() {
        Document document = new Document();

//       String input = null;
        String output = "src/main/resources/Brochures/brochure.pdf"; // path de la brochure
        try {
            FileOutputStream fos = new FileOutputStream(output);
            PdfWriter writer = PdfWriter.getInstance(document, fos);
            writer.open();
            document.open();
            // on parcout la liste de file, on creer une image et on l'ajoute au document pdf
            for(int i = 0; i < listeImages.size(); i++) {
                // redimensionne l'image en fonction du document
                float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                        - document.rightMargin()) / listeImages.get(i).getWidth()) * 100;
                listeImages.get(i).scalePercent(scaler);
                document.add(Image.getInstance(listeImages.get(i)));
            }
            document.close();
            writer.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
