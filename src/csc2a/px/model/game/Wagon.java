package csc2a.px.model.game;

import java.util.ArrayList;

import csc2a.px.model.shape.ESHAPE_TYPE;
import csc2a.px.model.shape.Shape;
import csc2a.px.model.visitor.IDrawVisitor;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Wagon {
	private static final int DEF_MAX_CARRIAGES = 4;
	private static final int DEF_SLOW_DIST = 10;
	private static final float MIN_SPEED = 0.2f;
	private static final float MAX_SPEED = 50f;
	private static final double DEF_ACCELERATION = 0.15;
	private static final double DEF_CARR_GAP = 3;
	
	private double speed;
	private double acceleration = DEF_ACCELERATION;
	
	private ArrayList<Carriage> carriages;
	private int maxCarriages = DEF_MAX_CARRIAGES;
	private float rotation;
	private boolean isForward = true;
	
	private Color c;
	private Image carriageImage;
	
	private Point2D pos;
	private Point2D dest;
	private Point2D prevPos;
	
	private double carrW;
	private double carrH;
	
	public Wagon(Color c, Point2D position , double carrW, double carrH, Image carriageImage) {
		this.c = c;
		this.pos = position;
		this.dest = this.pos;
		this.carrW = carrW;
		this.carrH = carrH;
		this.carriageImage = carriageImage;
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
			carriages.add(new Carriage(c, pos, carriageImage, carrW, carrH, rotation));
			renderCarriages();
			return true;
		} else
			return false;
	}
	
	public Shape deliverGoods(ArrayList<ESHAPE_TYPE> goods) {
		for (Carriage carriage : carriages) {
			Shape deliveredGoods = carriage.deliverGoods(goods);
			if (deliveredGoods != null)
				return deliveredGoods;
		}
		return null;
	}
	
	public void move(float deltaTime) {
		// deltaTime is used to run independently from frame rate
		if ((Math.abs(dest.getX() - pos.getX()) < 1 && Math.abs(dest.getY() - pos.getY()) < 1)) {
			pos = dest;
			return;
		}
		
		//Speed up
//		if (pos.getX() != dest.getX() || pos.getY() != dest.getY()) {
//			if (xspeed < MAX_SPEED) {
//				xspeed += acceleration * (prevPos.getX() - pos.getX());
//			}
//			if (xspeed > MAX_SPEED) {
//				xspeed = MAX_SPEED;
//			}
//			if (yspeed < MAX_SPEED) {
//				yspeed += acceleration * (prevPos.getY() - pos.getY());
//			}
//			if (yspeed > MAX_SPEED) {
//				yspeed = MAX_SPEED;
//			}
//		}
//		// Slow down
//		if (Math.abs(pos.getX() - dest.getX()) < DEF_SLOW_DIST && pos.getX() != dest.getX()) {				
//			// deceleration for a smooth stop
//			xspeed -= acceleration * (dest.getX() - pos.getX());
//			if (xspeed < MIN_SPEED) {
//				xspeed = MIN_SPEED;
//			}
//		}
//		if (Math.abs(pos.getY() - dest.getY()) < DEF_SLOW_DIST && pos.getY() != dest.getY()) {			
//			// deceleration for a smooth stop
//			yspeed -= acceleration * (dest.getY() - pos.getY());
//			if (yspeed < MIN_SPEED) {
//				yspeed = MIN_SPEED;
//			}
//		}
		Point2D motionVector = new Point2D(0, 0);
		if(deltaTime < 10)
			motionVector = new Point2D((float) (Math.cos(Math.toRadians(-rotation)) * deltaTime * speed), (float) (Math.sin(Math.toRadians(rotation)) * deltaTime * speed));
		//System.out.println(motionVector.getX() + ":" + motionVector.getY());
		if ((dest.getX() >= pos.getX() || dest.getY() > pos.getY()))
			pos = pos.add(motionVector);
		else
			pos = pos.subtract(motionVector);
		
		renderCarriages();
	}
	
	private void renderCarriages() {
		double xShift = 0, yShift = 0;
		
		xShift = (carrH + DEF_CARR_GAP)*Math.cos(Math.toRadians(rotation)); // d*cos(t);
		yShift = (carrH + DEF_CARR_GAP)*Math.sin(Math.toRadians(rotation)); // d*sin(t);

		carriages.get(0).updatePos(pos);
		for (int i = 1; i < carriages.size(); i++) {
			if (isForward)
				carriages.get(i).updatePos(carriages.get(i - 1).getPos().add(xShift, yShift));
			else
				carriages.get(i).updatePos(carriages.get(i - 1).getPos().subtract(xShift, yShift));
		}
	}
	
	public void setDest(Point2D dest) {
		this.prevPos = pos;
		this.dest = dest;
		
		double m = (dest.getY() - pos.getY())/(dest.getX() - pos.getX());
		double tempRot = Math.atan(m);
		if (tempRot == Double.NaN) {
			rotation = 90;
		} else {
			rotation = (float) (tempRot * 180/Math.PI);
		}
		for (Carriage carriage : carriages) {
			carriage.setRotation(rotation);
		}
		
		speed = MAX_SPEED;
	}
	
	public void drawCarriages(IDrawVisitor v) {
		for (Carriage carriage : carriages) {
			carriage.draw(v, true);
		}
	}
	
	public void setCarriageImage(Image carriageImage) { this.carriageImage = carriageImage; }	
	public Point2D getPos() { return pos; }
	public Point2D getDest() { return dest; }
	public void setForward(boolean isForward) { this.isForward = isForward; }
	public boolean isForward() { return isForward; }
}
