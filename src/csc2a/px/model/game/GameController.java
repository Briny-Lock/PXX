package csc2a.px.model.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import csc2a.px.model.abstract_factory.AbstractFactory;
import csc2a.px.model.abstract_factory.ShapeFactory;
import csc2a.px.model.shape.Circle;
import csc2a.px.model.shape.ESHAPE_TYPE;
import csc2a.px.model.shape.Rectangle;
import csc2a.px.model.shape.Shape;
import csc2a.px.model.shape.Triangle;
import csc2a.px.model.visitor.IDrawVisitor;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class GameController {
	private static final int NUM_ROUTES = 6;
	private static final int TICKS_TO_NEW_TOWN = 1000;
	private static final int CARR_W = 10;
	private static final int CARR_H = 20;
	
	private Color defC;
	private Color[] routeColors;
	private double townSize = 20;
	
	private Map map;
	private Route[] routes;
	private ArrayList<Town> towns;
	private AbstractFactory<Shape> factory;
	private ArrayList<ESHAPE_TYPE> availableShapes;
	private ArrayList<Integer> chances;
	private double ww;
	private double wh;
	
	private int tickCounter = 0;
	private float tickCorrection = 0;
	private float reputation = 10;

	private int coin = 100000;
	private int routeCost = 50;
	private int bridgeCost = 40;
	private int wagonCost = 20;
	private int carriageCost = 15;
	private int coinPerDelivery = 2;
	
	private EGAME_STATE gameState = EGAME_STATE.CONTINUE;
	
	public GameController(Color defC, Color[] routeColors, Image carriageImage, double ww, double wh) {
		this.defC = defC;
		this.routeColors = routeColors;
		factory = new ShapeFactory();
		routes = new Route[NUM_ROUTES];
		for (int i = 0; i < NUM_ROUTES; i++) {
			routes[i] = new Route(this.routeColors[i], carriageImage, CARR_W, CARR_H, coinPerDelivery);
			if (i < 3)
				routes[i].setUnlocked(true);
		}
		this.ww = ww;
		this.wh = wh;
	}

	public void initTowns() {
		ArrayList<Point2D> points = new ArrayList<>();

		boolean done = false;
		while (!done) {
			int failSafe = 0;
			Point2D point;
			boolean isTaken = false;
			boolean inRiver = false;			
			do {
				isTaken = false;
				failSafe++;
				Point2D[] drawable = map.getDrawable();
				point = getRandPos(drawable[0], drawable[1]);
				for (Point2D p : points) {
					if (p.distance(point) < townSize)
						isTaken = true;
				}
				inRiver = map.inRiver(point);
				if (!isTaken && !inRiver) {
					points.add(point);
					if (points.size() == 3) {
						done = true;
					}
				} else if (failSafe >= 200) {
					points.clear();
					failSafe = 0;
					map.calcDrawable(getRandPos(new Point2D(0, 0), new Point2D(ww, wh)));
				}
			} while (isTaken || inRiver);
		}
		towns.add(new Town(factory.createCircle(defC, points.get(0), townSize/2)));
		chances.add(1);
		towns.add(new Town(factory.createRectangle(defC, points.get(1), townSize, townSize)));
		chances.add(1);
		towns.add(new Town(factory.createTriangle(defC, points.get(2), townSize)));
		chances.add(1);
	}

	private Point2D getRandPos(Point2D p1, Point2D p2) {
		Random random = new Random();
		Point2D point = new Point2D(random.nextInt((int) (p2.getX() - p1.getX()) + 1) + (int) p1.getX(), 
				random.nextInt((int) (p2.getY() - p1.getY()) + 1) + (int) p1.getY());
		return new Point2D((point.getX() < p1.getX()) ? p1.getX() : Math.min(p2.getX(), point.getX()), 
						(point.getY() < p1.getY()) ? p1.getY() : Math.min(p2.getY(), point.getY()));
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
				if (rng < buffer) {
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
					if (rng < buffer) {
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
			route.update(coin, deltaTime);
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
		ArrayList<Point2D> points = getTownPos();
		do {
			isTaken = false;
			pos = getRandPos(drawable[0], drawable[1]);
			for (Point2D p : points) {
				if (pos.distance(p) < townSize)
					isTaken = true;
			}
		} while (isTaken);
		
		switch(type) {
		case CIRCLE:
			towns.add(new Town(factory.createCircle(Color.GRAY, pos, townSize/2)));
			return true;
		case RECTANGLE:
			towns.add(new Town(factory.createRectangle(Color.GRAY, pos, townSize, townSize)));
			return true;
		case TRIANGLE:
			towns.add(new Town(factory.createTriangle(Color.GRAY, pos, townSize)));
			return true;
		default:
			return false;
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

	public void setMap(Map map) {
		this.map = map;
		availableShapes = new ArrayList<>();
		initAvailableShapes();
		chances = new ArrayList<>();
		towns = new ArrayList<>();
		initTowns();
	}

	public Map getMap() {
		return map;
	}

	public EGAME_STATE getGameState() {
		return gameState;
	}

	public void setGameState(EGAME_STATE gameState) {
		this.gameState = gameState;
	}
}
