package csc2a.px.model.shape;

import csc2a.px.model.visitor.IDrawVisitor;
import javafx.scene.paint.Color;

public class Triangle extends Polygon {
	private double size;

	public Triangle(Color c, double centX, double centY, double size) {
		super(c, ESHAPE_TYPE.TRIANGLE);

		this.size = size;
		xCoords = new double[3];
		yCoords = new double[3];
		xCoords[0] = centX;
		yCoords[0] = centY - size/2;
		calcCoords();
	}

	@Override
	protected void calcCoords() {
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
