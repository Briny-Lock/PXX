import java.util.ArrayList;
import java.util.Arrays;

import csc2a.px.model.game.GameController;
import csc2a.px.model.game.GameLoop;
import csc2a.px.model.game.Map;
import csc2a.px.model.game.Route;
import csc2a.px.model.game.Town;
import csc2a.px.model.game.Wagon;
import csc2a.px.model.shape.Circle;
import csc2a.px.model.shape.Line;
import csc2a.px.model.shape.Rectangle;
import csc2a.px.model.shape.Shape;
import csc2a.px.model.shape.Triangle;
import csc2a.px.model.ui.GameCanvas;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application{

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		GameCanvas canvas = new GameCanvas();
		Group root = new Group();
		BorderPane pane = new BorderPane();
		pane.setCenter(canvas);
		root.getChildren().add(canvas);
		Scene scene = new Scene(root, 500, 500);
		scene.setFill(Color.web("#2f2f2f"));
		
		
		primaryStage.setScene(scene);
		primaryStage.show();
		
		Image carriageImage = new Image(getClass().getResourceAsStream(("assets/carriage.png")));
		Map map = new Map(Color.DARKSEAGREEN, Color.CORNFLOWERBLUE, scene.getWidth(), scene.getHeight());
		map.generateRandomMap(1, 60);
		GameController controller = new GameController(map, carriageImage);
		
		GameLoop loop = new GameLoop() {
			
			@Override
			public void tick(float deltaTime) {
				controller.update(deltaTime);
				canvas.redrawCanvas(controller);
			}
		};
		
		loop.start();
		
	}

	
}
