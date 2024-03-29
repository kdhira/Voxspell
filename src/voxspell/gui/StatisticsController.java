package voxspell.gui;

import voxspell.user.User;
import voxspell.spell.Word;
import voxspell.spell.TopicSet;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.application.Platform;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Controls the Statistics Menu.
 * @author Kevin Hira.
 */
public class StatisticsController implements Initializable {
    @FXML
    private Label lblTitle;

    @FXML
    private Label lblUser;

    @FXML
    private Label lblLevel;

    @FXML
    private Button btnMenu;

    @FXML
    private TableView<Word> tblStatistics;

    @FXML
    private TableColumn<Word, String> tclName;

    @FXML
    private TableColumn<Word, Integer> tclMastered;

    @FXML
    private TableColumn<Word, Integer> tclFaulted;

    @FXML
    private TableColumn<Word, Integer> tclFailed;

    @FXML
    private ComboBox<String> cmbTopics;

    /**
     * Handles the selection change of cmbTopics.
     */
    @FXML
    void cmbTopicSelectionChanged(ActionEvent event) {
        // Reload the words for the new topic.
        loadWords();
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
        // Add the user to the label, then load topics into combobox.
        lblUser.setText("User: " + User.getInstance().getName());
        loadTopics();

        // Load the words for the chosen topic to start off.
        loadWords();
    }

    /**
     * Load in the words into the table for a topic.
     */
    private void loadWords() {
        int index = cmbTopics.getSelectionModel().getSelectedIndex();

        lblLevel.setText(User.getInstance().getSelectedTopicSet().atPosition(index).getName());

        ObservableList<Word> words = FXCollections.observableArrayList();
        for (Word w : User.getInstance().getSelectedTopicSet().atPosition(index).getWords()) {
            if (!w.isClear()) {
                words.add(w);
            }

        }
        tblStatistics.setItems(words);
        tclName.setCellValueFactory(new PropertyValueFactory<Word, String>("name"));
        tclMastered.setCellValueFactory(new PropertyValueFactory<Word, Integer>("mastered"));
        tclFaulted.setCellValueFactory(new PropertyValueFactory<Word, Integer>("faulted"));
        tclFailed.setCellValueFactory(new PropertyValueFactory<Word, Integer>("failed"));
    }

    /**
     * Load in the topics from the topicSet into the combobox.
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
