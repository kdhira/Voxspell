package voxspell.gui;

import voxspell.spell.Quiz;
import voxspell.spell.QuizFlag;
import voxspell.spell.Topic;
import voxspell.spell.TopicSet;
import voxspell.spell.SpellResult;
import voxspell.user.User;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.application.Platform;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.TextFlow;
import javafx.scene.text.Text;

import java.util.LinkedList;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

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
    private int _numberWords;
    private Quiz _quiz;
    private LinkedList<SpellResult> _results;

    private Text currentOut;

    @FXML
    void btnSubmitPressed(ActionEvent event) {
        queryQuiz();
        pgiWheel.setProgress(_quiz.currentIndex() / (double)_numberWords);
        pgbProgress.setProgress(_quiz.currentIndex() / (double)_numberWords);

        txtResponse.setText("");
        txtResponse.requestFocus();
    }

    @FXML
    void btnMenuPressed(ActionEvent event) {
        SceneSwitcher.getInstance().execute(SceneType.MENU);
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        _numberWords = 10;

        // http://code.makery.ch/blog/javafx-2-event-handlers-and-change-listeners/
        txtResponse.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> o, String oldV, String newV) {
                if (!newV.matches("[A-Za-z' ]*")) {
                    txtResponse.setText(oldV);
                }
            }
        });

        Topic targetTopic = User.getInstance().targetTopic();
        _quiz = new Quiz(targetTopic, _numberWords, false);
        _results = _quiz.getResults();

        lblLevel.setText(targetTopic.getName());

        System.out.println(_quiz.currentWord());
        tflResults.getChildren().add(currentOut = new Text());
        currentOut.setText("Spell: " + _quiz.currentWord() + "\n");

        Platform.runLater(() -> txtResponse.requestFocus());


    }

    private void queryQuiz() {
        switch (_quiz.doWork(txtResponse.getText())) {
            case NO_CHANGE:
                break;
            case QUIZ_DONE:
                outputResponse();
                _quiz.logStatistics();
                System.out.println("DONE!");
                break;
            case NEW_WORD:
                outputResponse();
                tflResults.getChildren().add(currentOut = new Text());
                currentOut.setText("Spell: " + _quiz.currentWord() + "\n");
                System.out.println(_quiz.currentWord());
                break;

        }
    }

    private void outputResponse() {
        SpellResult r = _results.getLast();
        switch (r.getResult()) {
            case MASTERED:
                currentOut.setText(r.getName() + " - Correct.\n");
                break;
            case FAULTED:
                currentOut.setText(r.getName() + " - Faulted.\n");
                break;
            case FAILED:
                currentOut.setText(r.getName() + " - Failed.\n");
                break;
        }
    }
}
