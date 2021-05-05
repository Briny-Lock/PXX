package csc2a.px.model.game;

import java.util.ArrayList;

import csc2a.px.model.shape.Shape;
import csc2a.px.model.visitor.IDrawVisitor;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Wagon {
	private static final int DEF_MAX_CARRIAGES = 4;
	private static final int DEF_SLOW_DIST = 10;
	private static final float MIN_SPEED = 0.2f;
	private static final float MAX_SPEED = 5f;
	private static final double DEF_ACCELERATION = 0.15;
	private static final double DEF_CARR_GAP = 3;
	
	private double speed;
	private double acceleration = DEF_ACCELERATION;
	
	private ArrayList<Carriage> carriages;
	private int maxCarriages = DEF_MAX_CARRIAGES;
	private float rotation;
	
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
	
	public Shape deliverGoods(Shape goods) {
		for (Carriage carriage : carriages) {
			Shape deliveredGoods = carriage.deliverGoods(goods);
			if (deliveredGoods != null)
				return deliveredGoods;
		}
		return null;
	}
	
	public void move(float deltaTime) {
		//System.out.printf("%f : (%f,%f) at speed: %.5f\n", deltaTime, pos.getX(), pos.getY(), speed);
		// deltaTime is used to run independently from frame rate
		if (Math.abs(dest.getX() - pos.getX()) < 0.5 && Math.abs(dest.getY() - pos.getY()) < 0.5) {
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
			motionVector = new Point2D((float) (Math.cos(Math.toRadians(-rotation)) * deltaTime * 20), (float) (Math.sin(Math.toRadians(-rotation)) * deltaTime * 20));
		//System.out.println(motionVector.getX() + ":" + motionVector.getY());
		pos = pos.add(motionVector);
		
		renderCarriages();
	}
	
	private void renderCarriages() {
		double xShift = 0, yShift = 0;
		
		xShift = (carrH + DEF_CARR_GAP)*Math.cos(Math.toRadians(rotation)); // d*cos(t);
		yShift = (carrH + DEF_CARR_GAP)*Math.sin(Math.toRadians(rotation)); // d*sin(t);

		carriages.get(0).updatePos(pos);
		for (int i = 1; i < carriages.size(); i++) {
			carriages.get(i).updatePos(carriages.get(i - 1).getPosition().add(xShift, yShift));
		}
	}
	
	public void setDest(Point2D dest) {
		this.prevPos = pos;
		this.dest = dest;
		
		// TODO calculate rotation
		Point2D p1 = dest.subtract(pos);
		Point2D p2 = new Point2D(1, 0);
		double tempRot = p2.angle(p1);
		if (tempRot == Double.NaN) {
			rotation = 90;
		} else {
			rotation = (float) tempRot;
		}
		
		//System.out.println(pos.getX() + ":" + pos.getY() + " vs. " + dest.getX() + ":" + dest.getY() + " at rotation: " + rotation);
		for (Carriage carriage : carriages) {
			carriage.setRotation(rotation);
		}
		
		speed = MAX_SPEED;
	}
	
	public void drawCarriages(IDrawVisitor v, boolean hasFill) {
		for (Carriage carriage : carriages) {
			carriage.draw(v, hasFill);
		}
	}
}
