package csc2a.px.model.ui;

import java.io.File;
import java.util.Optional;
import csc2a.px.model.file.MapFileHandler;
import csc2a.px.model.game.Map;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public abstract class GameMenuBar extends MenuBar {	
	public GameMenuBar() {
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File("data"));
		
		Menu fileMenu = new Menu("_File");
		
		MenuItem openMapFile = new MenuItem("_Open Map File");
		openMapFile.setAccelerator(KeyCombination.keyCombination("shortcut+o"));
		openMapFile.setOnAction(event -> {
			fileChooser.setTitle("Select a map file to open");
			fileChooser.getExtensionFilters().add(new ExtensionFilter("Binary Map", "*.bmap"));
			File fileToOpen = fileChooser.showOpenDialog(this.getScene().getWindow());
			if (fileToOpen != null) {
				Map map = MapFileHandler.readMapFromFile(fileToOpen);
				if (map != null) {
					addMap(map);
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
		saveMapFile.setAccelerator(KeyCombination.keyCombination("shortcut+s"));
		saveMapFile.setOnAction(event -> {
			fileChooser.setTitle("Save Map");
			fileChooser.getExtensionFilters().add(new ExtensionFilter("Binary Map", "*.bmap"));
			System.out.println(this.getScene().getWindow());
			File fileToSave = fileChooser.showSaveDialog(this.getScene().getWindow());
			if (fileToSave != null) {
				if (!MapFileHandler.saveMapToFile(getMap(), fileToSave)) {
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
		randomizeMap.setAccelerator(KeyCombination.keyCombination("shortcut+r"));
		randomizeMap.setOnAction(event -> {
			randomRiver();
		});
		MenuItem exitItem = new MenuItem("E_xit"); 
		exitItem.setAccelerator(KeyCombination.keyCombination("shortcut+x"));
		exitItem.setOnAction(event -> {
			Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
			confirmAlert.setHeaderText("Exit Application");
			confirmAlert.setContentText("Are you sure you want to exit?");
			Optional<ButtonType> response = confirmAlert.showAndWait();;
			if (response.get() == ButtonType.OK) {
				Platform.exit();
			}			
		});
		
		fileMenu.getItems().addAll(openMapFile, saveMapFile, randomizeMap, new SeparatorMenuItem(), exitItem);
		
		Menu gameMenu = new Menu("_Game");
		MenuItem play = new MenuItem("_Play");
		play.setAccelerator(KeyCombination.keyCombination("shortcut+p"));
		play.setOnAction(event -> {
			start();
		});
		MenuItem stop = new MenuItem("S_top");
		stop.setAccelerator(KeyCombination.keyCombination("shortcut+t"));
		stop.setOnAction(event -> {
			stop();
		});
		MenuItem howToPlay = new MenuItem("_How To Play");
		howToPlay.setAccelerator(KeyCombination.keyCombination("shortcut+h"));
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
		
		this.getMenus().addAll(fileMenu, gameMenu, infoMenu);
	}

	public abstract void start();
	public abstract void stop();
	public abstract void showHowToPlay();
	public abstract void showAbout();
	public abstract void addMap(Map map);
	public abstract void randomRiver();
	public abstract Map getMap();
}
