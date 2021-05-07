package csc2a.px.model.shape;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public abstract class Polygon extends Shape {
	protected Point2D[] coords;	

	public Polygon(Color c, ESHAPE_TYPE type, Point2D refPos) {
		super(c, type, refPos);
	}


	protected abstract void calcCoords();
	
	/**
	 * @return the xCoords
	 */
	public double[] getxCoords() {
		double[] xCoords = new double[coords.length];
		for (int i = 0; i < coords.length; i++) {
			xCoords[i] = coords[i].getX();			
		}
		return xCoords;
	}

	/**
	 * @return the yCoords
	 */
	public double[] getyCoords() {
		double[] yCoords = new double[coords.length];
		for (int i = 0; i < coords.length; i++) {
			yCoords[i] = coords[i].getY();			
		}
		return yCoords;
	}
	
	public Point2D[] getCoords() {
		return coords;
	}
}
