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
        fChooser.setInitialDirectory(new File("./"));
        
        File f = fChooser.showOpenDialog(SceneSwitcher.getInstance().getStage());

        if (f != null) {
            try {
                User.getInstance().addWordlist(f.getCanonicalPath());
            }
            catch (IOException e) {

            }
        }

        setUpComboBoxes();

    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        if (User.getInstance().getSelectedTopicSet() == null) {
            btnBack.setText("Exit");
        }

        setUpComboBoxes();
    }

    private void setUpComboBoxes() {
        ObservableList<String> wordlists = FXCollections.observableArrayList();
        for (TopicSet t : User.getInstance().getTopicSets()) {
            wordlists.add(t.getName());
        }

        cmbWordlists.setItems(wordlists);
    }
}
