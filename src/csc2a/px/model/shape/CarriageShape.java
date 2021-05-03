package csc2a.px.model.shape;

import csc2a.px.model.visitor.IDrawVisitor;
import javafx.scene.paint.Color;

public class CarriageShape extends Polygon {
	private EORIENTATION_TYPE orientation;
	private double centX;
	private double centY;
	private double w;
	private double h;

	public CarriageShape(Color c, double centX, double centY, double w, double h, EORIENTATION_TYPE orientation) {
		super(c, ESHAPE_TYPE.CARRIAGE_SHAPE);
		xCoords = new double[4];
		yCoords = new double[4];
		this.centX = centX;
		this.centY = centY;
		this.w = w;
		this.h = h;
		this.orientation = orientation;
	}

	@Override
	protected void calcCoords() {
		double[] straightX;
		double[] straightY;
		switch (this.orientation) {
		case UP_DOWN:
			xCoords = calcXCoords(h);
			yCoords = calcYCoords(w);
			break;
		case LEFT_RIGHT:
			xCoords = calcXCoords(w);
			yCoords = calcYCoords(h);
			break;
		case DIAG_RIGHT:
			// Lines have midpoints calculated at 45 degree angles to ease the calculations for rotating carriages
			// (x,y) = (centX + (x1 - centX)/sqrt(2) + (y1 - centY)/sqrt(2), centY - (x1 - centX)/sqrt(2) + (y1 - centY)/sqrt(2) 
			// Where (x1, y1) is the point to be rotated
			straightX = calcXCoords(h);
			straightY = calcYCoords(w);
			for (int i = 0; i < 4; i++) {
				xCoords[i] = centX + (straightX[i] - centX)/Math.sqrt(2) + (straightY[0] - centY)/Math.sqrt(2);
				yCoords[i] = centY - (straightX[i] - centX)/Math.sqrt(2) + (straightY[0] - centY)/Math.sqrt(2);
			}
			break;
		case DIAG_LEFT:
			// Similar logic with a horizontal rectangle
			straightX = calcXCoords(w);
			straightY = calcYCoords(h);
			for (int i = 0; i < 4; i++) {
				xCoords[i] = centX + (straightX[i] - centX)/Math.sqrt(2) + (straightY[0] - centY)/Math.sqrt(2);
				yCoords[i] = centY - (straightX[i] - centX)/Math.sqrt(2) + (straightY[0] - centY)/Math.sqrt(2);
			}
			break;
		}
	}
	
	private double[] calcXCoords(double effW) {
		double[] coords = new double[4]; 
		coords[0] = centX - effW/2;
		coords[1] = centX + effW/2;
		coords[2] = coords[1];
		coords[3] = coords[0];
		return coords;
	}
	
	private double[] calcYCoords(double effH) {
		double[] coords = new double[4]; 
		coords[0] = centY - effH/2;		
		coords[1] = coords[0];		
		coords[2] = centY + effH/2;
		coords[3] = coords[2];
		return coords;
	}

	@Override
	public void draw(IDrawVisitor v, boolean hasFill) {
		v.visit(this, hasFill);
	}
	
	public void setOrientation(EORIENTATION_TYPE orientation) {
		this.orientation = orientation;
	}
	
	public void updateXY(double centX, double centY) {
		this.centX = centX;
		this.centY = centY;
		calcCoords();
	}
	
	public double getX() {
		return centX;
	}
	
	public double getY() {
		return centY;
	}
}
