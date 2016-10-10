package voxspell.gui;

import voxspell.user.User;
import voxspell.spell.Topic;
import voxspell.spell.TopicSet;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.application.Platform;

import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
    private Button btnExit;

    @FXML
    private Button btnChangeUser;

    @FXML
    private Button btnChangeLevel;

    @FXML
    private Button btnSettings;

    @FXML
    void cmbTopicsSelectionChanged(ActionEvent event) {
        User.getInstance().setTopicLevel(cmbTopics.getSelectionModel().getSelectedIndex());
    }

    @FXML
    void btnSettingsPressed(ActionEvent event) {
        SceneSwitcher.getInstance().execute(SceneType.SETTINGS);
    }

    @FXML
    void btnChangeUserPressed(ActionEvent event) {
        User.getInstance().save();
        SceneSwitcher.getInstance().execute(SceneType.USER_MENU);
    }

    @FXML
    void btnChangeLevelPressed(ActionEvent event) {
        User.getInstance().save();
        SceneSwitcher.getInstance().execute(SceneType.TOPIC_MENU);
    }

    @FXML
    void btnViewStatisticsPressed(ActionEvent event) {
        SceneSwitcher.getInstance().execute(SceneType.VIEW_STATISTICS);
    }

    @FXML
    void btnNewQuizPressed(ActionEvent event) {
        SceneSwitcher.getInstance().execute(SceneType.NEW_QUIZ);
    }

    @FXML
    void btnExitPressed(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        setTextElements();
        loadTopics();
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
