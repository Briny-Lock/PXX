package csc2a.px.model.ui;

import java.util.ArrayList;
import java.util.Random;

import csc2a.px.model.game.GameController;
import csc2a.px.model.game.GameLoop;
import csc2a.px.model.game.Map;
import csc2a.px.model.shape.Polygon;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class PracticalPane extends BorderPane {
	private static final Color DEF_C = Color.WHITE;
	private static final Color DEF_LAND_C = Color.MEDIUMSEAGREEN;
	private static final Color DEF_RIVER_C = Color.CORNFLOWERBLUE;
	private static final Color[] DEF_ROUTE_C = {Color.RED, Color.BLUE, Color.PURPLE, Color.YELLOW, Color.DEEPSKYBLUE, Color.HOTPINK};
	
	private GameMenuBar gameMenu;
	private GameInfoPane gameInfoPane;
	private HowToPlayPane howToPlayePane;
	private AboutPane aboutPane;
	private GameController controller;
	private GameLoop gameLoop;
	private GameCanvas canvas;
	
	public PracticalPane(Image carriageImage) {	
		canvas = new GameCanvas();
		controller = new GameController(DEF_C, DEF_ROUTE_C, carriageImage, canvas.getWidth(), canvas.getHeight());
		canvas.setController(controller);
		
		gameLoop = new GameLoop() {
			
			@Override
			public void tick(float deltaTime) {
				controller.update(deltaTime);
				canvas.redrawCanvas();
			}
		};

		
		gameMenu = new GameMenuBar(controller) {
			
			@Override
			public void stop() {
				gameLoop.stop();				
			}
			
			@Override
			public void showHowToPlay() {
				setLeft(null);
				setCenter(howToPlayePane);
			}
			
			@Override
			public void showAbout() {
				setLeft(null);
				setCenter(aboutPane);
			}
			
			@Override
			public void start() {
				setLeft(gameInfoPane);
				setCenter(canvas);
				gameLoop.start();
			}

			@Override
			public void randomRiver() {
				setCenter(canvas);
				Random random = new Random();
				Map map = new Map(DEF_LAND_C, DEF_RIVER_C, this.getScene().getWidth(), this.getScene().getHeight());
				ArrayList<Polygon> rivers = Map.generateRandomRivers(DEF_RIVER_C, random.nextInt(2) + 1, random.nextInt(100 - 45 + 1) + 15, this.getScene().getWidth(), this.getScene().getHeight(), 30);
				map.setRivers(rivers);
				controller.setMap(map);
				canvas.redrawCanvas();
			}
		};
		
		this.setTop(gameMenu);
	}
}
