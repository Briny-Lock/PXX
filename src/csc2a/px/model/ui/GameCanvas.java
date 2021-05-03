package csc2a.px.model.ui;

import java.util.ArrayList;

import csc2a.px.model.shape.Shape;
import csc2a.px.model.visitor.DrawShapesVisitor;
import javafx.scene.canvas.Canvas;

public class GameCanvas extends Canvas {
	ArrayList<Shape> shapes;
	DrawShapesVisitor visitor;
	
	public GameCanvas() {
		setWidth(500);
		setHeight(500);
		
		visitor = new DrawShapesVisitor();
		visitor.setGc(getGraphicsContext2D());
	}
	
	public void setShapes(ArrayList<Shape> shapes) {
		this.shapes = shapes;
		redrawCanvas();
	}
	
	public void redrawCanvas() {
		visitor.setGc(getGraphicsContext2D());
		for (Shape shape : shapes) {
			shape.draw(visitor, false);
		}
	}
}
