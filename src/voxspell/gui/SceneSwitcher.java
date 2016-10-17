package voxspell.gui;

import voxspell.Voxspell;
import voxspell.user.User;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Modality;

import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Stack;

import javafx.application.Platform;

import java.io.IOException;

/**
 * Handles the changing of scenes in the application.
 * @author Kevin Hira.
 */
public class SceneSwitcher {
    private static SceneSwitcher _sceneSwitcher;

    private Stack<Stage> _stages;

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
        _stages = new Stack<Stage>();
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
            case TITLE_MENU:
                return tryRetrieve("TitleMenu.fxml");
            default:
                System.err.println("Not implemented.");
            case MENU:
                return tryRetrieve("MainMenu.fxml");
        }
        // return null;
    }

    private Parent tryRetrieve(String fxml) {
        try {
            FXMLLoader fLoader = new FXMLLoader();
            fLoader.setResources(getResources());
            fLoader.setLocation(getClass().getResource(fxml));

            return fLoader.load();
        }
        catch (IOException e) {
            System.err.println("Could not load \"" + fxml + "\"");
            e.printStackTrace();
            return null;
        }
    }

    public void pushStage(Stage s) {
        _stages.push(s);
    }

    public Stage getStage() {
        return _stages.peek();
    }

    public Stage popStage() {
        return _stages.pop();
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

        ++_dialogDepth;
        pushStage(dialogStage);
        dialogStage.setScene(new Scene(getSceneFXML(dialogScene)));
        dialogStage.showAndWait();
        popStage();
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

    public ResourceBundle getResources() {
        Locale locale = User.getInstance() != null ? User.getInstance().getLocale() : new Locale("en", "EN");
        return ResourceBundle.getBundle("voxspell.properties.lang", locale);
    }
}
