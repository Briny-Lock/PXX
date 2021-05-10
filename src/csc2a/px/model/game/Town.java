package csc2a.px.model.game;

import java.util.ArrayList;
import csc2a.px.model.shape.ESHAPE_TYPE;
import csc2a.px.model.shape.Shape;
import csc2a.px.model.visitor.IDrawVisitor;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class Town {
	private static final float DEF_GOODS_SPACE = 6f;
	private static final int DEF_MAX_GOODS = 7;
	private static final int TICKS_TO_GOODS = 150;
	
	private Point2D pos;
	private Shape shape;
	private double townSize;
	private Color defC;
	
	private int tickCounter;
	private float tickCorrection;
	
	private ArrayList<Shape> storedGoods;
	private ArrayList<ESHAPE_TYPE> wantedGoods;
	
	public Town(Shape shape, double townSize, Color defC) {
		this.tickCounter = 0;
		this.tickCorrection = 0;
		this.shape = shape;
		this.townSize = townSize;
		shape.setSize(townSize);
		this.defC = defC;
		this.pos = shape.getPos();
		storedGoods = new ArrayList<>();
		wantedGoods = new ArrayList<>();
		wantedGoods.add(shape.getType());
	}
	
	public boolean addGoods(Shape goods) {
		if (goods.getType() == this.shape.getType()) {
			return true;
		}
		storedGoods.add(goods);
		renderGoods();
		return false;
	}
	
	public boolean generateGoods(float deltaTime) {
		tickCorrection += deltaTime;
		while (tickCorrection > 1) {
			tickCounter++;
			tickCorrection -= 1;
		}
		if (tickCounter >= TICKS_TO_GOODS) {
			tickCounter = 0;
			return true;
		} else {
			tickCounter++;
			return false;
		}
	}
	
	public Shape removeGoods(ArrayList<ESHAPE_TYPE> types) {
		for (Shape g : storedGoods) {
			for (ESHAPE_TYPE type : types) {
				if (g.getType() == type) {
					storedGoods.remove(g);
					renderGoods();
					return g;
				}
			}
		}
		return null;
	}
	
	private void renderGoods() {
		Point2D prevPos = pos.add(townSize/2, -3);
		for (int i = 0; i < storedGoods.size(); i++) {
			storedGoods.get(i).setSize(5);
			storedGoods.get(i).setPos(prevPos.add(DEF_GOODS_SPACE, 0));
			prevPos = storedGoods.get(i).getPos();
			if (i > DEF_MAX_GOODS) {
				storedGoods.get(i).setC(Color.RED);
			} else {
				storedGoods.get(i).setC(defC);
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
	
	public void setWantedGoods(ArrayList<ESHAPE_TYPE> wantedGoods) {
		if (wantedGoods.contains(this.shape.getType())) {
			this.wantedGoods = wantedGoods;
		} else {
			this.wantedGoods = new ArrayList<>();
			this.wantedGoods.add(this.shape.getType());
			this.wantedGoods.addAll(wantedGoods);
		}
	}
	
	public ArrayList<ESHAPE_TYPE> getWantedGoods() { return wantedGoods; }
	public Point2D getPos() { return pos; }
	public Shape getShape() { return shape; }
	public int getReputationLoss() { return (storedGoods.size() <= DEF_MAX_GOODS) ? 0 : storedGoods.size() - DEF_MAX_GOODS; }
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Town))
			return false;
		Town town = (Town) obj;
		return (this.pos.distance(town.getPos()) == 0 && this.shape.getType() == town.getShape().getType());
	}
}
