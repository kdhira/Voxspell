package voxspell.gui;
import voxspell.Voxspell;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.application.Platform;

import java.io.IOException;

/**
 * Handles the changing of scenes in the application.
 * @author Kevin Hira.
 */
public class SceneSwitcher {
    private static SceneSwitcher _sceneSwitcher;

    public static SceneSwitcher getInstance() {
        return _sceneSwitcher == null ? _sceneSwitcher = new SceneSwitcher() : _sceneSwitcher;
    }

    private SceneSwitcher() {

    }

    private Parent getSceneFXML(SceneType newScene) {
        switch (newScene) {
            case NEW_QUIZ:
                try {
                    return FXMLLoader.load(getClass().getResource("SpellingTest.fxml"));
                }
                catch (IOException e) {
                    System.err.println("Could not find \"SpellingTest.fxml");
                    break;
                }
            case VIEW_STATISTICS:
                try {
                    return FXMLLoader.load(getClass().getResource("Statistics.fxml"));
                }
                catch (IOException e) {
                    System.err.println("Could not find \"SpellingTest.fxml");
                    break;
                }
            default:
                System.err.println("Not implemented.");
                break;
            case MENU:
                try {
                    return FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
                }
                catch (IOException e) {
                    System.err.println("Could not find \"MainMenu.fxml");
                    break;
                }
        }
        return null;
    }

    private Stage getStage() {
        return Voxspell.getApplicationStage();
    }

    public Stage execute(SceneType newScene) {
        // Get application stage.
        Stage stage = getStage();

        // Set the new scene and show it.
        stage.setScene(new Scene(getSceneFXML(newScene)));
        stage.show();

        // Return the stage, so perhap who called it can do more operations on it.
        return stage;
    }

    public void close() {
        getStage().close();
    }


}
