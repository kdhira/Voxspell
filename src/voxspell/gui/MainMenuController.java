package voxspell.gui;

import voxspell.user.User;
import voxspell.spell.Topic;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.application.Platform;

import javafx.scene.control.Label;
import javafx.scene.control.Button;

public class MainMenuController implements Initializable {

    @FXML
    private Label lblAuthor;

    @FXML
    private Button btnNewQuiz;

    @FXML
    private Button btnReviewQuiz;

    @FXML
    private Button btnViewStatistics;

    @FXML
    private Label lblTitle;

    @FXML
    private Label lblCurrentUser;

    @FXML
    private Label lblCurrentTopic;

    @FXML
    private Button btnExit;

    @FXML
    private Button btnChangeUser;

    @FXML
    private Button btnChangeLevel;

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
    }

    private void setTextElements() {
        lblCurrentUser.setText("User: " + User.getInstance().getName());

        lblCurrentTopic.setText("Level/Topic: " + User.getInstance().targetTopic().getName() + " (" + (User.getInstance().getTopicLevel() + 1) + ")");
    }

}
