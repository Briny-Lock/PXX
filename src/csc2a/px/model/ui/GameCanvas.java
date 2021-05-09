package csc2a.px.model.ui;

import java.util.ArrayList;
import csc2a.px.model.game.GameController;
import csc2a.px.model.game.Town;
import csc2a.px.model.visitor.DrawShapesVisitor;
import javafx.beans.property.DoubleProperty;
import javafx.scene.canvas.Canvas;

public class GameCanvas extends Canvas {
	GameController controller;
	ArrayList<Town> towns;
	DrawShapesVisitor visitor;
	
	public GameCanvas() {
		setWidth(500);
		setHeight(500);
		
		visitor = new DrawShapesVisitor();
		visitor.setGc(getGraphicsContext2D(), this.getWidth(), this.getHeight());		
	}
	
	public void setController(GameController controller) {
		this.controller = controller;
	}
	
	public void bindProperties(DoubleProperty widthProperty, DoubleProperty heightProperty) {
		widthProperty.bind(widthProperty);
		heightProperty.bind(heightProperty);
	}
	
	public void redrawCanvas(GameController controller) {
		visitor.setGc(getGraphicsContext2D(), this.getWidth(), this.getHeight());
		controller.draw(visitor);
	}
}
