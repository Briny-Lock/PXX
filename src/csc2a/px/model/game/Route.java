/**
 * 
 */
package csc2a.px.model.game;

import java.util.ArrayList;
import java.util.List;

import csc2a.px.model.abstract_factory.ShapeFactory;
import csc2a.px.model.shape.ESHAPE_TYPE;
import csc2a.px.model.shape.Line;
import csc2a.px.model.shape.Shape;
import csc2a.px.model.visitor.IDrawVisitor;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * @author JC Swanzen (220134523)
 * @version PXX
 *
 */
public class Route {
	private static final int MAX_WAGONS = 4;
	
	private Color defC;
	private Color c;
	private ShapeFactory factory;
	private ArrayList<Line> lines;
	private ArrayList<Wagon> wagons;
	private ArrayList<Town> towns;
	private Image carriageImage;
	private double carrW, carrH;
	private int coinPerDelivery;
	private boolean isUnlocked = false;
	
	private ArrayList<Town> toRemove;
	
	public Route(Color c, Color defC, Image carriageImage, double carrW, double carrH, int coinPerDelivery) {
		lines = new ArrayList<>();
		wagons = new ArrayList<>();
		towns = new ArrayList<>();
		factory = new ShapeFactory();
		toRemove = new ArrayList<>();
		this.c = c;
		this.defC = defC;
		this.carriageImage = carriageImage;

		this.carrW = carrW;
		this.carrH = carrH;
		this.coinPerDelivery = coinPerDelivery;

		addWagon();
	}
	
	public boolean linkTowns(Town town1, Town town2) {
		if (town1 == null || town2 == null)
			return false;
		if (town1.equals(town2))
			return false;
		int check = checkTown(town1);
		if (check == -1) {
			addTown(town1, 0);
			addTown(town2, 1);
			return true;
		}
		if (check == -2) {
			check = checkTown(town2);
			if (check == -2)
				return false;
			addTown(town1, check);
			return true;
		}
		if (checkTown(town2) >= 0)
			return false;
		addTown(town2, check + 1);
		return true;
	}
	
	private int checkTown(Town town) {
		// If no towns are linked
		if (towns.size() == 0) {
			return -1;
		}
		// Test if town exists in line
		for (int i = 0; i < towns.size(); i++) {
			if (town.equals(towns.get(i))) {
				return i; // town was found at position i
			}
		}
		return -2; // town was not found
	}
	
	public void addTown(Town town, int index) {
		if (towns.size() < 1 || index == towns.size()) {
			towns.add(town);
		} else if (index <= 0) {
			ArrayList<Town> temp = new ArrayList<>();
			temp.add(town);
			temp.addAll(towns);
			towns = temp;
		} else if (index < towns.size()) {
			List<Town> preTown	= towns.subList(0, index);
			List<Town> postTown	= towns.subList(index, towns.size());
			towns = new ArrayList<>(preTown);
			towns.add(town);
			towns.addAll(postTown);
		}
		renderLines();
	}
	
	public void renderLines() {
		lines.clear();
		if (towns.size() > 1) {
			for (int i = 1; i < towns.size(); i++) {
				Line line = (Line) factory.createLine(c, towns.get(i - 1).getPos(), towns.get(i).getPos());
				lines.add(line);					
			}
		}
	}
	
	public void clear() {
		wagons.clear();
		towns.clear();
		lines.clear();
	}	
	
	public int removeTown(Town town) {
		for (Town t : towns) {
			if (t.equals(town)) {
				toRemove.add(t);
				if (towns.size() < 2) {
					towns.clear();
					int refund = wagons.size();
					wagons.clear();
					return refund;
				}
				return 0;
			}
		}
		return 0;
	}
	
	public boolean addWagon() {
		if (wagons.size() >= MAX_WAGONS)
			return false;
		if (towns.size() < 1)
			return false;
		wagons.add(new Wagon(c, defC, towns.get(0).getPos(), carrW, carrH, carriageImage));
		return true;
	}
	
