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
	private static final float DEF_GOODS_SPACE = 5f;
	
	public Carriage(Color c, Point2D position, Image carriageImage, double w, double h, float rotation) {
		this.c = c;
		this.pos = position;
		this.carriageImage = carriageImage;
		this.w = w;
		this.h = h;
		this.rotation = rotation;
		this.goods = new ArrayList<>();
	}
	
	public boolean addGoods(Shape goods) {
		if (this.goods.size() < maxGoods) {
			this.goods.add(goods);
			renderGoods();
			return true;
		} else {
			return false;
		}
	}
	
	public Shape deliverGoods(ArrayList<Shape> goods) {
		for (Shape g : this.goods) {
			for (Shape wanted : goods) {
				if (g.getType() == wanted.getType()) {
					renderGoods();
					goods.remove(g);
					return g;				
				}
			}			
		}
		return null;
	}
	
	private void renderGoods() {
		Point2D prevPos = pos.add(w, 0);
		for (int i = 0; i < goods.size(); i++) {
			goods.get(i).setPos(prevPos.add(DEF_GOODS_SPACE, 0));
			prevPos = goods.get(i).getPos();
			goods.get(i).setC(Color.GRAY);
		}
	}
	
	public void updatePos(Point2D pos) {
		this.pos = pos;
	}
	
	public void setRotation (float rotation) {
		this.rotation = rotation;
	}
	
	@Override
	public void draw(IDrawVisitor v, boolean hasFill) {
		for (Shape g : goods) {
			g.draw(v, hasFill);
		}
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
