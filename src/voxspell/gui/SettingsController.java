package voxspell.gui;

import voxspell.user.User;
import voxspell.spell.Topic;
import voxspell.festival.Festival;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

import javafx.scene.layout.AnchorPane;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SettingsController implements Initializable {
    @FXML
    private AnchorPane pneRoot;

    @FXML
    private Label lblTitle;

    @FXML
    private Button btnMenu;

    @FXML
    private ComboBox<String> cmbFestival;

    @FXML
    private Spinner<Integer> sprWordsPerQuiz;

    @FXML
    private Button btnClearStats;

    @FXML
    private Button btnDeleteUser;

    @FXML
    private Button btnChangeWordList;

    @FXML
    void btnChangeWordListPressed(ActionEvent event) {
        //Used to save here.
        SceneSwitcher.getInstance().showDialog(SceneType.TOPIC_MENU);
    }

    @FXML
    void btnDeleteUserPressed(ActionEvent event) {
        //TODO: Show a confirmation dialog of some sort maybe?
        User.deleteCurrentUser();
        SceneSwitcher.getInstance().execute(SceneType.TITLE_MENU);
    }

    @FXML
    void btnClearStatsPressed(ActionEvent event) {
        //TODO: Show a confirmation dialog of some sort maybe?
        User.getInstance().getSelectedTopicSet().clear();
        User.getInstance().setTopicLevel(0);
    }

    @FXML
    void cmbFestivalSelectionChanged(ActionEvent event) {
        User.getInstance().setFestivalVoice(cmbFestival.getValue());
    }

    @FXML
    void btnMenuPressed(ActionEvent event) {
        SceneSwitcher.getInstance().execute(SceneType.MENU);
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        cmbFestivalSetUp();
        sprWordsPerQuizSetUp();

        // http://code.makery.ch/blog/javafx-2-event-handlers-and-change-listeners/
        sprWordsPerQuiz.valueProperty().addListener(new ChangeListener<Integer>() {
            public void changed(ObservableValue<? extends Integer> o, Integer oldV, Integer newV) {
                User.getInstance().setWordsPerQuiz(sprWordsPerQuiz.getValue());
            }
        });
    }

    private void cmbFestivalSetUp() {
        ObservableList<String> voices = FXCollections.observableArrayList();

        for (String v : Festival.getInstance().getVoices().keySet()) {
            voices.add(v);
        }

        cmbFestival.setItems(voices);
        cmbFestival.getSelectionModel().select(User.getInstance().getFestivalVoice());
    }

    private void sprWordsPerQuizSetUp() {
        sprWordsPerQuiz.getValueFactory().setValue(User.getInstance().getWordsPerQuiz());
    }

}
