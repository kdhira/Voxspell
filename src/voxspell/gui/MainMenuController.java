package voxspell.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Label;

public class MainMenuController implements Initializable {

    @FXML
    private Label lblTitle;

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Hello World");
    }

}
