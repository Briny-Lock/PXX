package csc2a.px.model.ui;

import csc2a.px.model.game.GameController;
import csc2a.px.model.visitor.DrawShapesVisitor;
import javafx.beans.property.DoubleProperty;
import javafx.scene.canvas.Canvas;

public class GameCanvas extends Canvas {
	private DrawShapesVisitor visitor;
	private GameController controller;
	
	public GameCanvas() {
		setWidth(500);
		setHeight(500);
		
		visitor = new DrawShapesVisitor();
		visitor.setGc(getGraphicsContext2D(), this.getWidth(), this.getHeight());
		
		setOnMouseDragged(event -> {
			
		});
		
		setOnMouseDragOver(event -> {
					
		});

		setOnMouseDragReleased(event -> {
			
		});
	}
	
	public void bindProperties(DoubleProperty widthProperty, DoubleProperty heightProperty) {
		widthProperty.bind(widthProperty);
		heightProperty.bind(heightProperty);
	}
	
	public void redrawCanvas() {
		visitor.setGc(getGraphicsContext2D(), this.getWidth(), this.getHeight());
		controller.draw(visitor);
	}
	
	public void setController(GameController controller) { this.controller = controller; }
}
