package voxspell.gui;

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
    private Button btnNewTest;

    @FXML
    private Button btnReviewTest;

    @FXML
    private Label lblTitle;

    @FXML
    private Button btnExit;

    @FXML
    void btnExitPressed(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Hello World");
    }

}
