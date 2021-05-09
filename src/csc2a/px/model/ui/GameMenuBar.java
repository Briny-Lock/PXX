package csc2a.px.model.ui;

import java.io.File;
import java.util.Optional;
import java.util.Random;

import csc2a.px.model.file.MapFileHandler;
import csc2a.px.model.game.GameController;
import csc2a.px.model.game.Map;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public abstract class GameMenuBar extends MenuBar {
	
	public GameMenuBar(GameController controller) {		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File("data"));
		
		Menu fileMenu = new Menu("_File");
		
		MenuItem openMapFile = new MenuItem("_Open Map File");
		openMapFile.setOnAction(event -> {
			fileChooser.setTitle("Select a map file to open");
			fileChooser.getExtensionFilters().add(new ExtensionFilter("Map", "*.map"));
			File fileToOpen = fileChooser.showOpenDialog(this.getScene().getWindow());
			if (fileToOpen != null) {
				Map map = MapFileHandler.readMapFromFile(fileToOpen);
				if (map != null) {
					controller.setMap(map);
				} else {
					Alert errorAlert = new Alert(AlertType.ERROR);
					errorAlert.setHeaderText("Invalid file format");
					errorAlert.setContentText("The file you selected was of the wrong format");
					errorAlert.showAndWait();
				}
			} else {
				Alert errorAlert = new Alert(AlertType.ERROR);
				errorAlert.setHeaderText("File not found");
				errorAlert.setContentText("File could not be found.");
				errorAlert.showAndWait();
			}
		});
		MenuItem saveMapFile = new MenuItem("_Save Map File");
		saveMapFile.setOnAction(event -> {
			fileChooser.setTitle("Save Map");
			fileChooser.getExtensionFilters().add(new ExtensionFilter("Map", "*.map"));
			File fileToSave = fileChooser.showOpenDialog(this.getScene().getWindow());
			if (fileToSave != null) {
				if (!MapFileHandler.saveMapToFile(controller.getMap(), fileToSave)) {
					
				} else {
					Alert errorAlert = new Alert(AlertType.ERROR);
					errorAlert.setHeaderText("Error saving file");
					errorAlert.setContentText("An unexpected error occured causing the file to not save properly");
					errorAlert.showAndWait();
				}
			} else {
				Alert errorAlert = new Alert(AlertType.ERROR);
				errorAlert.setHeaderText("File not specified");
				errorAlert.setContentText("File was not specified.");
				errorAlert.showAndWait();
			}
		});
		MenuItem randomizeMap = new MenuItem("Genereate _Random Map");
		randomizeMap.setOnAction(event -> {
			Random random = new Random();
			controller.getMap().generateRandomMap(random.nextInt(2) + 1, random.nextInt(45 - 15 + 1) + 15);
		});
		MenuItem exitItem = new MenuItem("E_xit"); 
		exitItem.setOnAction(event -> {
			Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
			confirmAlert.setHeaderText("Exit Application");
			confirmAlert.setContentText("Are you sure you want to exit?");
			Optional<ButtonType> response = confirmAlert.showAndWait();;
			if (response.get() == ButtonType.YES) {
				Platform.exit();
			}
			
		});
		
		fileMenu.getItems().addAll(openMapFile, saveMapFile, randomizeMap, new SeparatorMenuItem(), exitItem);
		
		Menu gameMenu = new Menu("_Game");
		MenuItem play = new MenuItem("_Play");
		play.setOnAction(event -> {
			play();
		});
		MenuItem stop = new MenuItem("_Play");
		stop.setOnAction(event -> {
			stop();
		});
		MenuItem howToPlay = new MenuItem("_How To Play");
		howToPlay.setOnAction(event -> {
			showHowToPlay();
		});
		
		gameMenu.getItems().addAll(play, stop, new SeparatorMenuItem(), howToPlay);
		
		Menu infoMenu = new Menu("_Info");
		MenuItem aboutItem = new MenuItem("_About");
		aboutItem.setOnAction(event -> {
			showAbout();
		});
		
		infoMenu.getItems().addAll(aboutItem);
		
		this.getMenus().addAll(fileMenu, infoMenu);
	}

	public abstract void play();
	public abstract void stop();
	public abstract void showHowToPlay();
	public abstract void showAbout();
}
