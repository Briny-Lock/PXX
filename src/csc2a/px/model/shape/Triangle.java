package csc2a.px.model.shape;

import csc2a.px.model.visitor.IDrawVisitor;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class Triangle extends Polygon {
	private double size;

	public Triangle(Color c, Point2D refPos, double size) {
		super(c, ESHAPE_TYPE.TRIANGLE, refPos);

		this.size = size;
		xCoords = new double[3];
		yCoords = new double[3];
		calcCoords();
	}

	@Override
	protected void calcCoords() {
		xCoords[0] = refPos.getX();
		yCoords[0] = refPos.getY() - size/2;
		xCoords[1] = xCoords[0] - size/2;
		yCoords[1] = yCoords[0] + size;
		
		xCoords[2] = xCoords[0] + size/2;
		yCoords[2] = yCoords[1];
	}

	@Override
	public void draw(IDrawVisitor v, boolean hasFill) {
		v.visit(this, hasFill);
	}

}
