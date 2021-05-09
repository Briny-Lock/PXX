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
import csc2a.px.model.ui.PracticalPane;
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
		Image carImage = new Image(getClass().getResourceAsStream("assets/carriage.png"));
		PracticalPane root = new PracticalPane(carImage);
		Scene scene = new Scene(root, 500, 500);
		scene.setFill(Color.web("#2f2f2f"));
		
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}

	
}
