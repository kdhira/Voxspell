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

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SettingsController implements Initializable {
    @FXML
    private Label lblTitle;

    @FXML
    private Button btnMenu;

    @FXML
    private ComboBox<String> cmbFestival;

    @FXML
    private TextField txtWordsPerQuiz;

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
        txtWordsPerQuizSetUp();

        // http://code.makery.ch/blog/javafx-2-event-handlers-and-change-listeners/
        txtWordsPerQuiz.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> o, String oldV, String newV) {
                if (!newV.equals("") && !newV.matches("[0-9]*")) {
                    txtWordsPerQuiz.setText(oldV);
                }
                else if (!newV.equals("")) {
                    User.getInstance().setWordsPerQuiz(Integer.parseInt(txtWordsPerQuiz.getText()));
                }
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

    private void txtWordsPerQuizSetUp() {
        txtWordsPerQuiz.setText(User.getInstance().getWordsPerQuiz() + "");
    }

}
