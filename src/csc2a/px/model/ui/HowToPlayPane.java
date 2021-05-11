package csc2a.px.model.ui;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

/**
 * @author JC Swanzen (220134523)
 * @version PXX
 *
 */
public final class HowToPlayPane extends Pane {
	public HowToPlayPane() {
		this.setStyle("-fx-background-color: mediumseagreen");
		Label description = new Label("\tMerchants is a Strategy Puzzle game set in a Medieval world, using the Abstract Factory design pattern.\r\n"
				+ "\tIn Merchants you play as an up and coming merchant in an ever expanding connection of towns. Each town sells and buys goods to and \n"
				+ "\tfrom other towns using your trade routes. Delivering these goods earns you coin which you can use to further expand your trade routes \n"
				+ "\tto accompany more and more towns that start to request your services. Be careful, however, because competition is tough and even the \n"
				+ "\tsmallest knock to your reputation can cause the towns to refer to your competitors.");
		description.setTextAlignment(TextAlignment.JUSTIFY);
		GridPane rules = new GridPane();
		VBox vbox = new VBox();
		vbox.getChildren().addAll(description, rules);

		for (int i = 0; i < 4; i++) {
			RowConstraints rows = new RowConstraints(70);
			rules.getRowConstraints().add(rows);
		}
		
		for (int i = 0; i < 2; i++) {
			RowConstraints rows = new RowConstraints(100);
			rules.getRowConstraints().add(rows);
		}
		
		ImageView ivQ = new ImageView(new Image(getClass().getResourceAsStream("/assets/Q.png")));
		ivQ.setFitWidth(64);
		ivQ.setFitHeight(64);
		ImageView ivW = new ImageView(new Image(getClass().getResourceAsStream("/assets/W.png")));
		ivW.setFitWidth(64);
		ivW.setFitHeight(64);
		ImageView ivE = new ImageView(new Image(getClass().getResourceAsStream("/assets/E.png")));
		ivE.setFitWidth(64);
		ivE.setFitHeight(64);
		ImageView ivSpace = new ImageView(new Image(getClass().getResourceAsStream("/assets/Space.png")));
		ivSpace.setFitWidth(120);
		ivSpace.setFitHeight(64);
		ImageView ivDrag = new ImageView(new Image(getClass().getResourceAsStream("/assets/drag.png")));
		ivDrag.setFitWidth(146);
		ivDrag.setFitHeight(89);
		ImageView ivLose = new ImageView(new Image(getClass().getResourceAsStream("/assets/lose.png")));
		ivLose.setFitWidth(124);
		ivLose.setFitHeight(100);
		
		
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
