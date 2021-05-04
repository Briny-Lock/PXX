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

	private Point2D position;
	private Image carriageImage;
	private ArrayList<Shape> goods;
	private float rotation;
	private Color c;
	
	private int maxGoods = DEF_MAX_GOODS;
	
	public Carriage(Color c, Point2D position, Image carriageImage, float rotation) {
		this.c = c;
		this.position = position;
		this.carriageImage = carriageImage;
		carriageImage.
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
		position = pos;
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
		return position;
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
	
	
}
