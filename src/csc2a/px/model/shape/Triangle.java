package csc2a.px.model.shape;

import csc2a.px.model.visitor.IDrawVisitor;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class Triangle extends Polygon {
	public Triangle(Color c, Point2D refPos, double size) {
		super(c, ESHAPE_TYPE.TRIANGLE, refPos);
		setSize(size);
		coords = new Point2D[3];
		calcCoords();
	}

	@Override
	protected void calcCoords() {
		coords[0] = new Point2D(pos.getX(), pos.getY() - size/2);
		coords[1] = new Point2D(pos.getX() - size/2, pos.getY() + size/2);		
		coords[2] = new Point2D(pos.getX() + size/2, coords[1].getY());
	}

	@Override
	public void draw(IDrawVisitor v, boolean hasFill) {
		v.visit(this, hasFill);
	}
}
