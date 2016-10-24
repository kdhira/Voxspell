package voxspell.gui;

import voxspell.user.User;
import voxspell.Voxspell;

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
import javafx.stage.Stage;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
/**
 * Controls the User Menu.
 * @author Kevin Hira.
 */
public class UserMenuController implements Initializable {
    @FXML
    private Label lblTitle;

    @FXML
    private Label lblAuthor;

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnBack;

    @FXML
    private CheckBox cbxNewUser;

    @FXML
    private TextField txtNewUser;

    @FXML
    private ComboBox<String> cmbUsers;

    private int _userIndex = 0;
    /**
     * Handles the press of btnLogin.
     */
    @FXML
    void btnLoginPressed(ActionEvent event) {
        // Get the user to login as.
        User.switchToUser(cbxNewUser.isSelected()?txtNewUser.getText():cmbUsers.getValue());

        // Check if the word list has been set or not.
        if (User.getInstance().getSelectedTopicSet() == null) {
            SceneSwitcher.getInstance().addSceneDialogRequest(SceneType.TOPIC_MENU);
        }
        else {
            SceneSwitcher.getInstance().addChangeSceneRequest(SceneType.MENU);
        }
        ((Stage)btnLogin.getScene().getWindow()).close();
    }

    /**
     * Handles the press of btnBack.
     */
    @FXML
    void btnBackPressed(ActionEvent event) {
        ((Stage)btnBack.getScene().getWindow()).close();
    }

    /**
     * Handles the check change of cbxNewUser.
     */
    @FXML
    void cbxNewUserSelectedChanged(ActionEvent event) {
        // Updates button characteristics.
        updateAbilities();
    }

    /**
     * Handles the enter press of txtNewUser.
     */
    @FXML
    void txtNewUserEnterPressed(ActionEvent event) {
        // Pres the login button.
        btnLogin.fire();
    }

    /**
     * Handles the selection change of cmbUsers.
     */
    @FXML
    void cmbUsersSelectionChanged(ActionEvent event) {
        // Update buttons.
        _userIndex = cmbUsers.getSelectionModel().getSelectedIndex();
        updateAbilities();
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        // Set title for stage.
        SceneSwitcher.getInstance().getStage().setTitle("Choose User - Voxspell");
        loadUsers();

        // Add change listener to text box, updates buttons.
        txtNewUser.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> o, String oldV, String newV) {
                updateAbilities();
            }
        });


        updateAbilities();

    }

    /**
     * Sets which controls are enabled/disabled based on the state of the menu.
     */
    private void updateAbilities() {
        if (cbxNewUser.isSelected()) {
            txtNewUser.setDisable(false);
            cmbUsers.setDisable(true);
            for (int i = 0; i < cmbUsers.getItems().size(); ++i) {
                if (cmbUsers.getItems().get(i).equals(txtNewUser.getText())) {
                    cmbUsers.getSelectionModel().select(i);
                    btnLogin.setDisable(true);
                    return;
                }
            }
            cmbUsers.getSelectionModel().select(_userIndex);
            btnLogin.setDisable(txtNewUser.getText().equals(""));
        }
        else {
            txtNewUser.setDisable(true);
            cmbUsers.setDisable(false);
            btnLogin.setDisable(cmbUsers.getValue() == null || cmbUsers.getValue().equals(""));
        }
    }

    /**
     * Load in the users from serialised files.
     */
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
            if (User.getInstance() != null) {
                cmbUsers.getSelectionModel().select(User.getInstance().getName());
                _userIndex = cmbUsers.getSelectionModel().getSelectedIndex();
            }
            else {
                cmbUsers.getSelectionModel().select(0);
                _userIndex = 0;
            }
        }
        else {
            cbxNewUser.setSelected(true);
        }
    }
}
