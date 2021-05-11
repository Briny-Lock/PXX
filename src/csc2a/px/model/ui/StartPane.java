package csc2a.px.model.ui;


import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * @author JC Swanzen (220134523)
 * @version PXX
 * Start menu
 */
public class StartPane extends Pane {
	private Label title;
	private Label info;
	public StartPane(Image image, double w, double h) {
		this.setWidth(w);
		this.setHeight(h);
		ImageView iv = new ImageView(image);
		iv.setFitWidth(w);
		iv.setFitHeight(h);
		title = new Label("Merchants");
		title.setStyle("-fx-font-weight: bold; -fx-font-size: 20pt; -fx-font-family: stencil");
		info = new Label("Select a map from File, then go to Game and click 'Start'\n"
				+ "Refer to the 'How To Play' section under Game for additional information");
		info.setStyle("-fx-font-weight: bold; -fx-font-size: 15pt;");
		
		this.getChildren().add(iv);
		this.getChildren().addAll(title, info);	

		title.relocate(w/2 - title.getWidth() - 80, h/5 - 100);
		info.relocate(w/2 - info.getWidth() - 300, 3 * h/4);
	}
}
