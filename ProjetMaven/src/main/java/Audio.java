import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

/* Classe qui contient 2 fonctions :
* Une fonction qui gere le son lors d'un deplacement et
* la seconde gere le son pour la suppression */

public class Audio {

    public static void soundMove() { // son lors d'un mouvement de piece
        String bip = "./src/main/resources/move.wav"; // fichier
        Media hit = new Media(new File(bip).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(hit);
        mediaPlayer.play();
    }



    public static void soundDelete() { // son lors d'une suppresion de piece
        String bip = "./src/main/resources/remove.wav";
        Media hit = new Media(new File(bip).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(hit);
        mediaPlayer.play();
    }


}
