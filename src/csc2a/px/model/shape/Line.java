package csc2a.px.model.shape;

import csc2a.px.model.visitor.IDrawVisitor;
import javafx.scene.paint.Color;

public class Line extends Polygon {
	private boolean hasGuardPost = false;
	private Color bridgeColor = Color.BROWN;
	private double[] bridgeXCoords = null;
	private double[] bridgeYCoords = null;
	
	public Line(Color c, double startX, double startY, double destX, double destY) {
		super(c, ESHAPE_TYPE.LINE);
		xCoords = new double[3];
		yCoords = new double[3];
		xCoords[0] = startX;
		yCoords[0] = startY;
		xCoords[2] = destX;
		yCoords[2] = destY;
		calcCoords();
	}
	
	public Line(Color c, double startX, double startY, double destX, double destY, double[] bridgeXCoords, double[] bridgeYCoords) {
		super(c, ESHAPE_TYPE.LINE);
		xCoords = new double[3];
		yCoords = new double[3];
		xCoords[0] = startX;
		yCoords[0] = startY;
		xCoords[2] = destX;
		yCoords[2] = destY;
		this.bridgeXCoords = bridgeXCoords;
		this.bridgeYCoords = bridgeYCoords;
		calcCoords();
	}
	
	public void addGuardPost() { hasGuardPost = true; }
	
	@Override
	protected void calcCoords() {
		//TODO calculate midpoint
		xCoords[1] = xCoords[0];
		yCoords[1] = yCoords[0];
	}

	@Override
	public void draw(IDrawVisitor v, boolean hasFill) {
		v.visit(this, hasFill);
	}
	
	public double[] getBridgeXCoords() { return bridgeXCoords; }
	public double[] getBridgeYCoords() { return bridgeYCoords; }
	public Color getBridgeColor() { return bridgeColor; }
	public boolean hasGuardPost() { return hasGuardPost; }

}
