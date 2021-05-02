package csc2a.px.model.shape;

import javafx.scene.paint.Color;

public abstract class Polygon extends Shape {

	protected double[] xCoords;
	protected double[] yCoords;	

	public Polygon(Color c, ESHAPE_TYPE type) {
		super(c, type);
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
}
