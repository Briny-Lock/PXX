package csc2a.px.model.shape;

import csc2a.px.model.visitor.IDrawVisitor;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class Line extends Polygon {
	private boolean hasGuardPost = false;
	private Color bridgeColor = Color.BROWN;
	private double[] bridgeXCoords = null;
	private double[] bridgeYCoords = null;
	private Point2D dest;
	
	public Line(Color c, Point2D start, Point2D dest) {
		super(c, ESHAPE_TYPE.LINE, start);
		xCoords = new double[3];
		yCoords = new double[3];
		this.dest = dest;
		calcCoords();
	}
	
	public Line(Color c, Point2D start, Point2D dest, double[] bridgeXCoords, double[] bridgeYCoords) {
		super(c, ESHAPE_TYPE.LINE, start);
		xCoords = new double[3];
		yCoords = new double[3];
		this.bridgeXCoords = bridgeXCoords;
		this.bridgeYCoords = bridgeYCoords;
		calcCoords();
	}
	
	public void addGuardPost() { hasGuardPost = true; }
	
	@Override
	protected void calcCoords() {
		xCoords[0] = refPos.getX();
		yCoords[0] = refPos.getY();
		xCoords[2] = dest.getX();
		yCoords[2] = dest.getY();
		//TODO calculate midpoint
		xCoords[1] = xCoords[0];
		yCoords[1] = yCoords[0];
	}

	@Override
	public void draw(IDrawVisitor v, boolean hasFill) {
		v.visit(this);
	}
	
	public void addBridge(double[] bridgeXCoords, double[] bridgeYCoords) { 
		this.bridgeXCoords = bridgeXCoords; 
		this.bridgeYCoords = bridgeYCoords;
	}
	
	public void setDest(Point2D dest) { this.dest = dest; }
	public Point2D getDest() { return dest; }
	
	public double[] getBridgeXCoords() { return bridgeXCoords; }
	public double[] getBridgeYCoords() { return bridgeYCoords; }
	public Color getBridgeColor() { return bridgeColor; }
	public boolean hasGuardPost() { return hasGuardPost; }

}
