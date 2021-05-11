import csc2a.px.model.ui.PaneHandler;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * @author JC Swanzen (220134523)
 * @version PXX
 *
 */
public class Main extends Application{

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Image carImage = new Image(getClass().getResourceAsStream("/assets/carriage.png"));
		Image coinImage = new Image(getClass().getResourceAsStream("/assets/coin_icon.png"));
		Image startImage = new Image(getClass().getResourceAsStream("/assets/start.png"));
		PaneHandler root = new PaneHandler(carImage, startImage);
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
