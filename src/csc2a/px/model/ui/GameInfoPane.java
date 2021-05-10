package csc2a.px.model.ui;

import csc2a.px.model.game.GameController;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

/**
 * @author JC Swanzen (220134523)
 * @version PXX
 *
 */
public class GameInfoPane extends GridPane {
	private Label reputationLabel;
	private Label coinLabel;
	private Label routeCostLabel;
	private Label bridgeCostLabel;
	private Label wagonCostLabel;
	private Label carriageCostLabel;
	private Label coinPerDeliveryLabel;
	
	private Label reputation;
	private Label coin;
	private Label routeCost;
	private Label bridgeCost;
	private Label wagonCost;
	private Label carriageCost;
	private Label coinPerDelivery;
	
	public GameInfoPane() {
		ColumnConstraints column1 = new ColumnConstraints();
	    column1.setPercentWidth(70);
	    ColumnConstraints column2 = new ColumnConstraints();
	    column2.setPercentWidth(30);
	    
	    this.getColumnConstraints().addAll(column1, column2);
	    this.setMinWidth(160);
	    this.setPrefWidth(160);
	    this.setMaxWidth(160);
	    this.setWidth(160);
		reputationLabel = new Label("Reputation:");
		coinLabel = new Label("Coin:");
		routeCostLabel = new Label("Route Cost:");
		bridgeCostLabel = new Label("Bridge Cost:");
		wagonCostLabel = new Label("Wagon Cost:");
		carriageCostLabel = new Label("Carriage Cost:");
		coinPerDeliveryLabel = new Label("Coin Per Delivery:\t");
		
		reputation= new Label();
		coin= new Label();
		routeCost= new Label();
		bridgeCost= new Label();
		wagonCost= new Label();
		carriageCost= new Label();
		coinPerDelivery = new Label();
		
		this.addRow(0, reputationLabel, reputation);
		this.addRow(1, coinLabel, coin);
		this.addRow(2, routeCostLabel, routeCost);
		this.addRow(3, bridgeCostLabel, bridgeCost);
		this.addRow(4, wagonCostLabel, wagonCost);
		this.addRow(5, carriageCostLabel, carriageCost);
		this.addRow(6, coinPerDeliveryLabel, coinPerDelivery);
	}
	
	public void updateInfo(GameController controller) {
		reputation.setText(Integer.toString(controller.getReputation()));
		coin.setText(Integer.toString(controller.getCoin()));
		routeCost.setText(Integer.toString(controller.getRouteCost()));
		bridgeCost.setText(Integer.toString(controller.getBridgeCost()));
		wagonCost.setText(Integer.toString(controller.getWagonCost()));
		carriageCost.setText(Integer.toString(controller.getCarriageCost()));
		coinPerDelivery.setText(Integer.toString(controller.getCoinPerDelivery()));
	}
}
