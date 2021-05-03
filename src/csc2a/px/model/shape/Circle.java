package csc2a.px.model.shape;

import csc2a.px.model.visitor.IDrawVisitor;
import javafx.scene.paint.Color;

public class Circle extends Default_Shape {
	private double r;
	
	
	public Circle(Color c, double x, double y, double r) {
		super(c, ESHAPE_TYPE.CIRCLE, x, y);
		this.r = r;
	}

	@Override
	public void draw(IDrawVisitor v, boolean hasFill) {
		v.visit(this, hasFill);
	}

	public double getR() { return r; }
}
