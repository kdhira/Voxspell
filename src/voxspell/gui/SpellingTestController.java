package voxspell.gui;
import voxspell.SceneSwitcher;
import voxspell.SceneType;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;


public class SpellingTestController implements Initializable {

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
