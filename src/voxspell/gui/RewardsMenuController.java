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

    @FXML
    void btnPlayPressed(ActionEvent event) {
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

    @FXML
    void btnStopPressed(ActionEvent event) {
        mediaPlayer.stop();
        btnPlay.setText("Play");
    }

    @FXML
    void btnBackPressed(ActionEvent event) {
        ((Stage)btnBack.getScene().getWindow()).close();
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        SceneSwitcher.getInstance().getStage().setTitle("Video Reward - Voxspell");
        Media media = new Media(new File("resources/big_buck.mp4").toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mdaPlayer.setMediaPlayer(mediaPlayer);

    }
}
