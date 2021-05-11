package csc2a.px.model.ui;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;

/**
 * @author JC Swanzen (220134523)
 * @version PXX
 *
 */
public class AboutPane extends Pane {
	public AboutPane() {
		this.setStyle("-fx-background-color: mediumseagreen; -fx-font-size: 20pt; -fx-font-family: stencil");
		Label label = new Label("JC Swanzen (220134523)\n\nAll images used were self-created\n\nThank you for playing!");
		label.setTextAlignment(TextAlignment.CENTER);
		this.getChildren().add(label);
		label.relocate(175, 175);
	}
	
}
