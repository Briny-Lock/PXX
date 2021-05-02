package csc2a.px.model.shape;

import csc2a.px.model.visitor.IDrawVisitor;
import javafx.scene.paint.Color;

public class Line extends Polygon {
	private boolean bridge = false;
	private boolean guardPost = false;
	
	public Line(Color c, ESHAPE_TYPE type, double startX, double startY, double destX, double destY) {
		super(c, type);
		xCoords = new double[3];
		yCoords = new double[3];
		xCoords[0] = startX;
		yCoords[0] = startY;
		xCoords[2] = destX;
		yCoords[2] = destY;
	}	
	
	@Override
	protected void calcCoords() {
		//TODO calculate midpoint
		xCoords[1] = xCoords[0];
		yCoords[1] = yCoords[0];
	}

	@Override
	public void draw(IDrawVisitor v) {
		v.visit(this);
	}

}
