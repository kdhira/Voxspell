package voxspell.gui;

import voxspell.user.User;
import voxspell.spell.TopicSet;
import voxspell.Voxspell;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.application.Platform;

import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import javafx.stage.Stage;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Controls the Word List Menu.
 * @author Kevin Hira.
 */
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
    private ComboBox<String> cmbWordlists;

    @FXML
    private Label lblAuthor;

    /**
     * Handles the press of btnSubmit.
     */
    @FXML
    void btnSubmitPressed(ActionEvent event) {
        // Update value in User instance, and switch to the main menu.
        User.getInstance().setTopic(cmbWordlists.getSelectionModel().getSelectedIndex(), 0);
        SceneSwitcher.getInstance().addChangeSceneRequest(SceneType.MENU);

        ((Stage)btnSubmit.getScene().getWindow()).close();
    }

    /**
     * Handles the press of btnBack.
     */
    @FXML
    void btnBackPressed(ActionEvent event) {
        // If the word list hasn't been set then go back to the user menu.
        if (User.getInstance().getSelectedTopicSet() == null) {
            SceneSwitcher.getInstance().addSceneDialogRequest(SceneType.USER_MENU);
        }
        ((Stage)btnBack.getScene().getWindow()).close();
    }

    /**
     * Handles the press of btnImport.
     */
    @FXML
    void btnImportPressed(ActionEvent event) {
        retrieveNewList();

    }

    @FXML
    void cmbWordlistsSelectionChanged(ActionEvent event) {
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        // Set title and set up the combobox.
        SceneSwitcher.getInstance().getStage().setTitle("Choose Word List - Voxspell");
        setUpComboBoxes(0);
    }

    /**
     * Set up the combobox with word lists.
     * @param init which index should be selected to start with.
     */
    private void setUpComboBoxes(int init) {
        btnSubmit.setDisable(true);
        ObservableList<String> wordlists = FXCollections.observableArrayList();
        for (TopicSet t : User.getInstance().getTopicSets()) {
            wordlists.add(new File(t.getName()).getName());
        }

        cmbWordlists.setItems(wordlists);
        if (wordlists.size() == 0) {
            if (retrieveNewList()) {
                setUpComboBoxes(init);
            }
        }
        else if (init >= 0 && wordlists.size() > init) {
            cmbWordlists.getSelectionModel().select(init);
            btnSubmit.setDisable(false);
        }
    }

    /**
     * Opens a FileChooser and gets in a new word list that the user picks, if it s valid.
     * @return if the retrieve was successful.
     */
    private boolean retrieveNewList() {
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
                    setUpComboBoxes(newTopicIndex);
                }
            }
            catch (IOException e) {
                Logger.getLogger(TopicMenuController.class.getName()).log(Level.SEVERE, null, e);
                return false;
            }
        }
        else {
            return false;
        }

        return true;
    }
}
