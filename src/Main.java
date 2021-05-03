import java.util.ArrayList;

import csc2a.px.model.shape.Circle;
import csc2a.px.model.shape.Line;
import csc2a.px.model.shape.Rectangle;
import csc2a.px.model.shape.Shape;
import csc2a.px.model.shape.Triangle;
import csc2a.px.model.ui.GameCanvas;
import javafx.application.Application;
import javafx.scene.Scene;
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
		StackPane root = new StackPane();
		root.getChildren().add(canvas);
		Scene scene = new Scene(root, 500, 500);
		scene.setFill(Color.web("#2f2f2f"));
			
		primaryStage.setScene(scene);
		primaryStage.show();
		
		
		
		ArrayList<Shape> shapes = demoShapes();
		
		canvas.setShapes(shapes);
	}

	public ArrayList<Shape> demoShapes() {
		Line l1 = new Line(Color.RED, 25, 30, 345, 250);
		double[] bridgeXCoord = {200, 280};
		double[] bridgeYCoord = {40, 40};
		Line l2 = new Line(Color.BLUE, 40, 40, 410, 40, bridgeXCoord, bridgeYCoord);
		Circle c1 = new Circle(Color.GRAY, 300, 300, 10); 
		Rectangle r1 = new Rectangle(Color.BLUEVIOLET, 25, 65, 20, 20);
		Triangle t1 = new Triangle(Color.YELLOW, 250, 80, 20);
		ArrayList<Shape> shapes = new ArrayList<>();
		shapes.add(l1);
		shapes.add(l2);
		shapes.add(c1);
		shapes.add(r1);
		shapes.add(t1);
		return shapes;
	}
	
}
