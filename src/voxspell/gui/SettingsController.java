package voxspell.gui;

import voxspell.user.User;
import voxspell.spell.Topic;
import voxspell.festival.Festival;
import voxspell.music.Music;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import javafx.scene.layout.AnchorPane;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Controls the Settings Menu.
 * @author Kevin Hira.
 */
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
    private CheckBox cboMute;

    /**
     * Handles the check change of cboMute.
     */
    @FXML
    void cboMuteCheckedChanged(ActionEvent event) {
        // Update Music instance's state.
        Music.getInstance().setMute(cboMute.isSelected());
        if (cboMute.isSelected()) {
            Music.getInstance().stop();
        }
        else {
            Music.getInstance().play();
        }
    }

    /**
     * Handles the press of btnChangeWordList.
     */
    @FXML
    void btnChangeWordListPressed(ActionEvent event) {
        // Show the word list select menu.
        SceneSwitcher.getInstance().showDialog(SceneType.TOPIC_MENU);
    }

    /**
     * Handles the press of btnDeleteUser.
     */
    @FXML
    void btnDeleteUserPressed(ActionEvent event) {
        // Show a confirmation dialog.
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Delete User");
        confirmationDialog.setContentText("Are you sure you want to delete this user (" + User.getInstance().getName() + ")? You can not undo this.");

        if (confirmationDialog.showAndWait().get() == ButtonType.OK) {
            // Delete current user, return to title menu.
            User.deleteCurrentUser();
            Music.getInstance().stop();
            SceneSwitcher.getInstance().execute(SceneType.TITLE_MENU);
        }
    }

    @FXML
    void btnClearStatsPressed(ActionEvent event) {
        // Show a confirmation dialog of some sort maybe?
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Clear Statistics");
        confirmationDialog.setContentText("Are you sure you want to clear the statistics for the current word list? You can not undo this.");

        if (confirmationDialog.showAndWait().get() == ButtonType.OK) {
            // Clear User instance's fields.
            User.getInstance().getSelectedTopicSet().clear();
            User.getInstance().setTopicLevel(0);
            User.getInstance().getQuizzes().clear();
        }
    }
    /**
     * Handles the selection change of cmbFestival.
     */
    @FXML
    void cmbFestivalSelectionChanged(ActionEvent event) {
        // Update User state.
        User.getInstance().setFestivalVoice(cmbFestival.getValue());
    }

    /**
     * Handles the press of btnMenu.
     */
    @FXML
    void btnMenuPressed(ActionEvent event) {
        // Return to the main menu.
        SceneSwitcher.getInstance().execute(SceneType.MENU);
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        // Set up the combobox and spinner.
        cmbFestivalSetUp();
        sprWordsPerQuizSetUp();

        // Add change listener to spinner.
        sprWordsPerQuiz.valueProperty().addListener(new ChangeListener<Integer>() {
            public void changed(ObservableValue<? extends Integer> o, Integer oldV, Integer newV) {
                User.getInstance().setWordsPerQuiz(sprWordsPerQuiz.getValue());
            }
        });

        // Get state from Music model.
        cboMute.setSelected(Music.getInstance().isMuted());
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

    /**
     * Set up sprWordsPerQuiz
     */
    private void sprWordsPerQuizSetUp() {
        // Get value from User instance.
        sprWordsPerQuiz.getValueFactory().setValue(User.getInstance().getWordsPerQuiz());
    }

}
