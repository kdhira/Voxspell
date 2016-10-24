package voxspell.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Label;
import javafx.scene.control.Button;

/**
 * Controls the Title Menu.
 * @author Kevin Hira.
 */
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

    /**
     * Handles the press of btnEx`it.
     */
    @FXML
    void btnExitPressed(ActionEvent event) {
        // Exit the application.
        SceneSwitcher.getInstance().showDialog(SceneType.EXIT);
    }

    /**
     * Handles the press of btnLogin.
     */
    @FXML
    void btnLoginPressed(ActionEvent event) {
        // Show the login dialog.
        SceneSwitcher.getInstance().showDialog(SceneType.USER_MENU);
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {

    }

}
