package voxspell;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Voxspell extends Application {
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("gui/MainMenu.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Voxspell - KDHira");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
