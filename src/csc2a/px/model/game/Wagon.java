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
	private static final float SPEED = 40f;
	private static final double DEF_CARR_GAP = 3;
	
	private ArrayList<Carriage> carriages;
	private int maxCarriages = DEF_MAX_CARRIAGES;
	private float rotation;
	private boolean isForward = true;
	
	private Color c;
	private Color defC;
	private Image carriageImage;
	
	private Point2D pos;
	private Point2D dest;
	private Point2D prevPos;
	
	private double carrW;
	private double carrH;
	
	public Wagon(Color c, Color defC, Point2D position , double carrW, double carrH, Image carriageImage) {
		this.c = c;
		this.defC = defC;
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
			carriages.add(new Carriage(c, defC, pos, carriageImage, carrW, carrH, rotation));
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
		if (pos.distance(dest) < 1 || (pos.distance(prevPos) > dest.distance(prevPos))) {
			pos = dest;
			return;
		}
		
		Point2D motionVector = new Point2D(0, 0);
		if(deltaTime < 10)
			motionVector = new Point2D((float) (Math.cos(Math.toRadians(-rotation)) * deltaTime * SPEED), (float) (Math.sin(Math.toRadians(rotation)) * deltaTime * SPEED));
		if (dest.getX() >= pos.getX() && dest.getY() > pos.getY())
			pos = pos.add(motionVector);
		else if (dest.getX() >= pos.getX())
			pos = pos.add(motionVector);
		else
			pos = pos.subtract(motionVector);
		renderCarriages();
	}
	
	private void renderCarriages() {
		double xShift = 0, yShift = 0;
		
		xShift = (carrH + DEF_CARR_GAP)*Math.cos(Math.toRadians(-rotation)); // d*cos(-t);
		yShift = (carrH + DEF_CARR_GAP)*Math.sin(Math.toRadians(rotation)); // d*sin(t);

		carriages.get(0).updatePos(pos);
		for (int i = 1; i < carriages.size(); i++) {
			if (isForward)
				if (rotation <= 0)
					carriages.get(i).updatePos(carriages.get(i - 1).getPos().add(xShift, yShift));
				else
					carriages.get(i).updatePos(carriages.get(i - 1).getPos().subtract(xShift, yShift));
			else
				if (rotation <= 0)
					carriages.get(i).updatePos(carriages.get(i - 1).getPos().subtract(xShift, yShift));
				else
					carriages.get(i).updatePos(carriages.get(i - 1).getPos().add(xShift, yShift));
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
	}
	
	public void drawCarriages(IDrawVisitor v) {
		for (Carriage carriage : carriages) {
			carriage.draw(v, true);
		}
	}
	
	public void setCarriageImage(Image carriageImage) { 
		this.carriageImage = carriageImage; 
		for (Carriage carriage : carriages) {
			carriage.setCarriageImage(carriageImage);
		}
	}	
	public Point2D getPos() { return pos; }
	public Point2D getDest() { return dest; }
	public void setForward(boolean isForward) { this.isForward = isForward; }
	public boolean isForward() { return isForward; }
	public int getCarriageCount() { return carriages.size(); }
}
