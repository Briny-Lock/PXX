package csc2a.px.model.shape;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public abstract class Polygon extends Shape {
	protected Point2D refPos;
	protected double[] xCoords;
	protected double[] yCoords;	

	public Polygon(Color c, ESHAPE_TYPE type, Point2D refPos) {
		super(c, type, refPos);
	}


	protected abstract void calcCoords();
	
	/**
	 * @return the xCoords
	 */
	public double[] getxCoords() {
		return xCoords;
	}

	/**
	 * @return the yCoords
	 */
	public double[] getyCoords() {
		return yCoords;
	}
	
	public Point2D getPos() { return refPos; }
	
	public void setPos(Point2D pos) { 
		refPos = pos;
		calcCoords();
	}
}
