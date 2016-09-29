package voxspell.gui;

import voxspell.user.User;
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

import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TopicMenuController implements Initializable {
    @FXML
    private Label lblTitle;

    @FXML
    private Button btnSubmit;

    @FXML
    private Button btnBack;

    @FXML
    private Button btnImport;

    @FXML
    private ComboBox cmbWordlists;

    @FXML
    private ComboBox cmbTopicLists;

    @FXML
    void btnSubmitPressed(ActionEvent event) {
        User.getInstance().setTopic(cmbWordlists.getSelectionModel().getSelectedIndex(), cmbTopicLists.getSelectionModel().getSelectedIndex());

        SceneSwitcher.getInstance().execute(SceneType.MENU);
    }

    @FXML
    void btnBackPressed(ActionEvent event) {
        if (User.getInstance().getSelectedTopicSet() == null) {
            Platform.exit();
        }
        else {
            SceneSwitcher.getInstance().execute(SceneType.MENU);
        }
    }

    @FXML
    void btnImportPressed(ActionEvent event) {
        FileChooser fChooser = new FileChooser();
        fChooser.setTitle("Open Word List");
        fChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        fChooser.setInitialDirectory(new File("./resources"));

        File f = fChooser.showOpenDialog(SceneSwitcher.getInstance().getStage());

        if (f != null) {
            try {
                if (User.getInstance().addWordlist(f.getCanonicalPath())) {
                    cmbWordlists.getItems().add(f.getName());
                    int newTopicIndex = User.getInstance().getTopicSets().size()-1;
                    cmbWordlists.getSelectionModel().select(newTopicIndex);
                }
            }
            catch (IOException e) {

            }
        }
        // setUpComboBoxes(0);

    }

    @FXML
    void cmbWordlistsSelectionChanged(ActionEvent event) {
        int topicSetIndex = cmbWordlists.getSelectionModel().getSelectedIndex();
        if (topicSetIndex >= 0 && topicSetIndex < User.getInstance().getTopicSets().size()) {
            TopicSet selectedTopic = User.getInstance().getTopicSets().get(topicSetIndex);
            updateTopicChoices(selectedTopic);
        }



    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        if (User.getInstance().getSelectedTopicSet() == null) {
            btnBack.setText("Exit");
        }

        setUpComboBoxes(0);
    }

    private void setUpComboBoxes(int init) {
        ObservableList<String> wordlists = FXCollections.observableArrayList();
        for (TopicSet t : User.getInstance().getTopicSets()) {
            wordlists.add(new File(t.getName()).getName());
        }

        cmbWordlists.setItems(wordlists);

        if (init >= 0 && wordlists.size() > init) {
            cmbWordlists.getSelectionModel().select(init);
            updateTopicChoices(User.getInstance().getTopicSets().get(init));
        }
    }

    private void updateTopicChoices(TopicSet selectedTopic) {
        ObservableList<String> topics = FXCollections.observableArrayList();
        for (int i = 0; i < selectedTopic.size(); ++i) {
            topics.add(selectedTopic.atPosition(i).getName());
        }

        cmbTopicLists.setItems(topics);

        if (topics.size() > 0) {
            cmbTopicLists.getSelectionModel().select(0);
        }
    }
}
