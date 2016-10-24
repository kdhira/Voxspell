package voxspell.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;

import javafx.stage.Stage;

/**
 * Controls the Rewards Menu.
 * @author Kevin Hira.
 */
public class RewardsMenuController implements Initializable {
    @FXML
    private Button btnPlay;

    @FXML
    private Button btnStop;

    @FXML
    private Button btnBack;

    @FXML
    private MediaView mdaPlayer;

    private MediaPlayer mediaPlayer;

    /**
     * Handles the press of btnPlay.
     */
    @FXML
    void btnPlayPressed(ActionEvent event) {
        // Play or pause the video depending on the current text of the button.
        switch (btnPlay.getText()) {
            case "Play":
                mediaPlayer.play();
                btnPlay.setText("Pause");
                break;
            case "Pause":
                mediaPlayer.pause();
                btnPlay.setText("Play");
                break;
            default:
                break;
        }
    }

    /**
     * Handles the press of btnStop.
     */
    @FXML
    void btnStopPressed(ActionEvent event) {
        // Stop the video, set the play button's text.
        mediaPlayer.stop();
        btnPlay.setText("Play");
    }

    /**
     * Handles the press of btnBack.
     */
    @FXML
    void btnBackPressed(ActionEvent event) {
        // Stop the video and close the dialog.
        mediaPlayer.stop();
        ((Stage)btnBack.getScene().getWindow()).close();
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        // Set tje dialog window's title.
        SceneSwitcher.getInstance().getStage().setTitle("Video Reward - Voxspell");
        
        // Retrieve the rewards video.
        Media media = new Media(new File("resources/big_buck.mp4").toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mdaPlayer.setMediaPlayer(mediaPlayer);

    }
}
