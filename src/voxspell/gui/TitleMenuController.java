package voxspell.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Label;

public class TitleMenuController implements Initializable {
    @FXML
    private Label lblTitle;

    @FXML
    private Label lblSubtitle;

    @FXML
    private Label lblAuthor;

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        lblAuthor.setText(rb.getString("app.authorShort") + ", " + rb.getString("app.affiliation"));
        
    }

}
