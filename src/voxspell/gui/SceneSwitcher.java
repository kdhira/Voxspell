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
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    /**
     * Retrieves a scene from a file.
     * @param newScene scene to retrieve.
     */
    private Parent getSceneFXML(SceneType newScene) {
        switch (newScene) {
            case NEW_QUIZ:
                return tryRetrieve("SpellingTest.fxml");
            case RESULT_MENU:
                return tryRetrieve("ResultMenu.fxml");
            case REWARDS_MENU:
                return tryRetrieve("RewardsMenu.fxml");
            case VIEW_STATISTICS:
                return tryRetrieve("Statistics.fxml");
            case HISTORY_MENU:
                return tryRetrieve("HistoryMenu.fxml");
            case SETTINGS:
                return tryRetrieve("Settings.fxml");
            case USER_MENU:
                return tryRetrieve("UserMenu.fxml");
            case TOPIC_MENU:
                return tryRetrieve("TopicMenu.fxml");
            case TITLE_MENU:
                return tryRetrieve("TitleMenu.fxml");
            default:
            case MENU:
                return tryRetrieve("MainMenu.fxml");
        }
        // return null;
    }

    /**
     * Try and load the fxml into a Parent object.
     * @param fxml location of fxml document.
     */
    private Parent tryRetrieve(String fxml) {
        try {
            FXMLLoader fLoader = new FXMLLoader();
            // Set the resourse bundle and location
            fLoader.setResources(getResources());
            fLoader.setLocation(getClass().getResource(fxml));

            return fLoader.load();
        }
        catch (IOException e) {
            Logger.getLogger(SceneSwitcher.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    /**
     * Push a stage to the Stage stack.
     * @param s the stage to push.
     */
    public void pushStage(Stage s) {
        _stages.push(s);
    }

    /**
     * Peek at top stage on Stage stack.
     * @return the top of the stack.
     */
    public Stage getStage() {
        return _stages.peek();
    }

    /**
    * Pop the top stage on Stage stack.
    * @return the top of the stack before pop.
    */
    public Stage popStage() {
        return _stages.pop();
    }

    /**
     * Switch scenes to newScene.
     * @param newScene the scene to switch to.
     * @return the stage scene is attached to.
     */
    public Stage execute(SceneType newScene) {
        if (newScene == SceneType.EXIT) {
            Platform.exit();
            return null;
        }
        // Get application stage.
        Stage stage = getStage();

        // Set the new scene and show it.
        Scene scene = new Scene(getSceneFXML(newScene));
        scene.getStylesheets().add(getClass().getResource("styles/main.css").toExternalForm());
        stage.setScene(scene);
        stage.show();

        // Return the stage, so perhap who called it can do more operations on it.
        return stage;
    }

    /**
     * Close the stage.
     */
    public void close() {
        getStage().close();
    }

    /**
     * Show a dialog with the selected scene. Opens a new stage and shows it as a dialog.
     * @param dialogScene the scene to create a dialog off.
     */
    public void showDialog(SceneType dialogScene) {
        if (dialogScene == SceneType.EXIT) {
            Platform.exit();
            return;
        }

        // Set up new stage
        Stage dialogStage = new Stage();

        dialogStage.initOwner(getStage());
        dialogStage.initModality(Modality.APPLICATION_MODAL);

        dialogStage.setResizable(false);

        // Push stage on to stack, and load in scene/
        ++_dialogDepth;
        pushStage(dialogStage);
        Scene scene = new Scene(getSceneFXML(dialogScene));
        scene.getStylesheets().add(getClass().getResource("styles/main.css").toExternalForm());
        dialogStage.setScene(scene);
        // Show stage and wait.
        dialogStage.showAndWait();
        // After it closes, pop stage off stack, and flush any request for scene changes.
        popStage();
        flushRequests();
        --_dialogDepth;
    }
    /**
     * Flushes through any scene change requests that may have happened during a dialog.
     */
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

    /**
     * Add a scene change request.
     * @param requestedScene scene to add.
     */
    public void addChangeSceneRequest(SceneType requestedScene) {
        addToQueue(requestedScene, false);
    }

    /**
     * Add a scene dialog request.
     * @param requestedScene scene to add.
     */
    public void addSceneDialogRequest(SceneType requestedScene) {
        addToQueue(requestedScene, true);

    }

    /**
     * Add requested scene to the queue.
     * @param s scene to add.
     * @param isDialog whether the scene is a dialog or not.
     */
    private void addToQueue(SceneType s, boolean isDialog) {
        _requestQueue.add(s);
        _requestTypes.add(isDialog);
    }

    /**
     * Get the resource bundle from file.
     */
    public ResourceBundle getResources() {
        Locale locale = User.getInstance() != null ? User.getInstance().getLocale() : new Locale("en", "EN");
        return ResourceBundle.getBundle("voxspell.properties.lang", locale);
    }
}
