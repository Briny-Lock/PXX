package csc2a.px.model.shape;

import csc2a.px.model.visitor.IDrawVisitor;
import javafx.scene.paint.Color;

public class Rectangle extends Default_Shape {
	private double w;
	private double h;

	public Rectangle(Color c, double x, double y, double w, double h) {
		super(c, ESHAPE_TYPE.RECTANGLE, x, y);
		this.w = w;
		this.h = h;	
	}

	@Override
	public void draw(IDrawVisitor v, boolean hasFill) {
		v.visit(this, hasFill);
	}

	public double getW() { return w; }
	public double getH() { return h; }

}
