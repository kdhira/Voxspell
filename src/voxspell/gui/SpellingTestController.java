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

/**
 * Controls the Spelling Quiz Interface.
 * @author Kevin Hira.
 */
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


    private Text currentOut;

    /**
     * Handles the press of btnReplay.
     */
    @FXML
    void btnReplayPressed(ActionEvent event) {
        // Respeak word, return focus to textfield.
        speakWord(1, true);
        txtResponse.requestFocus();
    }

    /**
     * Handles the press of btnSubmit.
     */
    @FXML
    void btnSubmitPressed(ActionEvent event) {
        if (!txtResponse.getText().equals("")) {
            // Query the quiz and update elements.
            queryQuiz();
            pgiWheel.setProgress(_quiz.getCompletion());
            pgbProgress.setProgress(_quiz.getAccuracy());
            txtResponse.setText("");
        }

        txtResponse.requestFocus();
    }

    /**
     * Handles the press of btnMenu.
     */
    @FXML
    void btnMenuPressed(ActionEvent event) {
        // Close festival.
        Festival.getInstance().closeFestival();

        // Depending on the text of the button, got to the main menu or results menu.
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

    /**
     * Handles the selection change of cmbFestival.
     */
    @FXML
    void cmbFestivalSelectionChanged(ActionEvent event) {
        // Change voice.
        Festival.getInstance().changeVoice(cmbFestival.getValue());
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        // Set up festival and festival changer.
        _numberWords = User.getInstance().getWordsPerQuiz();
        Festival.getInstance().openFestival();
        cmbFestivalSetUp();

        // Added change listener to textfield, only allow certain characters to be entered.
        txtResponse.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> o, String oldV, String newV) {
                if (!newV.equals("") && !newV.matches("([A-z][a-z']*)( ([A-z][a-z']*)?)*")) {
                    txtResponse.setText(oldV);
                }
            }
        });

        // Add change listener to textflow, scroll to the top.
        tflResults.getChildren().addListener(new ListChangeListener<Node>() {
            public void onChanged(ListChangeListener.Change<? extends Node> c) {
                scrollToTop();
            }
        });

        // Build quiz object from words in topic.
        Topic targetTopic = User.getInstance().targetTopic();
        _quiz = new Quiz(targetTopic, _numberWords, false);
        _results = _quiz.getResults();
        _numberWords = _quiz.numberWords();

        lblLevel.setText(targetTopic.getName());

        // Say first word.
        promptWord();
    }

    /**
     * Query the quiz object and handle the response.
     */
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

    /**
     * Update the text flow when the quiz moves to the next word.
     */
    private void outputResponse() {
        SpellResult r = _results.getLast();
        switch (r.getResult()) {
            case MASTERED:
                Festival.getInstance().speak("Correct!");
                currentOut.setText(_prompt + " Correct (" + r.getName() + ").\n");
                break;
            case FAULTED:
                Festival.getInstance().speak("Correct!");
                currentOut.setText(_prompt + " Faulted (" + r.getName() + ").\n");
                break;
            case FAILED:
                Festival.getInstance().speak("Incorrect...");
                currentOut.setText(_prompt + " Failed (" + r.getName() + ").\n");
                break;
        }
    }

    /**
     * Write out a prompt to spell a word.
     */
    private void promptWord() {
        tflResults.getChildren().add(0, currentOut = new Text());
        _prompt = "Spell word " + (_quiz.currentIndex()+1) + "/" + _numberWords + ":";
        currentOut.setText(_prompt + "\n");
        speakWord(1, false);
    }

    /**
     * Speak the current word.
     * @param quantity the amount of times to speak the word.
     * @param whether to say it slower or not.
     */
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

    /**
     * Scrolls to the top of the text flow.
     */
    private void scrollToTop() {
        spnFlowScroll.setVvalue(0.00f);
    }

    /**
     * Build data for cmbFestival.
     */
    private void cmbFestivalSetUp() {
        ObservableList<String> voices = FXCollections.observableArrayList();

        // Get voices from Festival instance.
        for (String v : Festival.getInstance().getVoices().keySet()) {
            voices.add(v);
        }

        cmbFestival.setItems(voices);
        cmbFestival.getSelectionModel().select(User.getInstance().getFestivalVoice());
    }
}
