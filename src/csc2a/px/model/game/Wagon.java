package csc2a.px.model.game;

import java.util.ArrayList;

import csc2a.px.model.shape.EORIENTATION_TYPE;
import csc2a.px.model.shape.Shape;
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
	private EORIENTATION_TYPE orientation;
	
	private Color c;
	
	private double currX;
	private double currY;
	private double destX;
	private double destY;
	private double prevDestX;
	private double prevDestY;
	
	private double carrW;
	private double carrH;
	
	public Wagon(Color c, double x, double y, double maxspeed, double carrW, double carrH) {
		this.c = c;
		this.currX = x;
		this.currY = y;
		this.maxspeed = maxspeed;
		this.carrW = carrW;
		this.carrH = carrH;
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
			carriages.add(new Carriage(c, currX, currY, carrW, carrH, orientation));
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
	
	public void move(double deltaTime) {
		// deltaTime is used to run independently from frame rate
		if (Math.abs(destX - currX) < 0.5 && Math.abs(destY - currY) < 0.5) {
			currX = destX;
			currY = destY;
			return;
		}
		
		//Speed up
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
		switch (orientation) {
		case DIAG_LEFT:
			if (currX > destX) {
				xShift = -(carrH + DEF_CARR_GAP)/-Math.sqrt(2); // sin(-45) = -1/sqrt(2)
				yShift = (carrH + DEF_CARR_GAP)/Math.sqrt(2); // cos(-45) = 1/sqrt(2)
			} else {
				xShift = (carrH + DEF_CARR_GAP)/-Math.sqrt(2);
				yShift = -(carrH + DEF_CARR_GAP)/Math.sqrt(2);
			}
			break;
		case DIAG_RIGHT:
			if (currX > destX) {
				xShift = (carrH + DEF_CARR_GAP)/Math.sqrt(2); // sin(45) = 1/sqrt(2)
				yShift = -(carrH + DEF_CARR_GAP)/Math.sqrt(2); // cos(45) = 1/sqrt(2)
			} else {
				xShift = -(carrH + DEF_CARR_GAP)/Math.sqrt(2);
				yShift = (carrH + DEF_CARR_GAP)/Math.sqrt(2);
			}
			break;
		case LEFT_RIGHT:
			if (currX > destX) {
				xShift = (carrH + DEF_CARR_GAP);
				yShift = 0;
			} else {
				xShift = -(carrH + DEF_CARR_GAP);
				yShift = 0;
			}
			break;
		case UP_DOWN:
			if (currY > destY) {
				xShift = 0;
				yShift = (carrH + DEF_CARR_GAP);
			} else {
				xShift = 0;
				yShift = -(carrH + DEF_CARR_GAP);
			}
			break;
		}

		carriages.get(0).updateXY(currX + xShift, currY + yShift);
		for (int i = 1; i < carriages.size(); i++) {
			carriages.get(i).updateXY(carriages.get(i - 1).getX() + xShift, carriages.get(i - 1).getY() + yShift);
		}
	}
	
	public void setDest(double destX, double destY, EORIENTATION_TYPE orientation) {
		this.prevDestX = this.destX;
		this.prevDestY = this.destY;
		this.destX = destX;
		this.destY = destY;
		this.orientation = orientation;
		for (Carriage carriage : carriages) {
			carriage.setOrientation(orientation);
		}
		
		if (currX > destX) { xspeed = -DEF_SLOWEST; }
		else if (currX > destX) { xspeed = DEF_SLOWEST; }
		else { xspeed = 0; }
		
		if (currY > destY) { yspeed = DEF_SLOWEST; }
		else if (currY > destY) { yspeed = -DEF_SLOWEST; }
		else { yspeed = 0; }
	}
}
