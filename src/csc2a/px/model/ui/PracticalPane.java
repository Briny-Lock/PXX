package csc2a.px.model.ui;

import java.util.Random;

import csc2a.px.model.game.GameController;
import csc2a.px.model.game.GameLoop;
import csc2a.px.model.game.Map;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
	private StartPane startPane;
	public PracticalPane(Image carriageImage, Image startImage) {
		startPane = new StartPane(startImage, 800, 600);
		setMinSize(800, 600);
		setPrefSize(800, 600);
		setMaxSize(800, 600);
		setWidth(800);
		setHeight(600);
		this.setCenter(startPane);
		canvas = new GameCanvas();

		gameInfoPane = new GameInfoPane();
		controller = new GameController(DEF_C, DEF_ROUTE_C, carriageImage);
		canvas.setController(controller);
		
		gameLoop = new GameLoop() {

			@Override
			public void tick(float deltaTime) {
				controller.update(deltaTime);
				gameInfoPane.updateInfo(controller);
				canvas.redrawCanvas();
			}
		};

		gameMenu = new GameMenuBar() {

			@Override
			public void stop() {
				canvas.pause();
				gameLoop.stop();
			}

			@Override
			public void showHowToPlay() {
				setRight(null);
				setCenter(howToPlayePane);
			}

			@Override
			public void showAbout() {
				setRight(null);
				setCenter(aboutPane);
			}

			@Override
			public void start() {
				canvas.clear();
				if (controller.getMap() == null) {
					Alert errorAlert = new Alert(AlertType.WARNING);
					errorAlert.setHeaderText("No map specified");
					errorAlert.setContentText("Please generate or open a map file to continue");
					errorAlert.showAndWait();
				} else {
					setRight(gameInfoPane);
					canvas.setWidth(PracticalPane.this.getWidth() - gameInfoPane.getWidth());
					canvas.setHeight(PracticalPane.this.getHeight() - gameMenu.getHeight());
					setCenter(canvas);;
					canvas.play();
					gameLoop.start();
				}
			}

			@Override
			public void randomRiver() {
				setRight(gameInfoPane);
				canvas.setWidth(PracticalPane.this.getWidth() - gameInfoPane.getWidth());
				canvas.setHeight(PracticalPane.this.getHeight() - gameMenu.getHeight());
				setCenter(canvas);
				Random random = new Random();
				Map map = new Map(DEF_LAND_C, DEF_RIVER_C, canvas.getWidth(), 
						canvas.getHeight());
				map.generateRandomRivers(random.nextInt(2) + 1, random.nextInt(100 - 45 + 1) + 15, 30);
				controller.setMap(map);
				canvas.redrawCanvas();
			}

			@Override
			public void addMap(Map map) {
				canvas.clear();
				controller.setMap(map);
				setLeft(null);
				setCenter(canvas);
				canvas.redrawCanvas();
			}

			@Override
			public Map getMap() {
				return controller.getMap();
			}
		};

		this.setTop(gameMenu);
	}

	public void setKeyHandler(Scene scene) {
		setOnKeyPressed(event -> {
			switch (event.getCode()) {
			case SPACE:
				if (gameLoop.isPaused()) {
					canvas.play();
					gameLoop.play();
				}
				else {
					canvas.pause();
					gameLoop.pause();
				}
				break;
			default:
				break;
			}
		});
	}
}
