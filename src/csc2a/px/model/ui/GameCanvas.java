package csc2a.px.model.ui;

import java.util.ArrayList;

import csc2a.px.model.game.Wagon;
import csc2a.px.model.shape.Shape;
import csc2a.px.model.visitor.DrawShapesVisitor;
import javafx.scene.canvas.Canvas;

public class GameCanvas extends Canvas {
	ArrayList<Shape> shapes;
	Wagon wagon;
	DrawShapesVisitor visitor;
	
	public GameCanvas() {
		setWidth(500);
		setHeight(500);
		
		visitor = new DrawShapesVisitor();
		visitor.setGc(getGraphicsContext2D());
	}
	
	public void setShapes(ArrayList<Shape> shapes) {
		this.shapes = shapes;
	}
	
	public void addWagon(Wagon wagon) {
		this.wagon = wagon;
	}
	
	public void redrawCanvas(float deltaTime) {
		getGraphicsContext2D().clearRect(0, 0, this.getWidth(), this.getHeight());
		visitor.setGc(getGraphicsContext2D());
		wagon.move(deltaTime);
		wagon.drawCarriages(visitor, true);
		
	}
}
