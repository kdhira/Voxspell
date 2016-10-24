package voxspell.gui;

import voxspell.spell.Quiz;
import voxspell.spell.WordResult;
import voxspell.spell.SpellResult;
import voxspell.user.User;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.util.Map;

import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.chart.PieChart;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Controls the Results Menu.
 * @author Kevin Hira.
 */
public class ResultMenuController implements Initializable {
    @FXML
    private Label lblTitle;

    @FXML
    private Label lblLevel;

    @FXML
    private PieChart pChart;

    @FXML
    private Button btnBack;

    @FXML
    private Label lblAccuracy;

    @FXML
    private Button btnReward;

    private Quiz _quiz;

    /**
     * Handles the press of btnReward.
     */
    @FXML
    void btnRewardPressed(ActionEvent event) {
        // Switch to reward menu.
        SceneSwitcher.getInstance().showDialog(SceneType.REWARDS_MENU);
    }

    /**
     * Handles the press of btnBack.
     */
    @FXML
    void btnBackPressed(ActionEvent event) {
        // Go back to the main menu.
        SceneSwitcher.getInstance().execute(SceneType.MENU);
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        // Get the last quiz and load the pie chart data for it.
        _quiz = User.getInstance().getLastQuiz();
        loadPieChartData();
        lblLevel.setText(_quiz.getTopic().getName());
        lblAccuracy.setText("Accuracy: " + String.format("%.0f", _quiz.getAccuracy()*100) + "%");
        if (_quiz.getAccuracy() < 0.8) {
            btnReward.setVisible(false);
        }
    }

    /**
     * Builds the pie chart data.
     */
    private void loadPieChartData() {
        pChart.setLegendVisible(false);

        // Get data from quiz.
        Map<WordResult, Integer> data =  _quiz.retrieveTotals();
        ObservableList<PieChart.Data> pcData = FXCollections.observableArrayList();
        for (WordResult wR : data.keySet()) {
            pcData.add(new PieChart.Data(wR.toString(), data.get(wR)));
        }

        pChart.setData(pcData);

        // Colour the pie chart.
        int offset = 0;
        for (PieChart.Data pcD : pChart.getData()) {
            String colour = "#FFFFFF".replace("F", String.valueOf(offset%7+4));
            pcD.getNode().setStyle("-fx-pie-color: " + colour + ";");
            offset+=60;
        }
    }
}
