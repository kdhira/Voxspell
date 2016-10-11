package voxspell.gui;
import voxspell.Voxspell;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Modality;

import java.util.List;
import java.util.ArrayList;

import javafx.application.Platform;

import java.io.IOException;

/**
 * Handles the changing of scenes in the application.
 * @author Kevin Hira.
 */
public class SceneSwitcher {
    private static SceneSwitcher _sceneSwitcher;

    private List<SceneType> _requestQueue;
    private List<Boolean> _requestTypes;
    private int _dialogDepth;

    public static SceneSwitcher getInstance() {
        return _sceneSwitcher == null ? _sceneSwitcher = new SceneSwitcher() : _sceneSwitcher;
    }

    private SceneSwitcher() {
        _requestQueue = new ArrayList<SceneType>();
        _requestTypes = new ArrayList<Boolean>();
        _dialogDepth = 0;
    }

    private Parent getSceneFXML(SceneType newScene) {
        switch (newScene) {
            case NEW_QUIZ:
                return tryRetrieve("SpellingTest.fxml");
            case VIEW_STATISTICS:
                return tryRetrieve("Statistics.fxml");
            case SETTINGS:
                return tryRetrieve("Settings.fxml");
            case USER_MENU:
                return tryRetrieve("UserMenu.fxml");
            case TOPIC_MENU:
                return tryRetrieve("TopicMenu.fxml");
            default:
                System.err.println("Not implemented.");
            case MENU:
                return tryRetrieve("MainMenu.fxml");
        }
        // return null;
    }

    private Parent tryRetrieve(String fxml) {
        try {
            return FXMLLoader.load(getClass().getResource(fxml));
        }
        catch (IOException e) {
            System.err.println("Could not load \"" + fxml + "\"");
            return null;
        }
    }

    public Stage getStage() {
        return Voxspell.getApplicationStage();
    }

    public Stage execute(SceneType newScene) {
        if (newScene == SceneType.EXIT) {
            Platform.exit();
            return null;
        }
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

    // http://stackoverflow.com/questions/22166610/how-to-create-a-popup-windows-in-javafx
    public void showDialog(SceneType dialogScene) {
        if (dialogScene == SceneType.EXIT) {
            Platform.exit();
            return;
        }
        Stage dialogStage = new Stage();

        dialogStage.initOwner(getStage());
        dialogStage.initModality(Modality.APPLICATION_MODAL);

        dialogStage.setScene(new Scene(getSceneFXML(dialogScene)));
        ++_dialogDepth;
        dialogStage.showAndWait();
        flushRequests();
        --_dialogDepth;
    }

    private void flushRequests() {
        if (_dialogDepth > 1) {
            return;
        }
        int i = 0;
        while (i < _requestQueue.size()) {
            if (_requestTypes.get(i)) {
                showDialog(_requestQueue.get(i));
            }
            else {
                execute(_requestQueue.get(i));
            }
            ++i;
        }
        _requestQueue.clear();
        _requestTypes.clear();
    }

    public void addChangeSceneRequest(SceneType requestedScene) {
        addToQueue(requestedScene, false);
    }

    public void addSceneDialogRequest(SceneType requestedScene) {
        addToQueue(requestedScene, true);

    }

    private void addToQueue(SceneType s, boolean isDialog) {
        _requestQueue.add(s);
        _requestTypes.add(isDialog);
    }


}
