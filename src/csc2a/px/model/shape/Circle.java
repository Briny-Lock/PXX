package csc2a.px.model.shape;

import csc2a.px.model.visitor.IDrawVisitor;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class Circle extends Shape {
	private double r;
	
	
	public Circle(Color c, Point2D pos, double r) {
		super(c, ESHAPE_TYPE.CIRCLE, pos);
		this.r = r;
	}

	@Override
	public void draw(IDrawVisitor v, boolean hasFill) {
		v.visit(this, hasFill);
	}

	public double getR() { return r; }
}
