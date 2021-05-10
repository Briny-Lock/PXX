package csc2a.px.model.shape;

import java.util.Arrays;

import csc2a.px.model.visitor.IDrawVisitor;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class Line extends Polygon {
	private Point2D dest;
	
	public Line(Color c, Point2D start, Point2D dest) {
		super(c, ESHAPE_TYPE.LINE, start);
		this.dest = dest;
		calcCoords();
	}
	
	@Override
	protected void calcCoords() {
		if(pos.equals(calcMidpoint(pos, dest))) {
			coords = new Point2D[2];
			coords[0] = pos;
			coords[1] = dest;
		} else {
			coords = new Point2D[3];
			coords[0] = pos;
			coords[1] = calcMidpoint(pos, dest);
			coords[2] = dest;
		}
	}

	@Override
	public void draw(IDrawVisitor v, boolean hasFill) {
		v.visit(this);
	}
	
	public static Point2D calcMidpoint(Point2D start, Point2D dest) {
		// Smallest Y up top
		if (dest.getY() < start.getY()) {
			Point2D temp = start;
			start = dest;
			dest = temp;
		}
		double angle = Math.atan((dest.getY() - start.getY())/(dest.getX() - dest.getY())) * 180/Math.PI;
		
		// Checks if line is already in ideal state
		if (Arrays.asList(0.0, 45.0, -45.0, Double.NaN, 90, -90).contains(angle)) {
			return start;
		}
		
		// Check whether x or y are the farthest away
		Point2D diff = dest.subtract(start);
		Point2D midpoint;
		if(Math.abs(diff.getX()) > Math.abs(diff.getY())) {	// m = (ydiff)/(xdiff)
			double x;			
			if(start.getX() < dest.getX()) {
				x = (dest.getX() - diff.getY()); // m = 1; x = destX - diffY
			} else {
				x = (dest.getX() + diff.getY()); // m = -1; x = destX + diffY
			}
			midpoint = new Point2D(x, start.getY());
		} else {
			double y;			
			if(diff.getX() == Math.abs(diff.getX())) {
				y = (dest.getY() - diff.getX()); // m = 1; y = destY - diffX
			} else {
				y = (dest.getY() + diff.getX()); // m = -1; x = destY + diffX
			}
			midpoint = new Point2D(start.getX(), y);
		}
		return midpoint;
	}
	
	public void setDest(Point2D dest) { this.dest = dest; }
	public Point2D getDest() { return dest; }
	public Point2D getMid() { return (coords.length > 2) ? coords[1] : dest; }

}
