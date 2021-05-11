package csc2a.px.model.ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author JC Swanzen (220134523)
 * @version PXX
 *
 */
public class LosePane extends ImageView {
	public LosePane() {
		this.setImage(new Image(getClass().getResourceAsStream("/assets/game_lose.png")));
	}
}
