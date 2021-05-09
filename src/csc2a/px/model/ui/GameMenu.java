package csc2a.px.model.ui;

import java.io.File;

import csc2a.px.model.file.MapFileHandler;
import csc2a.px.model.game.GameController;
import csc2a.px.model.game.Map;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class GameMenu extends MenuBar {
	
	public GameMenu(GameController controller) {
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File("data"));
		
		Menu fileMenu = new Menu("_File");
		
		MenuItem openMapFile = new MenuItem("_Open Map File");
		openMapFile.setOnAction(event -> {
			fileChooser.setTitle("Select a map file to open");
			fileChooser.getExtensionFilters().add(new ExtensionFilter("Map", "*.map"));
			File fileToOpen = fileChooser.showOpenDialog(this.getScene().getWindow());
			if (fileChooser != null) {
				Map map = MapFileHandler.readMapFromFile(fileToOpen);
				if (map != null) {
					controller.setMap(map);
				}
			}
		});
		MenuItem saveMapFile = new MenuItem("_Save Map File");
		saveMapFile.setOnAction(event -> {
			fileChooser.setTitle("Save Map");
			fileChooser.getExtensionFilters().add(new ExtensionFilter("Map", "*.map"));
			File fileToSave = fileChooser.showOpenDialog(this.getScene().getWindow());
			if (fileToSave != null) {
				if (MapFileHandler.saveMapToFile(controller.getMap(), fileToSave));
			}
		});
		MenuItem randomizeMap = new MenuItem("Genereate _Random Map");
		randomizeMap.setOnAction(event -> {
			
		});
		MenuItem exitItem = new MenuItem("E_xit"); 
		exitItem.setOnAction(event -> {
			
		});
		
		fileMenu.getItems().addAll(openMapFile, saveMapFile, randomizeMap, new SeparatorMenuItem());
		
		Menu infoMenu = new Menu("_Info");
		MenuItem howToPlay = new MenuItem("_How To Play");
		howToPlay.setOnAction(event -> {
			
		});
		MenuItem aboutItem = new MenuItem("_About");
		howToPlay.setOnAction(event -> {
			
		});
		
		infoMenu.getItems().addAll(howToPlay, new SeparatorMenuItem(), aboutItem);
		
		
		
		
		this.getMenus().addAll(fileMenu, infoMenu);
	}
}
