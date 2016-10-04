package voxspell.gui;

import voxspell.user.User;
import voxspell.spell.Word;

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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

    private void loadWords() {
        ObservableList<Word> words = FXCollections.observableArrayList();
        for (Word w : User.getInstance().targetTopic().getWords()) {
            words.add(w);
        }
        tblStatistics.setItems(words);
        tclName.setCellValueFactory(new PropertyValueFactory<Word, String>("name"));
        tclMastered.setCellValueFactory(new PropertyValueFactory<Word, Integer>("mastered"));
        tclFaulted.setCellValueFactory(new PropertyValueFactory<Word, Integer>("faulted"));
        tclFailed.setCellValueFactory(new PropertyValueFactory<Word, Integer>("failed"));
    }

    @FXML
    void btnMenuPressed(ActionEvent event) {
        SceneSwitcher.getInstance().execute(SceneType.MENU);
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        lblLevel.setText(User.getInstance().targetTopic().getName());
        loadWords();
    }
}
