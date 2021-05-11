package csc2a.px.model.ui;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;

/**
 * @author JC Swanzen (220134523)
 * @version PXX
 *
 */
public class WinPane extends Pane {
	public WinPane() {
		this.setStyle("-fx-background-color: mediumseagreen; -fx-font-size: 30pt; -fx-font-family: stencil");
		Label label = new Label("Congratulations!!\n\nYou have become\nThe Ultimate Tradesman!");
		label.setTextAlignment(TextAlignment.CENTER);
		this.getChildren().add(label);
		label.relocate(150, 175);
	}
}
