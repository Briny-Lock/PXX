package csc2a.px.model.shape;

import javafx.scene.paint.Color;

public abstract class Default_Shape extends Shape {
	protected double x;
	protected double y;
	
	public Default_Shape(Color c, ESHAPE_TYPE type, double x, double y) {
		super(c, type);
		this.x = x;
		this.y = y;
	}
	
	public double getX() { return x; }
	public double getY() { return y; }
}
