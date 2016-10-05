package voxspell.gui;

import voxspell.user.User;
import voxspell.spell.Topic;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class SettingsController implements Initializable {
    @FXML
    private Label lblTitle;

    @FXML
    private Button btnMenu;

    @FXML
    void btnMenuPressed(ActionEvent event) {
        SceneSwitcher.getInstance().execute(SceneType.MENU);
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {

    }

}
