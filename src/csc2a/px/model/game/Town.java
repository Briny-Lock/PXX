package csc2a.px.model.game;

import java.util.ArrayList;

import csc2a.px.model.shape.Shape;
import csc2a.px.model.visitor.IDrawVisitor;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class Town {
	private static final float DEF_GOODS_SPACE = 5f;
	private static final int DEF_MAX_GOODS = 7;
	
	private Point2D pos;
	private Shape shape;
	private ArrayList<Shape> storedGoods;
	private ArrayList<Shape> wantedGoods;
	
	public Town(Shape shape) {
		this.shape = shape;
		this.pos = shape.getPos();
		storedGoods = new ArrayList<>();
		wantedGoods = new ArrayList<>();
	}
	
	public void addGoods(Shape goods) {
		storedGoods.add(goods);
		renderGoods();
	}
	
	public boolean removeGoods(Shape goods) {
		for (Shape g : storedGoods) {
			if (g.getType() == goods.getType()) {
				storedGoods.remove(g);
				renderGoods();
				return true;
			}
		}
		return false;
	}
	
	private void renderGoods() {
		Point2D prevPos = pos;
		for (int i = 0; i < storedGoods.size(); i++) {
			storedGoods.get(i).setPos(prevPos.add(DEF_GOODS_SPACE, 0));
			prevPos = storedGoods.get(i).getPos();
			if (i > DEF_MAX_GOODS) {
				storedGoods.get(i).setC(Color.RED);
			}
		}
	}
	
	public void drawAll(IDrawVisitor v) {
		drawTown(v);
		drawGoods(v);
	}
	
	public void drawTown(IDrawVisitor v) {
		shape.draw(v, false);		
	}
	
	public void drawGoods(IDrawVisitor v) {
		for (Shape shape : storedGoods) {
			shape.draw(v, true);
		}
	}
	
	public ArrayList<Shape> getWantedGoods() { return wantedGoods; }
	public Point2D getPos() { return pos; }
	public Shape getShape() { return shape; }
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Town))
			return false;
		Town town = (Town) obj;
		return (this.pos.distance(town.getPos()) == 0 && this.shape.getType() == town.getShape().getType()) ? true : false;
	}
}
