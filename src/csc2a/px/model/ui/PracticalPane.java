package csc2a.px.model.ui;

import csc2a.px.model.game.GameController;
import csc2a.px.model.game.GameLoop;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class PracticalPane extends BorderPane {
	private GameMenuBar gameMenu;
	private GameInfoPane gameInfoPane;
	private HowToPlayPane howToPlayePane;
	private AboutPane aboutPane;
	private GameController controller;
	private GameLoop gameLoop;
	private GameCanvas canvas;
	
	public PracticalPane() {
		
		canvas = new GameCanvas(controller);
		
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
				setCenter(howToPlayePane);
				setLeft(null);
			}
			
			@Override
			public void showAbout() {
				setCenter(aboutPane);
				setLeft(null);
			}
			
			@Override
			public void play() {
				setLeft(gameInfoPane);
				setCenter(canvas);
				gameLoop.start();
			}
		};
	}
}
