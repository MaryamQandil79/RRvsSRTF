package com.scheduler.controller;

import com.scheduler.model.Process;
import com.scheduler.scheduler.*;
import com.scheduler.util.MetricsCalculator;
import com.scheduler.util.Validator;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.net.URL;
import java.util.*;

public class MainController implements Initializable {


    @FXML private TextField tfProcessId;
    @FXML private TextField tfArrivalTime;
    @FXML private TextField tfBurstTime;
    @FXML private TextField tfQuantum;
    @FXML private Button    btnAddProcess;
    @FXML private Button    btnRemoveProcess;
    @FXML private Button    btnClearAll;
    @FXML private Button    btnRun;


    @FXML private TableView<Process>            tblProcesses;
    @FXML private TableColumn<Process,String>   colId;
    @FXML private TableColumn<Process,Integer>  colArrival;
    @FXML private TableColumn<Process,Integer>  colBurst;


    @FXML private Label lblRrAvgWT;
    @FXML private Label lblRrAvgTAT;
    @FXML private Label lblRrAvgRT;
    @FXML private Label lblSrAvgWT;
    @FXML private Label lblSrAvgTAT;
    @FXML private Label lblSrAvgRT;
    @FXML private Label lblWinner;


    @FXML private ListView<String>              lvReadyQueue;
    @FXML private ScrollPane                    spRrGantt;
    @FXML private TableView<Process>            tblRrMetrics;
    @FXML private TableColumn<Process,String>   colRrId;
    @FXML private TableColumn<Process,Integer>  colRrCT;
    @FXML private TableColumn<Process,Integer>  colRrTAT;
    @FXML private TableColumn<Process,Integer>  colRrWT;
    @FXML private TableColumn<Process,Integer>  colRrRT;


    @FXML private ScrollPane                    spSrtfGantt;
    @FXML private TableView<Process>            tblSrtfMetrics;
    @FXML private TableColumn<Process,String>   colSrId;
    @FXML private TableColumn<Process,Integer>  colSrCT;
    @FXML private TableColumn<Process,Integer>  colSrTAT;
    @FXML private TableColumn<Process,Integer>  colSrWT;
    @FXML private TableColumn<Process,Integer>  colSrRT;


    @FXML private ScrollPane                    spRrGanttSide;
    @FXML private ScrollPane                    spSrtfGanttSide;
    @FXML private TableView<Process>            tblRrMetricsSide;
    @FXML private TableColumn<Process,String>   colRrIdS;
    @FXML private TableColumn<Process,Integer>  colRrCTS;
    @FXML private TableColumn<Process,Integer>  colRrTATS;
    @FXML private TableColumn<Process,Integer>  colRrWTS;
    @FXML private TableColumn<Process,Integer>  colRrRTS;
    @FXML private TableView<Process>            tblSrtfMetricsSide;
    @FXML private TableColumn<Process,String>   colSrIdS;
    @FXML private TableColumn<Process,Integer>  colSrCTS;
    @FXML private TableColumn<Process,Integer>  colSrTATS;
    @FXML private TableColumn<Process,Integer>  colSrWTS;
    @FXML private TableColumn<Process,Integer>  colSrRTS;


    @FXML private TextArea taConclusion;


    @FXML private Button btnTest1;
    @FXML private Button btnTest2;
    @FXML private Button btnTest3;
    @FXML private Button btnTest4;
    @FXML private Button btnTest5;


    private final ObservableList<Process> processList = FXCollections.observableArrayList();

