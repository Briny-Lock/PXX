package csc2a.px.model.game;

import java.util.ArrayList;

import csc2a.px.model.shape.EORIENTATION_TYPE;
import csc2a.px.model.shape.Shape;
import csc2a.px.model.visitor.IDrawVisitor;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Wagon {
	private static final int DEF_MAX_CARRIAGES = 4;
	private static final int DEF_SLOW_DIST = 10;
	private static final double DEF_SLOWEST = 1;
	private static final double DEF_ACCELERATION = 0.15;
	private static final double DEF_CARR_GAP = 3;
	
	private double maxspeed;
	private double xspeed;
	private double yspeed;
	private double acceleration = DEF_ACCELERATION;
	
	private ArrayList<Carriage> carriages;
	private int maxCarriages = DEF_MAX_CARRIAGES;
	private float rotation;
	
	private Color c;
	
	private Point2D pos;
	private Point2D dest;
	private Point2D prevPos;
	
	private double carrW;
	private double carrH;
	
	public Wagon(Color c, Point2D position , double maxspeed, double carrW, double carrH, Image carriageImage) {
		this.c = c;
		this.pos = position;
		this.maxspeed = maxspeed;
		this.carrW = carrW;
		this.carrH = carrH;
		this.carriages = new ArrayList<>();
		this.rotation = 0.0f;
		addCarriage();
	}
	
	public boolean addGoods(Shape goods) {
		for (Carriage carriage : carriages) {
			if (carriage.addGoods(goods)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean addCarriage() {
		if (carriages.size() < maxCarriages) {
			carriages.add(new Carriage(c, pos));
			renderCarriages();
			return true;
		} else
			return false;
	}
	
	public Shape deliverGoods(Shape goods) {
		for (Carriage carriage : carriages) {
			Shape deliveredGoods = carriage.deliverGoods(goods);
			if (deliveredGoods != null)
				return deliveredGoods;
		}
		return null;
	}
	
	public void move(float deltaTime) {
		System.out.printf("%f : (%f,%f) at xspeed: %.5f and yspeed: %.5f\n", deltaTime, currX, currY, xspeed, yspeed);
		// deltaTime is used to run independently from frame rate
		if (Math.abs(destX - currX) < 0.5 && Math.abs(destY - currY) < 0.5) {
			currX = destX;
			currY = destY;
			return;
		}
		
		//Speed up
		if (currX != destX && currY != destY) {
			if (xspeed < maxspeed) {
				xspeed += acceleration * (prevDestX - currX);
			}
			if (xspeed > maxspeed) {
				xspeed = maxspeed;
			}
			if (yspeed < maxspeed) {
				yspeed += acceleration * (prevDestY - currY);
			}
			if (yspeed > maxspeed) {
				yspeed = maxspeed;
			}
		}
		// Slow down
		if (Math.abs(destX - currX) < DEF_SLOW_DIST && currX != destX) {				
			// deceleration for a smooth stop
			xspeed -= acceleration * (destX - currX);
			if (xspeed < DEF_SLOWEST) {
				xspeed = DEF_SLOWEST;
			}
		}
		if (Math.abs(destY - currY) < DEF_SLOW_DIST && currY != destY) {			
			// deceleration for a smooth stop
			yspeed -= acceleration * (destY - currY);
			if (yspeed < DEF_SLOWEST) {
				yspeed = DEF_SLOWEST;
			}
		}
		
		currX += xspeed * deltaTime;
		currY += yspeed * deltaTime;
		
		renderCarriages();
	}
	
	private void renderCarriages() {
		double xShift = 0, yShift = 0;
//		switch (orientation) {
//		case DIAG_LEFT:
//			if (currX > destX) {
//				xShift = -(carrH + DEF_CARR_GAP)/-Math.sqrt(2); // sin(-45) = -1/sqrt(2)
//				yShift = (carrH + DEF_CARR_GAP)/Math.sqrt(2); // cos(-45) = 1/sqrt(2)
//			} else {
//				xShift = (carrH + DEF_CARR_GAP)/-Math.sqrt(2);
//				yShift = -(carrH + DEF_CARR_GAP)/Math.sqrt(2);
//			}
//			break;
//		case DIAG_RIGHT:
//			if (currX > destX) {
//				xShift = (carrH + DEF_CARR_GAP)/Math.sqrt(2); // sin(45) = 1/sqrt(2)
//				yShift = -(carrH + DEF_CARR_GAP)/Math.sqrt(2); // cos(45) = 1/sqrt(2)
//			} else {
//				xShift = -(carrH + DEF_CARR_GAP)/Math.sqrt(2);
//				yShift = (carrH + DEF_CARR_GAP)/Math.sqrt(2);
//			}
//			break;
//		case LEFT_RIGHT:
//			if (currX > destX) {
//				xShift = (carrH + DEF_CARR_GAP);
//				yShift = 0;
//			} else {
//				xShift = -(carrH + DEF_CARR_GAP);
//				yShift = 0;
//			}
//			break;
//		case UP_DOWN:
//			if (currY > destY) {
//				xShift = 0;
//				yShift = (carrH + DEF_CARR_GAP);
//			} else {
//				xShift = 0;
//				yShift = -(carrH + DEF_CARR_GAP);
//			}
//			break;
//		}

//		carriages.get(0).updateXY(currX + xShift, currY + yShift);
		for (int i = 1; i < carriages.size(); i++) {
			carriages.get(i).updateXY(carriages.get(i - 1).getX() + xShift, carriages.get(i - 1).getY() + yShift);
		}
	}
	
	public void setDest(double destX, double destY) {
		this.prevDestX = this.destX;
		this.prevDestY = this.destY;
		this.destX = destX;
		this.destY = destY;
		
//		if (currX != destX && currY == destY) {
//			orientation = EORIENTATION_TYPE.LEFT_RIGHT;
//		} else if(currY != destY && currX == destX) {
//			orientation = EORIENTATION_TYPE.UP_DOWN;
//		} else if((destX < currX && destY < currY) || (destX < currX && destY < currY)) {
//			orientation = EORIENTATION_TYPE.DIAG_RIGHT;
//		} else {
//			orientation = EORIENTATION_TYPE.DIAG_LEFT;
//		}
		
		for (Carriage carriage : carriages) {
//			carriage.setOrientation(orientation);
		}
		
		if (currX > destX) { xspeed = -DEF_SLOWEST; }
		else if (currX > destX) { xspeed = DEF_SLOWEST; }
		else { xspeed = 0; }
		
		if (currY > destY) { yspeed = DEF_SLOWEST; }
		else if (currY > destY) { yspeed = -DEF_SLOWEST; }
		else { yspeed = 0; }
	}
	
	public void drawCarriages(IDrawVisitor v, boolean hasFill) {
		for (Carriage carriage : carriages) {
			carriage.draw(v, hasFill);
		}
	}
}
