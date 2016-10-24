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

    @FXML
    void btnHelpPressed(ActionEvent event) {
        String command = "gnome-open";
        if (System.getProperty("os.name").contains("Mac")) {
            command = "open";
        }
        String helpPDF = "khir664_Voxspell_Manual.pdf";
        if (!new File(helpPDF).exists()) {
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

    @FXML
    void btnHistoryPressed(ActionEvent event) {
        SceneSwitcher.getInstance().execute(SceneType.HISTORY_MENU);
    }

    @FXML
    void cmbTopicsSelectionChanged(ActionEvent event) {
        User.getInstance().setTopicLevel(cmbTopics.getSelectionModel().getSelectedIndex());
    }

    @FXML
    void btnSettingsPressed(ActionEvent event) {
        SceneSwitcher.getInstance().execute(SceneType.SETTINGS);
    }

    @FXML
    void btnLogoutPressed(ActionEvent event) {
        Music.getInstance().stop();
        User.logout();
        SceneSwitcher.getInstance().execute(SceneType.TITLE_MENU);
    }

    @FXML
    void btnViewStatisticsPressed(ActionEvent event) {
        SceneSwitcher.getInstance().execute(SceneType.VIEW_STATISTICS);
    }

    @FXML
    void btnNewQuizPressed(ActionEvent event) {
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

    private void setTextElements() {
        lblCurrentUser.setText("User: " + User.getInstance().getName());
    }

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
