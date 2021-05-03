package csc2a.px.model.game;

import java.util.ArrayList;

import csc2a.px.model.shape.CarriageShape;
import csc2a.px.model.shape.EORIENTATION_TYPE;
import csc2a.px.model.shape.Shape;
import javafx.scene.paint.Color;

public class Carriage {
	public static final int DEF_MAX_GOODS = 6;

	private CarriageShape carriageShape;
	private ArrayList<Shape> goods;
	private int maxGoods = DEF_MAX_GOODS;
	
	public Carriage(Color c, double centX, double centY, double w, double h, EORIENTATION_TYPE orientation) {
		carriageShape = new CarriageShape(c, centX, centY, w, h, orientation);
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
	
	public void updateXY(double centX, double centY) {
		carriageShape.updateXY(centX, centY);
	}
	
	public void setOrientation(EORIENTATION_TYPE orientation) {
		carriageShape.setOrientation(orientation);
	}
	
	public double getX() {
		return carriageShape.getX();
	}
	
	public double getY() {
		return carriageShape.getY();
	}
}
