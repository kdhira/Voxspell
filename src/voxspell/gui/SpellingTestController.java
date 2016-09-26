package voxspell.gui;
import voxspell.SceneSwitcher;
import voxspell.SceneType;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.TextFlow;
import javafx.scene.text.Text;



public class SpellingTestController implements Initializable {

    @FXML
    private Button btnMenu;

    @FXML
    private Label lblTitle;

    @FXML
    private Label lblLevel;

    @FXML
    private Button btnSubmit;

    @FXML
    private Button btnReplay;

    @FXML
    private TextFlow tflResults;

    @FXML
    private ProgressIndicator pgiWheel;

    @FXML
    private ProgressBar pgbProgress;

    @FXML
    private TextField txtResponse;

    private int _currentWord = 0;

    @FXML
    void btnSubmitPressed(ActionEvent event) {
        if (!txtResponse.getText().equals("") && _currentWord < 10) {
            ++_currentWord;
            pgiWheel.setProgress(_currentWord / 10.0);
            pgbProgress.setProgress(_currentWord / 10.0);

            Text t = new Text(_currentWord + " " + txtResponse.getText() + "\n");
            tflResults.getChildren().add(t);
        }
    }

    @FXML
    void btnMenuPressed(ActionEvent event) {
        SceneSwitcher.getInstance().execute(SceneType.MENU);
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {

    }
}
