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
	private Color[] routeColors = {Color.RED, Color.BLUE, Color.PURPLE, Color.YELLOW, Color.DEEPSKYBLUE, Color.HOTPINK};
	private double townSize = 20;
	
	private Map map;
	private Image carriageImage;
	private Route[] routes;
	private ArrayList<Town> towns;
	private AbstractFactory<Shape> factory;
	private ArrayList<ESHAPE_TYPE> availableShapes;
	private ArrayList<Integer> chances;
	
	private int tickCounter = 0;
	private float tickCorrection = 0;
	private int reputation = 10;

	private int coin = 100000;
	private int routeCost = 50;
	private int bridgeCost = 40;
	private int wagonCost = 20;
	private int carriageCost = 15;
	private int coinPerDelivery = 2;
	
	public GameController(Map map, Image carriageImage) {
		factory = new ShapeFactory();
		this.map = map;
		this.carriageImage = carriageImage;
		routes = new Route[NUM_ROUTES];
		for (int i = 0; i < NUM_ROUTES; i++) {
			routes[i] = new Route(routeColors[i], carriageImage, CARR_W, CARR_H, coinPerDelivery);
			if (i < 3)
				routes[i].setUnlocked(true);
		}
		availableShapes = new ArrayList<>();
		initAvailableShapes();
		chances = new ArrayList<>();
		towns = new ArrayList<>();
		initTowns();
		demo();
	}

	public void initTowns() {
		ArrayList<Point2D> points = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			Point2D point = getRandPos();
			while (points.contains(point)) {
				point = getRandPos();
			}
			points.add(point);
		}
		towns.add(new Town(factory.createCircle(defC, points.get(0), townSize/2)));
		chances.add(1);
		towns.add(new Town(factory.createRectangle(defC, points.get(1), townSize, townSize)));
		chances.add(1);
		towns.add(new Town(factory.createTriangle(defC, points.get(2), townSize)));
		chances.add(1);
	}

	private Point2D getRandPos() {
		Random random = new Random();
		Point2D[] drawable = map.getDrawable();
		return new Point2D(random.nextInt((int) (drawable[2].getX() - drawable[0].getX()) + 1) + (int) drawable[0].getX(), 
				random.nextInt((int) (drawable[2].getY() - drawable[0].getY()) + 1) + (int) drawable[0].getY());
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
		this.carriageImage = carriageImage;
		for (Route route : routes) {
			route.setCarriageImage(carriageImage);
		}		
	}
	
	private void demo() {
		towns = new ArrayList<>(Arrays.asList(
				new Town(new Circle(Color.GRAY, new Point2D(100, 400), 10)),
				new Town(new Circle(Color.RED, new Point2D(50, 50), 10)),
				new Town(new Rectangle(Color.GRAY, new Point2D(100, 50), 10, 20)),
				new Town(new Triangle(Color.ALICEBLUE, new Point2D(250, 100), 20))));
				
		routes[0].linkTowns(towns.get(0), towns.get(1));
		routes[0].linkTowns(towns.get(1), towns.get(2));
		routes[0].linkTowns(towns.get(2), towns.get(3));
		routes[0].addWagon();
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
		map.updateDrawable();
		Random random = new Random();
		
		reputation++;
		
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
		Point2D pos = getRandPos();
		ArrayList<Point2D> points = getTownPos();
		while (points.contains(pos)) {
			pos = getRandPos();
		}
		
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
	}

	public Map getMap() {
		return map;
	}
}
