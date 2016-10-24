package voxspell.music;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

/**
 * Controls the meny background music.
 * @author Kevin Hira.
 */
public class Music {
    private static Music _musicInstance;

    private MediaPlayer mediaPlayer;
    private boolean _userMuted;
    private Music() {
        Media media = new Media(new File("resources/Mars_Landscape_Sun_Set.mp3").toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        _userMuted = false;
    }

    public static Music getInstance() {
        if (_musicInstance == null) {
            _musicInstance = new Music();
        }

        return _musicInstance;
    }

    /**
     * Play the music.
     */
    public void play() {
        if (!_userMuted) {
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.play();
        }
    }

    /**
     * Pause the music.
     */
    public void pause() {
        mediaPlayer.pause();
    }

    /**
     * Stop the music.
     */
    public void stop() {
        mediaPlayer.stop();
    }

    /**
     * Set mute on music.
     * @param muted the value to set.
     */
    public void setMute(boolean muted) {
        _userMuted = muted;
    }

    /**
     * Gets whether the music is muted.
     * @return whether the music is muted.
     */
    public boolean isMuted() {
        return _userMuted;
    }

}