    private static final String[] BLOCK_COLORS = {
            "#4C9BE8", "#E8734C", "#4CE87A", "#E8C84C",
            "#A04CE8", "#4CE8D8", "#E84C8D", "#8DE84C",
            "#E84C4C", "#4C6EE8"
    };



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupProcessTable();
        setupMetricsTables();
        tblProcesses.setItems(processList);
        tblProcesses.setPlaceholder(new Label("No processes added yet."));
        tfQuantum.setText("3");
    }



    private void setupProcessTable() {
        colId.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getProcessId()));
        colArrival.setCellValueFactory(d ->
                new SimpleIntegerProperty(d.getValue().getArrivalTime()).asObject());
        colBurst.setCellValueFactory(d ->
                new SimpleIntegerProperty(d.getValue().getBurstTime()).asObject());
    }

    private void setupMetricsTables() {

        colRrId .setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getProcessId()));
        colRrCT .setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getCompletionTime()).asObject());
        colRrTAT.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getTurnaroundTime()).asObject());
        colRrWT .setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getWaitingTime()).asObject());
        colRrRT .setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getResponseTime()).asObject());


        colSrId .setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getProcessId()));
        colSrCT .setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getCompletionTime()).asObject());
        colSrTAT.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getTurnaroundTime()).asObject());
        colSrWT .setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getWaitingTime()).asObject());
        colSrRT .setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getResponseTime()).asObject());


        colRrIdS .setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getProcessId()));
        colRrCTS .setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getCompletionTime()).asObject());
        colRrTATS.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getTurnaroundTime()).asObject());
        colRrWTS .setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getWaitingTime()).asObject());
        colRrRTS .setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getResponseTime()).asObject());


        colSrIdS .setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getProcessId()));
        colSrCTS .setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getCompletionTime()).asObject());
        colSrTATS.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getTurnaroundTime()).asObject());
        colSrWTS .setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getWaitingTime()).asObject());
        colSrRTS .setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getResponseTime()).asObject());
    }



    @FXML
    private void onAddProcess() {
        try {
            Validator.validateNewProcess(
                    tfProcessId.getText(), tfArrivalTime.getText(),
                    tfBurstTime.getText(), processList);

            Process p = new Process(
                    tfProcessId.getText().trim(),
                    Integer.parseInt(tfArrivalTime.getText().trim()),
                    Integer.parseInt(tfBurstTime.getText().trim())
            );
            processList.add(p);
            tfProcessId.clear();
            tfArrivalTime.clear();
            tfBurstTime.clear();
            tfProcessId.requestFocus();

        } catch (IllegalArgumentException e) {
            showError("Input Error", e.getMessage());
        }
    }

    @FXML
    private void onRemoveProcess() {
        Process selected = tblProcesses.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Selection Error", "Please select a process to remove.");
            return;
        }
        processList.remove(selected);
    }

    @FXML
    private void onClearAll() {
        processList.clear();
        clearResults();
    }

    @FXML
    private void onRun() {
        try {
            Validator.validateProcessList(processList);
            int quantum = Validator.validateQuantum(tfQuantum.getText());

            List<Process> snapshot = new ArrayList<>(processList);

            RoundRobinScheduler rrSched = new RoundRobinScheduler(quantum);
            SchedulerResult rrResult = rrSched.run(snapshot);

            SRTFScheduler srtfSched = new SRTFScheduler();
            SchedulerResult srtfResult = srtfSched.run(snapshot);

            displayResults(rrResult, srtfResult, quantum);

        } catch (IllegalArgumentException e) {
            showError("Simulation Error", e.getMessage());
        }
    }



    @FXML private void onTest1() {
        loadTestData(
                new int[][]{{0,6},{1,4},{2,2},{3,8},{4,3}},
                new String[]{"P1","P2","P3","P4","P5"}, "3");
    }
    @FXML private void onTest2() {
        loadTestData(
                new int[][]{{0,10},{0,4},{0,6},{0,2}},
                new String[]{"P1","P2","P3","P4"}, "2");
    }
    @FXML private void onTest3() {
        loadTestData(
                new int[][]{{0,1},{1,2},{2,1},{3,3},{4,1}},
                new String[]{"P1","P2","P3","P4","P5"}, "2");
    }
    @FXML private void onTest4() {
        loadTestData(
                new int[][]{{0,5},{1,5},{2,5},{3,5}},
                new String[]{"P1","P2","P3","P4"}, "4");
    }
    @FXML private void onTest5() {
        processList.clear();
        tfProcessId.setText("P1");
        tfArrivalTime.setText("-2");
        tfBurstTime.setText("0");
        tfQuantum.setText("-1");
        showInfo("Invalid Input Demo",
                "The fields have been pre-filled with invalid values.\n"
                        + "Click 'Add Process' or 'Run' to see the validation messages.");
    }

    private void loadTestData(int[][] atbt, String[] ids, String quantum) {
        processList.clear();
        clearResults();
        for (int i = 0; i < ids.length; i++) {
            processList.add(new Process(ids[i], atbt[i][0], atbt[i][1]));
        }
        tfQuantum.setText(quantum);
    }



    private void displayResults(SchedulerResult rr, SchedulerResult srtf, int quantum) {


        lblRrAvgWT .setText(String.format("%.2f", rr.getAvgWaitingTime()));
        lblRrAvgTAT.setText(String.format("%.2f", rr.getAvgTurnaroundTime()));
        lblRrAvgRT .setText(String.format("%.2f", rr.getAvgResponseTime()));
        lblSrAvgWT .setText(String.format("%.2f", srtf.getAvgWaitingTime()));
        lblSrAvgTAT.setText(String.format("%.2f", srtf.getAvgTurnaroundTime()));
        lblSrAvgRT .setText(String.format("%.2f", srtf.getAvgResponseTime()));

        double rrScore   = rr.getAvgWaitingTime()   + rr.getAvgTurnaroundTime();
        double srtfScore = srtf.getAvgWaitingTime() + srtf.getAvgTurnaroundTime();

        if (Math.abs(rrScore - srtfScore) < 0.01) {
            lblWinner.setText("TIE");
            lblWinner.setStyle("-fx-text-fill: #FFA500; -fx-font-weight: bold;");
        } else if (srtfScore < rrScore) {
            lblWinner.setText("SRTF");
            lblWinner.setStyle("-fx-text-fill: #4CE87A; -fx-font-weight: bold;");
        } else {
            lblWinner.setText("Round Robin");
            lblWinner.setStyle("-fx-text-fill: #4C9BE8; -fx-font-weight: bold;");
        }


        lvReadyQueue.setItems(FXCollections.observableArrayList(rr.getReadyQueueLog()));


        buildGanttChart(rr.getGanttBlocks(), spRrGantt, rr.getProcesses());

        buildGanttChart(srtf.getGanttBlocks(), spSrtfGantt, srtf.getProcesses());

        buildGanttChart(rr.getGanttBlocks(), spRrGanttSide, rr.getProcesses());
        buildGanttChart(srtf.getGanttBlocks(), spSrtfGanttSide, srtf.getProcesses());


        tblRrMetrics.setItems(FXCollections.observableArrayList(rr.getProcesses()));
        tblSrtfMetrics.setItems(FXCollections.observableArrayList(srtf.getProcesses()));
        tblRrMetricsSide.setItems(FXCollections.observableArrayList(rr.getProcesses()));
        tblSrtfMetricsSide.setItems(FXCollections.observableArrayList(srtf.getProcesses()));


        taConclusion.setText(MetricsCalculator.generateComparison(rr, srtf, quantum));
    }



    private void buildGanttChart(List<GanttBlock> blocks,
                                 ScrollPane pane,
                                 List<Process> processes) {
        HBox chart = new HBox(0);
        chart.setAlignment(Pos.BOTTOM_LEFT);
        chart.setPadding(new Insets(10, 10, 40, 10));

        Map<String, String> colorMap = new LinkedHashMap<>();
        int ci = 0;
        for (Process p : processes) {
            colorMap.put(p.getProcessId(), BLOCK_COLORS[ci++ % BLOCK_COLORS.length]);
        }

        final int UNIT_WIDTH  = 28;
        final int BLOCK_HEIGHT = 48;

        for (GanttBlock block : blocks) {
            int duration = block.getEndTime() - block.getStartTime();
            int width    = Math.max(duration * UNIT_WIDTH, 40);
            String color = block.isIdle() ? "#555555" :
                    colorMap.getOrDefault(block.getProcessId(), "#888888");

            StackPane cell = buildGanttCell(
                    block.getProcessId(), block.getStartTime(), block.getEndTime(),
                    width, BLOCK_HEIGHT, color);
            chart.getChildren().add(cell);
        }

        if (!blocks.isEmpty()) {
            GanttBlock last = blocks.get(blocks.size() - 1);
            Label endLbl = new Label(String.valueOf(last.getEndTime()));
            endLbl.getStyleClass().add("gantt-time-label");
            chart.getChildren().add(endLbl);
        }

        pane.setContent(chart);
        pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        pane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pane.setFitToHeight(true);
    }

    private StackPane buildGanttCell(String pid, int start, int end,
                                     int width, int height, String color) {
        StackPane cell = new StackPane();
        cell.setPrefSize(width, height);

        Rectangle rect = new Rectangle(width, height);
        rect.setFill(Color.web(color));
        rect.setStroke(Color.web("#1a1a2e"));
        rect.setStrokeWidth(1.5);
        rect.setArcWidth(6);
        rect.setArcHeight(6);

        Text nameText = new Text(pid);
        nameText.setFill(Color.WHITE);
        nameText.setFont(Font.font("Monospace", FontWeight.BOLD, 12));
        nameText.setTextAlignment(TextAlignment.CENTER);

        cell.getChildren().addAll(rect, nameText);

        Label startLbl = new Label(String.valueOf(start));
        startLbl.getStyleClass().add("gantt-time-label");
        startLbl.setTranslateY(height / 2.0 + 12);
        startLbl.setTranslateX(-(width / 2.0) + 4);

        StackPane wrapper = new StackPane(cell, startLbl);
        wrapper.setAlignment(Pos.BOTTOM_LEFT);
        return wrapper;
    }



    private void clearResults() {
        lblRrAvgWT.setText("—");
        lblRrAvgTAT.setText("—");
        lblRrAvgRT.setText("—");
        lblSrAvgWT.setText("—");
        lblSrAvgTAT.setText("—");
        lblSrAvgRT.setText("—");
        lblWinner.setText("—");
        lvReadyQueue.setItems(FXCollections.emptyObservableList());
        spRrGantt.setContent(new Pane());
        spSrtfGantt.setContent(new Pane());
        spRrGanttSide.setContent(new Pane());
        spSrtfGanttSide.setContent(new Pane());
        tblRrMetrics.setItems(FXCollections.emptyObservableList());
        tblSrtfMetrics.setItems(FXCollections.emptyObservableList());
        tblRrMetricsSide.setItems(FXCollections.emptyObservableList());
        tblSrtfMetricsSide.setItems(FXCollections.emptyObservableList());
        taConclusion.clear();
    }

    private void showError(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showInfo(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}