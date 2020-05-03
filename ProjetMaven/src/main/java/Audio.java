import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class Audio {


    public static void playPOPMove() {
        String bip = "./src/main/resources/pop.mp3";
        Media hit = new Media(new File(bip).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(hit);
        mediaPlayer.play();
    }


}
