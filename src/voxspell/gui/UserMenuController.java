package voxspell.gui;

import voxspell.user.User;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.application.Platform;

import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;

public class UserMenuController implements Initializable {
    @FXML
    private Label lblTitle;

    @FXML
    private Label lblAuthor;

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnExit;

    @FXML
    private CheckBox cbxNewUser;

    @FXML
    private TextField txtNewUser;

    @FXML
    private ComboBox<String> cmbUsers;

    @FXML
    void btnLoginPressed(ActionEvent event) {
        User.switchToUser(cbxNewUser.isSelected()?txtNewUser.getText():cmbUsers.getValue());
        if (User.getInstance().getSelectedTopicSet() == null) {
            SceneSwitcher.getInstance().execute(SceneType.TOPIC_MENU);
        }
        else {
            SceneSwitcher.getInstance().execute(SceneType.MENU);
        }
    }

    @FXML
    void btnExitPressed(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void cbxNewUserSelectedChanged(ActionEvent event) {
        updateAbilities();
    }

    @FXML
    void txtNewUserTextChanged(ActionEvent event) {
        updateAbilities();
    }

    @FXML
    void cmbUsersSlectionChanged(ActionEvent event) {
        updateAbilities();
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        loadUsers();

        // http://code.makery.ch/blog/javafx-2-event-handlers-and-change-listeners/
        txtNewUser.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> o, String oldV, String newV) {
                updateAbilities();
            }
        });
        updateAbilities();

    }

    private void updateAbilities() {
        if (cbxNewUser.isSelected()) {
            txtNewUser.setDisable(false);
            cmbUsers.setDisable(true);
            for (String u : cmbUsers.getItems()) {
                if (u.equals(txtNewUser.getText())) {

                    btnLogin.setDisable(true);
                    return;
                }
            }
            btnLogin.setDisable(txtNewUser.getText().equals(""));
        }
        else {
            txtNewUser.setDisable(true);
            cmbUsers.setDisable(false);
            btnLogin.setDisable(cmbUsers.getValue() == null || cmbUsers.getValue().equals(""));
        }
    }

    private void loadUsers() {
        ObservableList<String> users = FXCollections.observableArrayList();
        File saveDirectory = new File("saves/");

        for (File f : saveDirectory.listFiles()) {
            if (f.isFile()) {
                users.add(f.getName());
            }
        }

        cmbUsers.setItems(users);

        if (users.size() > 0) {
            cmbUsers.getSelectionModel().select(0);
        }
    }
}