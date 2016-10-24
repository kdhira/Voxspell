package voxspell.music;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class Music {
    private static Music _musicInstance;

    private MediaPlayer mediaPlayer;
    private Music() {
        Media media = new Media(new File("resources/Mars_Landscape_Sun_Set.mp3").toURI().toString());
        mediaPlayer = new MediaPlayer(media);
    }

    public static Music getInstance() {
        if (_musicInstance == null) {
            _musicInstance = new Music();
        }

        return _musicInstance;
    }

    public void play() {
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public void stop() {
        mediaPlayer.stop();
    }

}
