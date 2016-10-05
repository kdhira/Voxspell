package voxspell.gui;

import voxspell.spell.Quiz;
import voxspell.spell.QuizFlag;
import voxspell.spell.Topic;
import voxspell.spell.TopicSet;
import voxspell.spell.SpellResult;
import voxspell.user.User;
import voxspell.festival.Festival;

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

    private int _nCorrect;

    private Text currentOut;

    @FXML
    void btnReplayPressed(ActionEvent event) {
        speakWord(1, true);
        txtResponse.requestFocus();
    }

    @FXML
    void btnSubmitPressed(ActionEvent event) {
        if (!txtResponse.getText().equals("")) {
            queryQuiz();
            pgiWheel.setProgress(_nCorrect / (double)_numberWords);
            pgbProgress.setProgress(_nCorrect / (double)_numberWords);
            txtResponse.setText("");
        }

        txtResponse.requestFocus();
    }

    @FXML
    void btnMenuPressed(ActionEvent event) {
        Festival.getInstance().closeFestival();
        SceneSwitcher.getInstance().execute(SceneType.MENU);
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        _nCorrect = 0;
        _numberWords = 10;

        Festival.getInstance().openFestival();
        Festival.getInstance().changeVoice();

        // http://code.makery.ch/blog/javafx-2-event-handlers-and-change-listeners/
        txtResponse.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> o, String oldV, String newV) {
                if (!newV.equals("") && !newV.matches("([A-z][a-z']*)( ([A-z][a-z']*)?)*")) {
                    txtResponse.setText(oldV);
                }
            }
        });

        Topic targetTopic = User.getInstance().targetTopic();
        _quiz = new Quiz(targetTopic, _numberWords, false);
        _results = _quiz.getResults();

        lblLevel.setText(targetTopic.getName());

        // Say first word. (Could refactor out to startTest() or something.)
        promptWord();

        // Platform.runLater(() -> txtResponse.requestFocus());


    }

    private void queryQuiz() {
        switch (_quiz.doWork(txtResponse.getText())) {
            case NO_CHANGE:
                Festival.getInstance().speak("Try once more...");
                speakWord(2, true);
                break;
            case QUIZ_DONE:
                outputResponse();
                _quiz.logStatistics();
                txtResponse.setDisable(true);
                btnSubmit.setDisable(true);
                btnReplay.setDisable(true);
                break;
            case NEW_WORD:
                outputResponse();
                promptWord();
                break;

        }
    }

    private void outputResponse() {
        SpellResult r = _results.getLast();
        switch (r.getResult()) {
            case MASTERED:
                Festival.getInstance().speak("Correct!");
                currentOut.setText(r.getName() + " - Correct.\n");
                ++_nCorrect;
                break;
            case FAULTED:
                Festival.getInstance().speak("Correct!");
                currentOut.setText(r.getName() + " - Faulted.\n");
                ++_nCorrect;
                break;
            case FAILED:
                Festival.getInstance().speak("Incorrect...");
                currentOut.setText(r.getName() + " - Failed.\n");
                break;
        }
    }

    private void promptWord() {
        tflResults.getChildren().add(currentOut = new Text());
        currentOut.setText("Spell: " + _quiz.currentWord() + "\n");
        speakWord(1, false);
    }

    private void speakWord(int quantity, boolean goSlower) {
        if (goSlower) {
            // TODO: Slow down festival
            Festival.getInstance().changeStretch("2.0");
        }

        for (int i = 0; i < quantity; ++i) {
            Festival.getInstance().speak( _quiz.currentWord());
        }

        if (goSlower) {
            // TODO: Speed up festival to default.
            Festival.getInstance().changeStretch("1.0");
        }
    }
}
