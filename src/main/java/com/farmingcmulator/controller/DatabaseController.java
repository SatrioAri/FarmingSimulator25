package com.farmingcmulator.controller;

import com.farmingcmulator.GameState;
import com.farmingcmulator.SceneManager;
import com.farmingcmulator.model.Crop;
import com.farmingcmulator.model.Highscore;
import com.farmingcmulator.util.SoundManager;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class DatabaseController implements Initializable {

    @FXML private VBox selectionPane;
    @FXML private VBox cropsPane;
    @FXML private VBox highscoresPane;
    
    @FXML private TableView<Crop> cropsTable;
    @FXML private TableColumn<Crop, Integer> cropIndexCol;
    @FXML private TableColumn<Crop, String> cropNameCol;
    @FXML private TableColumn<Crop, String> cropRarityCol;
    @FXML private TableColumn<Crop, String> cropStatusCol;
    
    @FXML private TableView<Highscore> highscoresTable;
    @FXML private TableColumn<Highscore, String> hsNameCol;
    @FXML private TableColumn<Highscore, Integer> hsScoreCol;
    
    private GameState gameState;
    private SoundManager sound = SoundManager.getInstance();
    private List<Crop> cropList;
    private List<Highscore> highscoreList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gameState = GameState.getInstance();
        
        cropIndexCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getIndex()).asObject());
        cropNameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        cropRarityCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRarity()));
        cropStatusCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));
        
        hsNameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPlayerName()));
        hsScoreCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getScore()).asObject());
        
        showSelectionPane();
    }

    private void showSelectionPane() {
        selectionPane.setVisible(true);
        selectionPane.setManaged(true);
        cropsPane.setVisible(false);
        cropsPane.setManaged(false);
        highscoresPane.setVisible(false);
        highscoresPane.setManaged(false);
    }

    @FXML
    private void onCropsClicked() {
        sound.playClick();
        selectionPane.setVisible(false);
        selectionPane.setManaged(false);
        cropsPane.setVisible(true);
        cropsPane.setManaged(true);
        
        gameState.reloadCropDatabase();
        cropList = new ArrayList<>(gameState.getCropDatabase());
        refreshCropsTable();
    }

    @FXML
    private void onHighscoresClicked() {
        sound.playClick();
        selectionPane.setVisible(false);
        selectionPane.setManaged(false);
        highscoresPane.setVisible(true);
        highscoresPane.setManaged(true);
        
        gameState.reloadHighscores();
        highscoreList = new ArrayList<>(gameState.getHighscores());
        refreshHighscoresTable();
    }

    @FXML
    private void sortCropsAlphabetical() {
        sound.playClick();
        cropList.sort(Comparator.comparing(Crop::getName));
        refreshCropsTable();
    }

    @FXML
    private void sortCropsAscending() {
        sound.playClick();
        cropList.sort(Comparator.comparingInt(Crop::getIndex));
        refreshCropsTable();
    }

    @FXML
    private void sortCropsDescending() {
        sound.playClick();
        cropList.sort((a, b) -> Integer.compare(b.getIndex(), a.getIndex()));
        refreshCropsTable();
    }

    @FXML
    private void sortCropsObtained() {
        sound.playClick();
        cropList.sort((a, b) -> {
            boolean aObtained = a.getStatus().equals("Obtained");
            boolean bObtained = b.getStatus().equals("Obtained");
            return Boolean.compare(bObtained, aObtained);
        });
        refreshCropsTable();
    }

    @FXML
    private void sortCropsNotObtained() {
        sound.playClick();
        cropList.sort((a, b) -> {
            boolean aObtained = a.getStatus().equals("Obtained");
            boolean bObtained = b.getStatus().equals("Obtained");
            return Boolean.compare(aObtained, bObtained);
        });
        refreshCropsTable();
    }

    private void refreshCropsTable() {
        ObservableList<Crop> data = FXCollections.observableArrayList(cropList);
        cropsTable.setItems(data);
    }

    @FXML
    private void sortHighscoresAlphabetical() {
        sound.playClick();
        highscoreList.sort(Comparator.comparing(Highscore::getPlayerName));
        refreshHighscoresTable();
    }

    @FXML
    private void sortHighscoresAscending() {
        sound.playClick();
        highscoreList.sort(Comparator.comparingInt(Highscore::getScore));
        refreshHighscoresTable();
    }

    @FXML
    private void sortHighscoresDescending() {
        sound.playClick();
        highscoreList.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));
        refreshHighscoresTable();
    }

    private void refreshHighscoresTable() {
        ObservableList<Highscore> data = FXCollections.observableArrayList(highscoreList);
        highscoresTable.setItems(data);
    }

    @FXML
    private void onBackFromCrops() {
        sound.playClick();
        showSelectionPane();
    }

    @FXML
    private void onBackFromHighscores() {
        sound.playClick();
        showSelectionPane();
    }

    @FXML
    private void onBackToMainMenu() {
        sound.playClick();
        SceneManager.getInstance().switchScene("MainMenu");
    }
}
