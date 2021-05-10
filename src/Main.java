import csc2a.px.model.ui.PracticalPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application{

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Image carImage = new Image(getClass().getResourceAsStream("assets/carriage.png"));
		Image coinImage = new Image(getClass().getResourceAsStream("assets/coinIcon.png"));
		Image startImage = new Image(getClass().getResourceAsStream("assets/start.png"));
		PracticalPane root = new PracticalPane(carImage, startImage);
		Scene scene = new Scene(root, 800, 600);
		root.setKeyHandler(scene);
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("Merchants");
		primaryStage.getIcons().add(coinImage);
		root.setPrefSize(primaryStage.getWidth(), primaryStage.getHeight());
		primaryStage.setMinWidth(800);
		primaryStage.setMinHeight(600);
		primaryStage.setMaxWidth(800);
		primaryStage.setMaxHeight(600);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	
}
