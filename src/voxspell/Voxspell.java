package voxspell;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Platform;

public class Voxspell extends Application {
    public void start(Stage stage) throws Exception {
        // Load in the main menu.
        Parent root = FXMLLoader.load(getClass().getResource("gui/MainMenu.fxml"));
        Scene scene = new Scene(root);

        // Set the title and c  loseRequest.
        stage.setTitle("Voxspell - Kevin Hira");
        stage.setOnCloseRequest(e -> cleanUp());

        // Switch to the main menu and show.
        stage.setScene(scene);
        stage.show();

        // Set it so it can't be resized.
        stage.setResizable(false);
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void cleanUp() {
        //TODO: Cleanup things.
    }
}
