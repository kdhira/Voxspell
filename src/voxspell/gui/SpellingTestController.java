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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ComboBox;
import javafx.scene.text.TextFlow;
import javafx.scene.text.Text;
import javafx.scene.Node;

import java.util.LinkedList;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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

    @FXML
    private ScrollPane spnFlowScroll;

    @FXML
    private ComboBox<String> cmbFestival;

    private int _numberWords;
    private String _prompt;
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
            pgiWheel.setProgress(_quiz.getCompletion());
            pgbProgress.setProgress(_quiz.getAccuracy());
            txtResponse.setText("");
        }

        txtResponse.requestFocus();
    }

    @FXML
    void btnMenuPressed(ActionEvent event) {
        Festival.getInstance().closeFestival();
        switch (btnMenu.getText()) {
            case "Back to Menu":
                SceneSwitcher.getInstance().execute(SceneType.MENU);
                break;
            case "View Results":
                SceneSwitcher.getInstance().execute(SceneType.RESULT_MENU);
                break;
            default:
                break;
        }
    }

    @FXML
    void cmbFestivalSelectionChanged(ActionEvent event) {
        Festival.getInstance().changeVoice(cmbFestival.getValue());
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        _nCorrect = 0;
        _numberWords = User.getInstance().getWordsPerQuiz();

        Festival.getInstance().openFestival();
        cmbFestivalSetUp();

        // http://code.makery.ch/blog/javafx-2-event-handlers-and-change-listeners/
        txtResponse.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> o, String oldV, String newV) {
                if (!newV.equals("") && !newV.matches("([A-z][a-z']*)( ([A-z][a-z']*)?)*")) {
                    txtResponse.setText(oldV);
                }
            }
        });

        tflResults.getChildren().addListener(new ListChangeListener<Node>() {
            public void onChanged(ListChangeListener.Change<? extends Node> c) {
                scrollToTop();
            }
        });

        Topic targetTopic = User.getInstance().targetTopic();
        _quiz = new Quiz(targetTopic, _numberWords, false);
        _results = _quiz.getResults();
        _numberWords = _quiz.numberWords();

        lblLevel.setText(targetTopic.getName());

        // Say first word.
        promptWord();
    }

    private void queryQuiz() {
        switch (_quiz.doWork(txtResponse.getText())) {
            case NO_CHANGE:
                Festival.getInstance().speak("Try once more...");
                speakWord(1, false);
                speakWord(1, true);
                break;
            case QUIZ_DONE:
                outputResponse();
                _quiz.logStatistics();
                User.getInstance().getQuizzes().add(_quiz);
                txtResponse.setDisable(true);
                btnSubmit.setDisable(true);
                btnReplay.setDisable(true);
                cmbFestival.setDisable(true);
                btnMenu.setText("View Results");
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
                currentOut.setText(_prompt + " Correct (" + r.getName() + ").\n");
                ++_nCorrect;
                break;
            case FAULTED:
                Festival.getInstance().speak("Correct!");
                currentOut.setText(_prompt + " Faulted (" + r.getName() + ").\n");
                ++_nCorrect;
                break;
            case FAILED:
                Festival.getInstance().speak("Incorrect...");
                currentOut.setText(_prompt + " Failed (" + r.getName() + ").\n");
                break;
        }
    }

    private void promptWord() {
        tflResults.getChildren().add(0, currentOut = new Text());
        _prompt = "Spell word " + (_quiz.currentIndex()+1) + "/" + _numberWords + ":";
        currentOut.setText(_prompt + "\n");
        speakWord(1, false);
    }

    private void speakWord(int quantity, boolean goSlower) {
        boolean shortWord = _quiz.currentWord().length() <= 4;
        if (goSlower) {
            Festival.getInstance().changeStretch(shortWord ? "2.5" : "2.0");
        }
        else if (shortWord) {
            Festival.getInstance().changeStretch("1.25");
        }

        for (int i = 0; i < quantity; ++i) {
            Festival.getInstance().speak( _quiz.currentWord());
        }

        if (goSlower || shortWord) {
            Festival.getInstance().changeStretch("1.0");
        }
    }

    private void scrollToTop() {
        spnFlowScroll.setVvalue(0.00f);
    }

    private void cmbFestivalSetUp() {
        ObservableList<String> voices = FXCollections.observableArrayList();

        for (String v : Festival.getInstance().getVoices().keySet()) {
            voices.add(v);
        }

        cmbFestival.setItems(voices);
        cmbFestival.getSelectionModel().select(User.getInstance().getFestivalVoice());
    }
}
