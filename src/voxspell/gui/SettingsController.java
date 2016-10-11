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

        // sprWordsPerQuiz = new Spinner();
        // sprWordsPerQuiz.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(3, 30, User.getInstance().getWordsPerQuiz()));
        // sprWordsPerQuiz.relocate(200, 200);
        // pneRoot.getChildren().add(sprWordsPerQuiz);
        // sprWordsPerQuiz.getValueFactory().setValue();
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
