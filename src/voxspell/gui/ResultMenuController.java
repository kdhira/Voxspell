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

    private Quiz _quiz;

    @FXML
    void btnBackPressed(ActionEvent event) {
        SceneSwitcher.getInstance().execute(SceneType.MENU);
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        _quiz = User.getInstance().getLastQuiz();
        loadPieChartData();
        lblLevel.setText(_quiz.getTopic().getName());
        lblAccuracy.setText("Accuracy: " + String.format("%.0f", _quiz.getAccuracy()*100) + "%");
    }

    private void loadPieChartData() {
        pChart.setLegendVisible(false);
        Map<WordResult, Integer> data =  _quiz.retrieveTotals();
        ObservableList<PieChart.Data> pcData = FXCollections.observableArrayList();
        for (WordResult wR : data.keySet()) {
            pcData.add(new PieChart.Data(wR.toString(), data.get(wR)));
        }

        pChart.setData(pcData);

        int offset = 0;
        for (PieChart.Data pcD : pChart.getData()) {
            String colour = "#FFFFFF".replace("F", String.valueOf(offset%7+4));
            pcD.getNode().setStyle("-fx-pie-color: " + colour + ";");
            offset+=60;
        }



    }
}
