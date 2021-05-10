package csc2a.px.model.shape;

import csc2a.px.model.visitor.IDrawVisitor;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

/**
 * @author JC Swanzen (220134523)
 * @version PXX
 *
 */
public class Rectangle extends Shape {
	private double w;
	private double h;

	public Rectangle(Color c, Point2D pos, double w, double h) {
		super(c, ESHAPE_TYPE.RECTANGLE, pos);
		this.w = w;
		this.h = h;	
	}

	@Override
	public void draw(IDrawVisitor v, boolean hasFill) {
		v.visit(this, hasFill);
	}

	public double getW() { return w; }
	public double getH() { return h; }

	@Override
	public void setSize(double size) {
		super.setSize(size);
		w = size;
		h = size;
	}
}