	public int update(float deltaTime) {
		int coin = 0;
		for (Wagon wagon : wagons) {
			if (wagon.getPos().equals(wagon.getDest())) {
				coin += processGoods(wagon, deltaTime);
			} else {
				wagon.move(deltaTime);
			}
		}
		
		ArrayList<Town> trueRemove = new ArrayList<>();
		for (Town town : toRemove) {
			boolean removable = true;
			int index = towns.indexOf(town);
			for (Wagon wagon : wagons) {
				if (wagon.isForward())
					index = towns.indexOf(town);
				else
					index = towns.indexOf(town) + 1;
				if (index >= lines.size()) {
					removable = false;
					break;
				}
				if (index == -1) {
					removable = true;
					break;
				}
				Point2D mid = lines.get(index).getMid();
				Point2D start = lines.get(index).getPos();
				if (wagon.getDest().equals(town.getPos()) || wagon.getDest().equals(mid) 
						|| wagon.getDest().equals(start)) {
					removable = false;
					break;
				}
			}
			if (removable) {
				towns.remove(town);
				trueRemove.add(town);
				renderLines();
			}
		}
		
		for (Town town : trueRemove) {
			toRemove.remove(town);
		}
		return coin;
	}
	
	public void draw(IDrawVisitor v) {
		for (Line l : lines) {
			l.draw(v, true);
		}
		for (Wagon wagon : wagons) {
			wagon.drawCarriages(v);
		}
	}
	
	private int processGoods(Wagon wagon, float deltaTime) {
		int coin = 0;
		for (int i = 0; i < towns.size(); i++) {
			Town t = towns.get(i);
			if (t.getPos().equals(wagon.getPos())) {
				if (wagon.canTransfer(deltaTime)) {
					if (wagon.isForward() && t == towns.get(towns.size() - 1)) {
						wagon.setForward(false);
					} else if (!wagon.isForward() && t == towns.get(0)) {					
						wagon.setForward(true);
					}
					Shape goods = wagon.deliverGoods(t.getWantedGoods());
					if (goods == null) {
						ArrayList<ESHAPE_TYPE> predictedGoods;
						if (wagon.isForward()) {
							predictedGoods = predictGoods(towns.subList(i, towns.size()));
						} else {						
							predictedGoods = predictGoods(towns.subList(0, i));
						}
						Shape toGo = t.removeGoods(predictedGoods);
						if (toGo == null) {
							if (wagon.isForward())
								wagon.setDest(lines.get(i).getCoords()[1]);
							else
								wagon.setDest(lines.get(i - 1).getMid());	
						} else
							wagon.addGoods(toGo);
					}
					else {
						if (t.addGoods(goods))
							coin += coinPerDelivery;
					}
				}
				return coin;
			}
		}
		
		// Wagon at midpoint of a line
		for (Line l : lines) {
			if (l.getMid().equals(wagon.getPos())) {
				if (wagon.isForward())
					wagon.setDest(l.getDest());
				else
					wagon.setDest(l.getPos());
				break;
			}
		}
		return coin;
	}
	
	private ArrayList<ESHAPE_TYPE> predictGoods(List<Town> towns) {
		ArrayList<ESHAPE_TYPE> predicted = new ArrayList<>();
		for (Town t : towns) {
			if (!predicted.contains(t.getShape().getType()))
				predicted.add(t.getShape().getType());
		}
		return predicted;
	}
	
	public void setCarriageImage(Image carriageImage) {
		this.carriageImage = carriageImage;
		for (Wagon wagon : wagons) {
			wagon.setCarriageImage(carriageImage);
		}
	}

	public boolean isUnlocked() {
		return isUnlocked;
	}

	public void setUnlocked(boolean isUnlocked) {
		this.isUnlocked = isUnlocked;
	}
	
	public Color getC() {
		return c;
	}

	public boolean addCarriage() {
		int leastCarriages = 100, index = -1;
		for (int i = 0; i < wagons.size(); i++) {
			if (wagons.get(i).getCarriageCount() < leastCarriages) {
				leastCarriages = wagons.get(i).getCarriageCount();
				index = i;
			}
		}
		if (index == -1)
			return false;		
		return wagons.get(index).addCarriage();
	}
}
