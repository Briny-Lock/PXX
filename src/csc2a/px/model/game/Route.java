/**
 * 
 */
package csc2a.px.model.game;

import java.util.ArrayList;
import java.util.List;

import csc2a.px.model.Common;
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
	
	private Color c;
	private ShapeFactory factory;
	private ArrayList<Line> lines;
	private ArrayList<Wagon> wagons;
	private ArrayList<Town> towns;
	private ArrayList<Point2D[]> bridges;
	private Image carriageImage;
	private double carrW, carrH;
	private int coinPerDelivery;
	private boolean isUnlocked = false;
	
	public Route(Color c, Image carriageImage, double carrW, double carrH, int coinPerDelivery) {
		lines = new ArrayList<>();
		bridges = new ArrayList<>();
		wagons = new ArrayList<>();
		towns = new ArrayList<>();
		factory = new ShapeFactory();
		this.c = c;
		this.carriageImage = carriageImage;

		this.carrW = carrW;
		this.carrH = carrH;
		this.coinPerDelivery = coinPerDelivery;

		addWagon();
	}
	
	public boolean linkTowns(Town town1, Town town2) {
		int check = checkTown(town1);
		if (check < 0) {
			check = checkTown(town2);
			if (check < 0)
				return false;
			addTown(town1, check - 1);
			return true;
		}
		if (check == 0) {
			addTown(town1, 0);
			addTown(town2, 1);
			return true;
		}
		addTown(town2, check);
		return true;
	}
	
	public boolean linkTowns(Town town1, Town town2, Point2D[] bridge) {
		if (!linkTowns(town1, town2))
			return false;
		bridges.add(bridge);
		return true;
	}
	
	private int checkTown(Town town) {
		// If no towns are linked
		if (towns.size() == 0) {
			return 0;
		}
		// Test if town exists in line
		for (int i = 0; i < towns.size(); i++) {
			if (town.equals(towns.get(i))) {
				return i; // town was found at position i
			}
		}
		return -1; // town was not found
	}
	
	public void addTown(Town town, int index) {
		if (towns.size() < 1) {
			towns.add(town);
		} else if (index == towns.size()) {
			towns.add(town);
		} else if (index == 0) {
			ArrayList<Town> temp = new ArrayList<>();
			temp.add(town);
			temp.addAll(towns);
			towns = temp;
		} else {
			List<Town> preTown	= towns.subList(0, index + 1);
			List<Town> postTown	= towns.subList(index, towns.size() - 1);
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
				Line line = (Line) factory.createLine(c, towns.get(i - 1).getPos(), 
						towns.get(i).getPos());
				for (Point2D[] b : bridges) {
					if (bridgeOnLine(line, b)) {
						line.addBridge(b);
					}
				}
				lines.add(line);					
			}
		}
	}
	
	private boolean bridgeOnLine(Line line, Point2D[] bridge) {
		// true if bridge exists on the line, false if not
		if(bridge.length > 2)
			if (!(line.getPos().angle(line.getMid()) == bridge[0].angle(bridge[1]) 
					&& line.getMid().angle(line.getDest()) == bridge[1].angle(bridge[2])))
				return false;
			else {
				if (!(line.getPos().angle(line.getMid()) == bridge[0].angle(bridge[1]) 
						|| line.getMid().angle(line.getDest()) == bridge[0].angle(bridge[1])))
					return false;
			}

		for (Point2D p : bridge) {
			boolean onLine = false;
			for (int i = 1; i < line.getCoords().length; i++) {
				if(Common.isBetween(line.getxCoords()[i - 1], line.getxCoords()[i], p.getX()) && Common.isBetween(line.getyCoords()[i - 1], line.getyCoords()[i], p.getY())) {
					onLine = true;
					break;
				}
			}
			if (!onLine)
				return false;
		}		
		return true;
	}
	
	
	
	public void removeTown(Town town) {
		for (Town t : towns) {
			if (t.equals(town)) {
				towns.remove(t);
				return;
			}
		}
	}
	
	public boolean addWagon() {
		if (wagons.size() >= MAX_WAGONS)
			return false;
		if (towns.size() < 1)
			return false;
		wagons.add(new Wagon(c, towns.get(0).getPos(), carrW, carrH, carriageImage));
		return true;
	}
	
	public void update(int coin, float deltaTime) {		
		for (Wagon wagon : wagons) {
			if (wagon.getPos().equals(wagon.getDest())) {
				processGoods(coin, wagon);
			} else {
				wagon.move(deltaTime);
			}
		}
	}
	
	public void draw(IDrawVisitor v) {
		for (Line l : lines) {
			l.draw(v, true);
		}
		for (Wagon wagon : wagons) {
			wagon.drawCarriages(v);
		}
	}
	
	private void processGoods(int coin, Wagon wagon) {
		for (int i = 0; i < towns.size(); i++) {
			Town t = towns.get(i);
			if (t.getPos().equals(wagon.getPos())) {
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
				return;
			}
		}
		
		// Wagon at midpoint of a line
		for (Line l : lines) {
			if (l.getMid().equals(wagon.getPos())) {
				if (wagon.isForward())
					wagon.setDest(l.getDest());
				else
					wagon.setDest(l.getPos());
				return;
			}
		}
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
}
