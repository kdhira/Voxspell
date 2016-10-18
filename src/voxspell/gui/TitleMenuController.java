package voxspell.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Label;
import javafx.scene.control.Button;

public class TitleMenuController implements Initializable {
    @FXML
    private Label lblTitle;

    @FXML
    private Label lblSubtitle;

    @FXML
    private Label lblAuthor;

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnExit;

    @FXML
    void btnExitPressed(ActionEvent event) {
        SceneSwitcher.getInstance().showDialog(SceneType.EXIT);
    }

    @FXML
    void btnLoginPressed(ActionEvent event) {
        SceneSwitcher.getInstance().showDialog(SceneType.USER_MENU);
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {

    }

}
