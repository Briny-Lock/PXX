package csc2a.px.model.game;

import java.util.ArrayList;
import java.util.Random;

import csc2a.px.model.shape.ESHAPE_TYPE;
import csc2a.px.model.shape.Shape;
import csc2a.px.model.visitor.IDrawVisitor;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class Town {
	private static final float DEF_GOODS_SPACE = 3f;
	private static final int DEF_MAX_GOODS = 7;
	private static final int DEF_TOWN_SIZE = 20;
	private static final int TICKS_TO_GOODS = 200;
	
	private Point2D pos;
	private Shape shape;
	private int goodsCounter;
	private ArrayList<Shape> storedGoods;
	private ArrayList<ESHAPE_TYPE> wantedGoods;
	
	public Town(Shape shape) {
		this.goodsCounter = 0;
		this.shape = shape;
		this.pos = shape.getPos();
		storedGoods = new ArrayList<>();
		wantedGoods = new ArrayList<>();
		wantedGoods.add(shape.getType());
	}
	
	public boolean addGoods(Shape goods) {
		if (goods.getType() == this.shape.getType()) {
			System.out.println("Delivered goods");
			return true;
		}
		storedGoods.add(goods);
		renderGoods();
		return false;
	}
	
	public boolean generateGoods() {
		if (goodsCounter >= TICKS_TO_GOODS) {
			goodsCounter = 0;
			return true;
		} else {
			goodsCounter++;
			return false;
		}
	}
	
	public Shape removeGoods(ArrayList<ESHAPE_TYPE> types) {
		for (Shape g : storedGoods) {
			for (ESHAPE_TYPE type : types) {
				if (g.getType() == type) {
					storedGoods.remove(g);
					renderGoods();
					System.out.println("Removed Goods");
					return g;
				}
			}
		}
		return null;
	}
	
	private void renderGoods() {
		Point2D prevPos = pos.add(DEF_TOWN_SIZE, 0);
		for (int i = 0; i < storedGoods.size(); i++) {
			storedGoods.get(i).setPos(prevPos.add(DEF_GOODS_SPACE, 0));
			prevPos = storedGoods.get(i).getPos();
			if (i > DEF_MAX_GOODS) {
				storedGoods.get(i).setC(Color.RED);
			} else {
				storedGoods.get(i).setC(Color.GRAY);
			}
		}
	}
	
	public void drawAll(IDrawVisitor v) {
		drawTown(v);
		drawGoods(v);
	}
	
	public void drawTown(IDrawVisitor v) {
		shape.draw(v, true);		
	}
	
	public void drawGoods(IDrawVisitor v) {
		for (Shape shape : storedGoods) {
			shape.draw(v, false);
		}
	}
	
	public ArrayList<ESHAPE_TYPE> getWantedGoods() { return wantedGoods; }
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
