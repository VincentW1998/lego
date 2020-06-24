import com.itextpdf.text.*;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
    public static void creationBrochure(Model model) {
        clearAll();
        for(int i = 0;i<model.selection.PartiesSelection.size();i++){
            while(model.group.getChildren().size() > 1)//vide le groupe en laissant le sol
                model.group.getChildren().remove(1);

            listeIdentifiantPartie.add(model.selection.PartiesSelection.get(i).getIdParties());
            listeImagesPartie.add(model.selection.PartiesSelection.get(i).addPartiesToGroup());
        }
        while(model.group.getChildren().size() > 1)
            model.group.getChildren().remove(1);

        for (int i = 0; i < model.selection.PartiesSelection.size(); i++) {// crée les png de l'assemblage des parties
            model.selection.PartiesSelection.get(i).addPiecesToGroup(); // Pour chaque parties, on ajoute toutes les pieces de la parties dans le groupe
            try {//creer l'image
                File f = new File("src/main/resources/Brochures/etape.png");
                ImageIO.write(SwingFXUtils.fromFXImage(model.scene.snapshot(null), null), "png", f);
                listeImagesAssemblage.add(Image.getInstance(f.getPath()));
                f.delete();
            } catch (IOException | BadElementException e) {
                System.out.println("error PNG");
            }
        }
        while(model.group.getChildren().size() > 1)
            model.group.getChildren().remove(1);
        imagesToPdfManuel(model);
    }


    // creer une brochure PDF à partir de l'alog
    public static void creationBrochureAlgo(SelectionModel selection, Model model) {
        clearAll();
        for(int i = 0; i < selection.listeCubeSelectionne.size(); i++){  // on parcourt la selection de cube
            selection.group.getChildren().add(selection.listeCubeSelectionne.get(i)); // on ajoute chaque cube de la selection dans le groupe
            Cube.moveToLoc((Cube) selection.group.getChildren().get(i + 1));
            selection.group.getChildren().get(i+1).addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                if (!e.isShiftDown())
                    model.selection.clear();
                model.selection.add((Cube) e.getSource());
            });
            File f = new File("src/main/resources/Brochures/piece.png"); // on initialise un fichier
            listeIdentifiant.add(selection.listeCubeSelectionne.get(i).getIdentifiant());
            try { //creer un fichier png à partir d'un screenshot de la scene
                ImageIO.write(SwingFXUtils.fromFXImage(selection.group.getScene().snapshot(null), null), "png", f);
                listeImages.add(Image.getInstance(f.getPath())); // on ajoute à la Linkedlist d'Image
                f.delete(); // supprimer le fichier png
            } catch (IOException | BadElementException e) {
                System.out.println("error PNG");
            }
        }
        imagesToPdfAlgo(model); // On transforme la linkedlist de File en un document pdf
    }

    // fonction qui permet de creer un document pdf
    public static void imagesToPdfAlgo(Model model) {
        Document document = new Document(PageSize.A4);

        String output = configurePdfFile(model.primaryStage);
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
            model.CurrentBrochure = new File(output);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void imagesToPdfManuel(Model model) {
        Document document = new Document(PageSize.A4);

//        String output = "src/main/resources/Brochures/brochure.pdf"; // path de la brochure
        String output = configurePdfFile(model.primaryStage);
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
            model.CurrentBrochure = new File(output);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clearAll(){
        listeImages.clear();
        listeIdentifiant.clear();
        listeImagesPartie.clear();
        listeIdentifiantPartie.clear();
        listeImagesAssemblage.clear();
    }

    public static String configurePdfFile(Stage primaryStage){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName("brochure");
        fileChooser.setTitle("brochure");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("fichier pdf", "*.pdf"));
        fileChooser.setInitialDirectory(new File("src/main/resources/Brochures/"));
        File file = fileChooser.showSaveDialog(primaryStage);
        return file.getPath();
    }
}
