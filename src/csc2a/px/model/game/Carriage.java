package csc2a.px.model.game;

import java.util.ArrayList;

import csc2a.px.model.shape.Shape;
import csc2a.px.model.visitor.IDrawVisitor;
import csc2a.px.model.visitor.IDrawable;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Carriage implements IDrawable {
	public static final int DEF_MAX_GOODS = 6;

	private Color c;
	private Point2D pos;
	private Image carriageImage;
	private ArrayList<Shape> goods;
	private float rotation;
	private double w;
	private double h;
	
	private int maxGoods = DEF_MAX_GOODS;
	
	public Carriage(Color c, Point2D position, Image carriageImage, double w, double h, float rotation) {
		this.c = c;
		this.pos = position;
		this.carriageImage = carriageImage;
		this.w = w;
		this.h = h;
		this.rotation = rotation;
	}
	
	public boolean addGoods(Shape goods) {
		if (this.goods.size() < maxGoods) {
			this.goods.add(goods);
			return true;
		} else {
			return false;
		}
	}
	
	public Shape deliverGoods(Shape goods) {
		for (Shape g : this.goods) {
			if (g.getType() == goods.getType())
				return g;
		}
		return null;
	}
	
	public void updatePos(Point2D pos) {
		this.pos = pos;
	}
	
	public void setRotation (float rotation) {
		this.rotation = rotation;
	}
	
	@Override
	public void draw(IDrawVisitor v, boolean hasFill) {
		v.visit(this);
	}

	/**
	 * @return the position
	 */
	public Point2D getPosition() {
		return pos;
	}

	/**
	 * @return the carriageImage
	 */
	public Image getCarriageImage() {
		return carriageImage;
	}

	/**
	 * @return the rotation
	 */
	public float getRotation() {
		return rotation;
	}

	/**
	 * @return the c
	 */
	public Color getC() {
		return c;
	}

	public Point2D getCenter() {
		return new Point2D(pos.getX() + w / 2, pos.getY() + h / 2);
	}

	public double getW() {
		return w;
	}

	public double getH() {
		return h;
	}
}
