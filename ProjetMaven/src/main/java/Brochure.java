import com.itextpdf.text.*;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
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
    static LinkedList <Integer> listeIdentifiant = new LinkedList<Integer>();
    static LinkedList <LinkedList <Image>> listeImagesPartie = new LinkedList<LinkedList<Image>>();
    static LinkedList <LinkedList <Integer>> listeIdentifiantPartie = new LinkedList<LinkedList<Integer>>();
    static LinkedList <Image> listeImagesAssemblage = new LinkedList<Image>();

    /* Creer une brochure à partir du decoupage manuel */
    public static void creationBrochure(Scene scene, Group group, SelectionModel selection) {
        for(int i = 0;i<selection.PartiesSelection.size();i++){
            while(group.getChildren().size() > 1)//vide le groupe en laissant le sol
                group.getChildren().remove(1);

            listeIdentifiantPartie.add(selection.PartiesSelection.get(i).getIdParties());
            listeImagesPartie.add(selection.PartiesSelection.get(i).addPartiesToGroup());
        }
        while(group.getChildren().size() > 1)
            group.getChildren().remove(1);

        for (int i = 0; i < selection.PartiesSelection.size(); i++) {// crée les png de l'assemblage des parties
            selection.PartiesSelection.get(i).addPiecesToGroup(); // Pour chaque parties, on ajoute toutes les pieces de la parties dans le groupe
            try {//creer l'image
                File f = new File("src/main/resources/Brochures/etape.png");
                ImageIO.write(SwingFXUtils.fromFXImage(scene.snapshot(null), null), "png", f);
                listeImagesAssemblage.add(Image.getInstance(f.getPath()));
                f.delete();
            } catch (IOException | BadElementException e) {
                System.out.println("error PNG");
            }
        }
        while(group.getChildren().size() > 1)
            group.getChildren().remove(1);
        imagesToPdfManuel();
    }


    // creer une brochure PDF à partir de l'alog
    public static void creationBrochureAlgo(SelectionModel selection) {
        // on parcourt la selection de cube
        for(int i = 0; i < selection.listeCubeSelectionne.size(); i++){
            // on ajoute chaque cube de la selection dans le groupe
            selection.group.getChildren().add(selection.listeCubeSelectionne.get(i));
            selection.listeCubeSelectionne.get(i).setDrawMode(DrawMode.FILL);
            // on initialise un fichier
            File f = new File("src/main/resources/Brochures/piece.png");
            listeIdentifiant.add(selection.listeCubeSelectionne.get(i).getIdentifiant());
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
        imagesToPdfAlgo(); // On transforme la linkedlist de File en un document pdf
    }

    // fonction qui permet de creer un document pdf
    public static void imagesToPdfAlgo() {
        Document document = new Document(PageSize.A4);

        String output = "src/main/resources/Brochures/brochure.pdf"; // path de la brochure
        try {
            FileOutputStream fos = new FileOutputStream(output);
            PdfWriter writer = PdfWriter.getInstance(document, fos);
            writer.open();
            writer.setStrictImageSequence(true);
            document.open();
            Paragraph p = new Paragraph("Brochure Lego\n\n");
            p.setAlignment(1); // centre le titre
            document.add(p);
            Paragraph saut = new Paragraph("\n\n");
            Paragraph presentation = new Paragraph("Veuillez suivre les instructions pour obtenir cette construction :\n");
            presentation.setAlignment(1);
            document.add(presentation);
            int x = listeImages.size() - 1;
            float sca = ((document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin()) / listeImages.get(x).getWidth()) * 100;
            listeImages.get(x).scalePercent(sca);
            listeImages.get(x).setBorder(Rectangle.BOX); // creer la bordure
            listeImages.get(x).setBorderWidth(5); // epaisseur de la bordure
            document.add(Image.getInstance(listeImages.get(x)));
            Paragraph sautenorme = new Paragraph("\n\n\n\n\n\n\n\n\n\n");
            document.add(sautenorme);
            // on parcout la liste de file, on creer une image et on l'ajoute au document pdf
            for(int i = 0; i < listeImages.size(); i++) {
                // redimensionne l'image en fonction du document
                float scaler = ((document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin()) / listeImages.get(i).getWidth()) * 100;
                listeImages.get(i).scalePercent(scaler);
                listeImages.get(i).setBorder(Rectangle.BOX); // creer la bordure
                listeImages.get(i).setBorderWidth(5); // epaisseur de la bordure
                document.add(Image.getInstance(listeImages.get(i)));
                document.add(new Paragraph("Etape " + (i + 1) + " : placez la pièce " + listeIdentifiant.get(i)));
                document.add(saut);
            }
            document.close();
            writer.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void imagesToPdfManuel() {
        Document document = new Document(PageSize.A4);

        String output = "src/main/resources/Brochures/brochure.pdf"; // path de la brochure
        try {
            FileOutputStream fos = new FileOutputStream(output);
            PdfWriter writer = PdfWriter.getInstance(document, fos);
            writer.open();
            writer.setStrictImageSequence(true);
            document.open();
            Paragraph p = new Paragraph("Brochure Lego\n\n");
            p.setAlignment(1); // centre le titre
            document.add(p);
            Paragraph saut = new Paragraph("\n\n");
            Paragraph presentation = new Paragraph("Veuillez suivre les instructions pour obtenir cette construction :\n");
            presentation.setAlignment(1);
            document.add(presentation);
            int x = listeImagesAssemblage.size() - 1;
            float sca = ((document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin()) / listeImagesAssemblage.get(x).getWidth()) * 100;
            listeImagesAssemblage.get(x).scalePercent(sca);
            listeImagesAssemblage.get(x).setBorder(Rectangle.BOX); // creer la bordure
            listeImagesAssemblage.get(x).setBorderWidth(5); // epaisseur de la bordure
            document.add(Image.getInstance(listeImagesAssemblage.get(x)));
            Paragraph sautenorme = new Paragraph("\n\n\n\n\n\n\n\n\n\n");
            document.add(sautenorme);


            for (int i = 0; i < listeImagesPartie.size(); i ++) {
                for (int j = 0; j < listeImagesPartie.get(i).size(); j ++) {
                    float scaler = ((document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin()) / listeImagesPartie.get(i).get(j).getWidth()) * 100;
                    listeImagesPartie.get(i).get(j).scalePercent(scaler);
                    listeImagesPartie.get(i).get(j).setBorder(Rectangle.BOX); // creer la bordure
                    listeImagesPartie.get(i).get(j).setBorderWidth(5); // epaisseur de la bordure
                    document.add(Image.getInstance(listeImagesPartie.get(i).get(j)));
                    document.add(new Paragraph("Partie " + (i + 1)+" Etape " + (j + 1) + " : placez la pièce " + listeIdentifiantPartie.get(i).get(j)));
                    document.add(saut);
                }
            }

            for (int i = 0; i < listeImagesAssemblage.size(); i ++) {
                float scaler = ((document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin()) / listeImagesAssemblage.get(i).getWidth()) * 100;
                listeImagesAssemblage.get(i).scalePercent(scaler);
                listeImagesAssemblage.get(i).setBorder(Rectangle.BOX); // creer la bordure
                listeImagesAssemblage.get(i).setBorderWidth(5); // epaisseur de la bordure
                document.add(Image.getInstance(listeImagesAssemblage.get(i)));
                document.add(new Paragraph("Assemblage" + (i + 1)+" Etape " + (i + 1)));
                document.add(saut);
            }
            document.close();
            writer.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
