package csc2a.px.model.ui;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * @author JC Swanzen (220134523)
 * @version PXX
 *
 */
public final class HowToPlayPane extends Pane {
	public HowToPlayPane() {
		Label discription = new Label("Merchants is a Strategy Puzzle game set in a Medieval world, using the Abstract Factory design pattern.\r\n"
				+ "In Merchants you play as an up and coming merchant in an ever expanding connection of towns. Each town sells and buys goods to and "
				+ "from other towns using your trade routes. Delivering these goods earns you coin which you can use to further expand your trade routes "
				+ "to accompany more and more towns that start to request your services. Be careful, however, because competition is tough and even the "
				+ "smallest knock to your reputation can cause the towns to refer to your competitors.");
		GridPane rules = new GridPane();
		VBox vbox = new VBox();
		vbox.getChildren().addAll(discription, rules);

		ImageView ivQ = new ImageView(new Image(getClass().getResourceAsStream("assets/Q.png")));
		ImageView ivW = new ImageView(new Image(getClass().getResourceAsStream("assets/W.png")));
		ImageView ivE = new ImageView(new Image(getClass().getResourceAsStream("assets/E.png")));
		ImageView ivSpace = new ImageView(new Image(getClass().getResourceAsStream("assets/Space.png")));
		ImageView ivDrag = new ImageView(new Image(getClass().getResourceAsStream("assets/drag.png")));
		ImageView ivLose = new ImageView(new Image(getClass().getResourceAsStream("assets/lose.png")));
		
		
		Label lbQ = new Label("Press Q to buy a new route");
		Label lbW = new Label("Press W to buy a new wagon");
		Label lbE = new Label("Press E to add a carriage to a wagon");
		Label lbSpace = new Label("Press Space to pause the game");
		Label lbDrag = new Label("Drag fom one town to another to link them to your route, or Right Click to remove them");
		Label lbLose = new Label("Don't let goods pile up! The more red icons appear, the quicker your reputation will fall!");
		
		rules.addRow(0, ivQ, lbQ);
		rules.addRow(1, ivW, lbW);
		rules.addRow(2, ivE, lbE);
		rules.addRow(3, ivSpace, lbSpace);
		rules.addRow(4, ivDrag, lbDrag);
		rules.addRow(5, ivLose, lbLose);
		
		this.getChildren().add(vbox);
	}
	
}
