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
		Image coinImage = new Image(getClass().getResourceAsStream("assets/Coin 64x64.png"));
		PracticalPane root = new PracticalPane(carImage);
		Scene scene = new Scene(root, 800, 600);
		root.setKeyHandler(scene);
		
		primaryStage.setScene(scene);
		primaryStage.setFullScreen(true);
		primaryStage.setTitle("Merchants");
		primaryStage.getIcons().add(coinImage);
		root.setPrefSize(primaryStage.getWidth(), primaryStage.getHeight());
		primaryStage.setMinWidth(800);
		primaryStage.setMinHeight(600);
		primaryStage.widthProperty().addListener(e -> {
			root.setNewWidth(primaryStage.getWidth());
		});
		primaryStage.heightProperty().addListener(e -> {
			root.setNewHeight(primaryStage.getHeight());
		});
		primaryStage.show();
	}

	
}
