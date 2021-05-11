package csc2a.px.model.ui;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;

/**
 * @author JC Swanzen (220134523)
 * @version PXX
 *
 */
public class LosePane extends Pane {
	public LosePane() {
		this.setStyle("-fx-background-color: mediumseagreen; -fx-font-size: 40pt; -fx-font-family: stencil");
		Label label = new Label("Game Over!!!");
		label.setTextAlignment(TextAlignment.CENTER);
		this.getChildren().add(label);
		label.relocate(200, 200);
	}
}
