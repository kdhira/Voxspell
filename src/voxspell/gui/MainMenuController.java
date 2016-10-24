package voxspell.gui;

import voxspell.user.User;
import voxspell.spell.Topic;
import voxspell.spell.TopicSet;
import voxspell.music.Music;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.application.Platform;

import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controls the Main Menu.
 * @author Kevin Hira.
 */
public class MainMenuController implements Initializable {

    @FXML
    private Label lblAuthor;

    @FXML
    private Button btnNewQuiz;

    @FXML
    private ComboBox<String> cmbTopics;

    @FXML
    private Button btnViewStatistics;

    @FXML
    private Label lblTitle;

    @FXML
    private Label lblCurrentUser;

    @FXML
    private Button btnLogout;

    @FXML
    private Button btnSettings;

    @FXML
    private Button btnHistory;

    @FXML
    private Button btnHelp;

    /**
     * Handles the press of btnHelp.
     */
    @FXML
    void btnHelpPressed(ActionEvent event) {
        // Build a process that will open the manual pdf in the defualt pdf viewer.
        String command = "gnome-open";
        if (System.getProperty("os.name").contains("Mac")) {
            command = "open";
        }
        String helpPDF = "khir664_Voxspell_Manual.pdf";
        if (!new File(helpPDF).exists()) {
            // Show a error if the file doesn't exist.
            Alert errorMessage = new Alert(Alert.AlertType.ERROR);
            errorMessage.setTitle("Help PDF missing.");
            errorMessage.setContentText("Could not find help PDF \"" + helpPDF + "\". Make sure it is in the working directory.");
            errorMessage.showAndWait();
        }
        ProcessBuilder pb = new ProcessBuilder("bash", "-c", command + " " + helpPDF);
        try {
            pb.start();
        } catch (IOException ex) {
            Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Handles the press of btnHistory.
     */
    @FXML
    void btnHistoryPressed(ActionEvent event) {
        // Switch to the history menu.
        SceneSwitcher.getInstance().execute(SceneType.HISTORY_MENU);
    }

    /**
     * Handles the selection change of cmbTopics.
     */
    @FXML
    void cmbTopicsSelectionChanged(ActionEvent event) {
        // Update desired topic for quizzes.
        User.getInstance().setTopicLevel(cmbTopics.getSelectionModel().getSelectedIndex());
    }

    /**
     * Handles the press of btnSettings.
     */
    @FXML
    void btnSettingsPressed(ActionEvent event) {
        // Switch to the settings menu.
        SceneSwitcher.getInstance().execute(SceneType.SETTINGS);
    }

    /**
     * Handles the press of btnLogout.
     */
    @FXML
    void btnLogoutPressed(ActionEvent event) {
        // Log out and switch to the title menu.
        Music.getInstance().stop();
        User.logout();
        SceneSwitcher.getInstance().execute(SceneType.TITLE_MENU);
    }

    /**
     * Handles the press of btnViewStatistics.
     */
    @FXML
    void btnViewStatisticsPressed(ActionEvent event) {
        // Switch to the statistics menu.
        SceneSwitcher.getInstance().execute(SceneType.VIEW_STATISTICS);
    }

    /**
     * Handles the press of btnNewQuiz.
     */
    @FXML
    void btnNewQuizPressed(ActionEvent event) {
        // Switch to the new quiz interface.
        Music.getInstance().stop();
        SceneSwitcher.getInstance().execute(SceneType.NEW_QUIZ);
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        setTextElements();
        loadTopics();
        Music.getInstance().play();

        if (User.getInstance().getQuizzes().isEmpty()) {
            btnHistory.setVisible(false);
        }
    }

    /**
     * Set the label elements' text values.
     */
    private void setTextElements() {
        lblCurrentUser.setText("User: " + User.getInstance().getName());
    }

    /**
     * Load in the topics from the User instance and set them to the combobox.
     */
    private void loadTopics() {
        TopicSet selectedTopicSet = User.getInstance().getSelectedTopicSet();
        ObservableList<String> topics = FXCollections.observableArrayList();
        for (int i = 0; i < selectedTopicSet.size(); ++i) {
            topics.add(selectedTopicSet.atPosition(i).getName());
        }

        cmbTopics.setItems(topics);
        cmbTopics.getSelectionModel().select(User.getInstance().getTopicLevel());
    }

}
