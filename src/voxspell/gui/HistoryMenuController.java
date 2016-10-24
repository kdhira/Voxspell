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
import java.text.SimpleDateFormat;

import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.chart.PieChart;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Controls the History Menu.
 * @author Kevin Hira.
 */
public class HistoryMenuController implements Initializable {
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
    private ComboBox<String> cmbQuizzes;

    private Quiz _quiz;

    /**
     * Handles the selection change of cmbQuizzes.
     */
    @FXML
    void cmbQuizzesSelectionChanged(ActionEvent event) {
        // Reload data.
        loadPieChartData();
    }

    /**
     * Handles the press of btnBack.
     */
    @FXML
    void btnBackPressed(ActionEvent event) {
        // Switch to main menu.
        SceneSwitcher.getInstance().execute(SceneType.MENU);
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        // Get data for cmbQuizzes.
        cmbQuizzesSetUp();
        if (_quiz == null) {
            SceneSwitcher.getInstance().execute(SceneType.MENU);
            return;
        }

    }

    /**
     * Sets up the combobox elements for the combobox.
     */
    private void cmbQuizzesSetUp() {
        ObservableList<String> quizzes = FXCollections.observableArrayList();

        for (Quiz q : User.getInstance().getQuizzes()) {
            quizzes.add(q.getTopic().getName()+ " (" + new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(q.getDateCompleted()) + ")");
        }

        cmbQuizzes.setItems(quizzes);
        cmbQuizzes.getSelectionModel().select(quizzes.size()-1);

        loadPieChartData();
    }

    /**
     * Builds the pie chart data. Also sets the labels' text.
     */
    private void loadPieChartData() {
        _quiz = _quiz = User.getInstance().getQuizzes().get(cmbQuizzes.getSelectionModel().getSelectedIndex());
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

        lblLevel.setText(_quiz.getTopic().getName());
        lblAccuracy.setText("Accuracy: " + String.format("%.0f", _quiz.getAccuracy()*100) + "%");
    }
}
