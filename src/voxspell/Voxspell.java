package voxspell;
import voxspell.gui.SceneSwitcher;
import voxspell.gui.SceneType;

import voxspell.user.User;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.io.File;
import java.util.ResourceBundle;

public class Voxspell extends Application {

    private static Stage _stage;
    public void start(Stage stage) throws Exception {
        // Save the stage reference.
        _stage = stage;

        // Set the title.
        ResourceBundle rb = SceneSwitcher.getInstance().getResources();
        stage.setTitle(rb.getString("app.title") + " - " + rb.getString("app.version"));

        // Set it so it can't be resized.
        stage.setResizable(false);

        // Switch to the main menu and show.
        SceneSwitcher.getInstance().execute(SceneType.TITLE_MENU);
        SceneSwitcher.getInstance().showDialog(SceneType.USER_MENU);
    }

    public static void main(String[] args) {
        new File("saves/").mkdir();
        launch(args);
    }

    public void stop() {
        if (User.getInstance() != null) {
            User.getInstance().save();
        }
    }

    public static Stage getApplicationStage() {
        return _stage;
    }
}
