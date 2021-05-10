package csc2a.px.model.game;

import java.util.ArrayList;
import java.util.Random;

import csc2a.px.model.Common;
import csc2a.px.model.abstract_factory.AbstractFactory;
import csc2a.px.model.abstract_factory.ShapeFactory;
import csc2a.px.model.shape.Circle;
import csc2a.px.model.shape.ESHAPE_TYPE;
import csc2a.px.model.shape.Shape;
import csc2a.px.model.visitor.IDrawVisitor;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class GameController {
	private static final int NUM_ROUTES = 6;
	private static final int TICKS_TO_NEW_TOWN = 400;
	private static final int CARR_W = 10;
	private static final int CARR_H = 20;
	private static final int START_COIN = 10000;
	private static final int START_REP = 10;
	private static final int BORDER_ADJUSTMENT = 40;
	
	
	private Color defC;
	private Color[] routeColors;
	private double townSize = 20;
	private Circle colorCircle;
	
	private Map map;
	private Route[] routes;
	private int currentRoute;
	private ArrayList<Town> towns;
	private AbstractFactory<Shape> factory;
	private ArrayList<ESHAPE_TYPE> availableShapes;
	private ArrayList<Integer> chances;
	private double ww;
	private double wh;
	
	private int tickCounter = 0;
	private float tickCorrection = 0;
	private float reputation = START_REP;

	private int coin = START_COIN;
	private int routeCost = 50;
	private int bridgeCost = 40;
	private int wagonCost = 20;
	private int carriageCost = 15;
	private int coinPerDelivery = 1;
	
	private EGAME_STATE gameState = EGAME_STATE.CONTINUE;
	
	public GameController(Color defC, Color[] routeColors, Image carriageImage) {
		this.defC = defC;
		this.routeColors = routeColors;
		factory = new ShapeFactory();
		routes = new Route[NUM_ROUTES];
		for (int i = 0; i < NUM_ROUTES; i++) {
			routes[i] = new Route(this.routeColors[i], defC, carriageImage, CARR_W, CARR_H, coinPerDelivery);
			if (i < 3)
				routes[i].setUnlocked(true);
		}
		setCurrentRoute(1);
	}

	public void initTowns() {
		ArrayList<Point2D> points = new ArrayList<>();

		boolean done = false;
		while (!done) {
			int failSafe = 0;
			Point2D point;
			boolean isTaken = false;
			boolean valid = true;			
			do {
				isTaken = false;
				valid = true;
				failSafe++;
				Point2D[] drawable = map.getDrawable();
				point = getRandPos(drawable[0], drawable[1]);
				if (((point.getX() - BORDER_ADJUSTMENT) < 0) || ((point.getX() + BORDER_ADJUSTMENT) > ww))
					valid = false;
				if (((point.getY() - BORDER_ADJUSTMENT) < 0) || ((point.getY() + BORDER_ADJUSTMENT) > wh))
					valid = false;
				if (map.inRiver(point))
					valid = false;
				for (Point2D p : points) {
					if (p.distance(point) < townSize + 10)
						isTaken = true;
				}
				if (!isTaken && valid) {
					points.add(point);
					if (points.size() == 3) {
						done = true;
					}
				} else if (failSafe >= 200) {
					points.clear();
					failSafe = 0;
					map.calcDrawable(getRandPos(new Point2D(1, 1), new Point2D(ww, wh)));
				}
			} while (isTaken || !valid);
		}
		towns.add(new Town(factory.createCircle(defC, points.get(0), townSize/2), townSize, defC));
		chances.add(1);
		towns.add(new Town(factory.createRectangle(defC, points.get(1), townSize, townSize), townSize, defC));
		chances.add(1);
		towns.add(new Town(factory.createTriangle(defC, points.get(2), townSize), townSize, defC));
		chances.add(1);
	}

	private Point2D getRandPos(Point2D p1, Point2D p2) {
		Random random = new Random();
		Point2D point = new Point2D(random.nextInt((int) (p2.getX() - p1.getX()) + 1) + (int) p1.getX(), 
				random.nextInt((int) (p2.getY() - p1.getY()) + 1) + (int) p1.getY());
		return point;
	}
	
	public Shape createGoods(ESHAPE_TYPE type, Point2D pos) {
		switch(type) {
		case CIRCLE:
			return factory.createCircle(defC, pos, 1);
		case RECTANGLE:
			return factory.createRectangle(defC, pos, 2, 2);
		case TRIANGLE:
			return factory.createTriangle(defC, pos, 2);
		default:
			return null;
		}
	}

	public void setCarriageImage(Image carriageImage) {
		for (Route route : routes) {
			route.setCarriageImage(carriageImage);
		}		
	}
	
	public void initAvailableShapes() {
		availableShapes.add(ESHAPE_TYPE.CIRCLE);
		availableShapes.add(ESHAPE_TYPE.RECTANGLE);
		availableShapes.add(ESHAPE_TYPE.TRIANGLE);
	}
	
	public boolean generateTown(float deltaTime) {
		tickCorrection += deltaTime;
		while (tickCorrection > 1) {
			tickCounter++;
			tickCorrection -= 1;
		}
		if (tickCounter >= TICKS_TO_NEW_TOWN) {
			tickCounter = 0;
			return true;
		} else {
			tickCounter++;
			return false;
		}
	}
	
	public void update(float deltaTime) {
		if (reputation <= 0) {
			setGameState(EGAME_STATE.LOSE);
			return;
		}
		reputation += deltaTime/10;
		if (reputation > 100) {
			setGameState(EGAME_STATE.WIN);
			return;
		}
		map.updateDrawable();
		Random random = new Random();
		
		if (generateTown(deltaTime)) {
			int total = 0;
			for (int i : chances) {
				total += i;
			}
			int rng = random.nextInt(total) + 1;
			int buffer = 0;
			for (int i = 0; i < chances.size(); i++) {
				buffer += chances.get(i);
				if (rng == buffer) {
					addTown(availableShapes.get(i));
					break;
				}
			}
		}
		
		for (Town town : towns) {
			if (town.generateGoods(deltaTime)) {
				int total = 0;
				for (int i : chances) {
					total += i;
				}
				int rng = random.nextInt(total) + 1;
				int buffer = 0;
				for (int i = 0; i < chances.size(); i++) {
					buffer += chances.get(i);
					if (rng == buffer) {
						if (availableShapes.get(i) != town.getShape().getType()) {
							town.addGoods(createGoods(availableShapes.get(i), town.getPos()));
							break;
						}
					}
				}
			}
			reputation -= town.getReputationLoss() * deltaTime;
		}
		
		for (Route route : routes) {
			coin += route.update(deltaTime);
		}
	}

	private ArrayList<Point2D> getTownPos() {
		ArrayList<Point2D> points = new ArrayList<>();
		for (Town town : towns) {
			points.add(town.getPos());
		}
		return points;
	}
	
	private boolean addTown(ESHAPE_TYPE type) {
		boolean isTaken;
		Point2D pos = null;
		Point2D[] drawable = map.getDrawable();
		boolean valid = true;
		ArrayList<Point2D> points = getTownPos();
		do {
			valid = true;
			isTaken = false;
			pos = getRandPos(drawable[0], drawable[1]);
			if (((pos.getX() - BORDER_ADJUSTMENT) < 0) || (pos.getX() + BORDER_ADJUSTMENT > ww))
				valid = false;
			if (((pos.getY() - BORDER_ADJUSTMENT) < 0) || (pos.getY() + BORDER_ADJUSTMENT > wh))
				valid = false;
			if (map.inRiver(pos))
					valid = false;
			for (Point2D p : points) {
				if (pos.distance(p) < townSize + 10)
					isTaken = true;
			}
		} while (isTaken || !valid);
		
		switch(type) {
		case CIRCLE:
			towns.add(new Town(factory.createCircle(defC, pos, townSize/2), townSize, defC));
			updateShapeChance(ESHAPE_TYPE.CIRCLE);
			return true;
		case RECTANGLE:
			towns.add(new Town(factory.createRectangle(defC, pos, townSize, townSize), townSize, defC));
			updateShapeChance(ESHAPE_TYPE.RECTANGLE);
			return true;
		case TRIANGLE:
			towns.add(new Town(factory.createTriangle(defC, pos, townSize), townSize, defC));
			updateShapeChance(ESHAPE_TYPE.TRIANGLE);
			return true;
		default:
			return false;
		}
	}
	
	private void updateShapeChance(ESHAPE_TYPE type) {
		for (int i = 0; i < availableShapes.size(); i++) {
			if (availableShapes.get(i) == type) {
				if (i < chances.size())
					chances.set(i, chances.get(i) + 1);
				else
					chances.add(1);
				return;
			}
		}
		availableShapes.add(type);
		chances.add(1);
	}

	public void linkTowns(Point2D p1, Point2D p2) {
		if (isNearTown(p1) && isNearTown(p2) && routes[currentRoute].isUnlocked()) {
			if (map.hasBridge(p1, p2)) {
				System.out.println("Has Bridge");
				if (coin < bridgeCost)
					return;
				else if (routes[currentRoute].linkTowns(getNearestTown(p1), getNearestTown(p2))) {
					coin -= bridgeCost;
					return;
				}
			}
			routes[currentRoute].linkTowns(getNearestTown(p1), getNearestTown(p2));
		}
	}

	public void draw(IDrawVisitor v) {
		map.draw(v);
		for (Route route : routes) {
			route.draw(v);
		}
		for (Town town : towns) {
			town.drawAll(v);
		}
		colorCircle.draw(v, true);
	}

	public int getCarriageCost() {
		return carriageCost;
	}

	public void setCarriageCost(int carriageCost) {
		this.carriageCost = carriageCost;
	}

	public int getWagonCost() {
		return wagonCost;
	}

	public void setWagonCost(int wagonCost) {
		this.wagonCost = wagonCost;
	}

	public int getBridgeCost() {
		return bridgeCost;
	}

	public void setBridgeCost(int bridgeCost) {
		this.bridgeCost = bridgeCost;
	}

	public int getRouteCost() {
		return routeCost;
	}

	public void setRouteCost(int routeCost) {
		this.routeCost = routeCost;
	}

	public int getCoin() {
		return coin;
	}

	public int getReputation() {
		return (int) reputation;
	}

	public int getCoinPerDelivery() {
		return coinPerDelivery;
	}

	public void setMap(Map map) {
		this.map = map;
		reset();
	}

	public Map getMap() {
		return map;
	}
	
	public Color getCurrentColor() {
		return routes[currentRoute].getC();
	}

	public EGAME_STATE getGameState() {
		return gameState;
	}

	public void setGameState(EGAME_STATE gameState) {
		this.gameState = gameState;
	}
	
	public boolean isNearTown(Point2D pos) {
		if (pos == null)
			return false;
		for (Town town : towns) {
			if (pos.distance(town.getPos()) < townSize * 2) {
				return true;
			}
		}
		return false;
	}
	
	private Town getNearestTown(Point2D pos) {
		if (pos == null)
			return null;
		for (Town town : towns) {
			if (pos.distance(town.getPos()) < 5) {
				return town;
			}
		}
		return null;
	}

	public void removeTown(Point2D pos) {
		if (isNearTown(pos))
			routes[currentRoute].removeTown(getNearestTown(pos));
	}

	public void purchaseRoute() {
		if (coin > routeCost)
			for (Route route : routes) {
				if (!route.isUnlocked()) {
					coin -= routeCost;
					route.setUnlocked(true);
					return;
				}
				if (route == routes[currentRoute])
					setCurrentRoute(currentRoute + 1); 
			}
	}

	public void addWagon() {
		if (routes[currentRoute].isUnlocked()) {
			if (coin > wagonCost) {
				if (routes[currentRoute].addWagon()) {
					coin -= wagonCost;
				}				
			}
		}
	}

	public void addCarriage() {
		if (routes[currentRoute].isUnlocked()) {
			if (coin > carriageCost) {
				if (routes[currentRoute].addCarriage()) {
					coin -= wagonCost;
				}				
			}
		}
	}

	public void setCurrentRoute(int routeCounter) {
		if (Common.isBetween(1, routes.length + 1, routeCounter))
			currentRoute = routeCounter - 1;
		colorCircle = new Circle((routes[currentRoute].isUnlocked()) ? getCurrentColor() : Color.GRAY, 
				new Point2D(ww - 15, wh - 15), 10);
	}
	
	public void setSize(double w, double h) {
		ww = w;
		wh = h;
	}
	
	public void reset() {
		setSize(map.getWw(), map.getWh());
		availableShapes = new ArrayList<>();
		initAvailableShapes();
		chances = new ArrayList<>();
		towns = new ArrayList<>();
		initTowns();
		for (int i = 0; i < routes.length; i++) {
			routes[i].clear();
			if(i < 3)
				routes[i].setUnlocked(true);
			else
				routes[i].setUnlocked(false);
		}
		coin = START_COIN;
		reputation = START_REP;
		setCurrentRoute(1);
	}
}
