package csc2a.px.model.ui;


import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class StartPane extends StackPane {
	public StartPane(Image image, double w, double h) {
		ImageView iv = new ImageView(image);
		iv.setFitWidth(w);
		iv.setFitHeight(h);
		Label title = new Label("Merchants");
		title.setStyle("-fx-font-weight: bold; -fx-font-size: 20pt;");
		title.setLayoutX(w/2 - title.getWidth());
		title.setLayoutY(h/3);
		Label info = new Label("Select a map from File, then go to Game and click 'Start'\n"
				+ "Refer to the 'How To Play' section under Game for additional information");
		info.setStyle("-fx-font-weight: bold; -fx-font-size: 15pt;");
		title.setLayoutX(w/2 - title.getWidth());
		title.setLayoutY(2 * h/3);
		
		this.getChildren().add(iv);
		this.getChildren().addAll(title, info);		
	}
}
