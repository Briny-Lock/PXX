package csc2a.px.model.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import csc2a.px.model.game.Route;
import csc2a.px.model.game.Town;
import csc2a.px.model.shape.Circle;
import csc2a.px.model.shape.ESHAPE_TYPE;
import csc2a.px.model.shape.Rectangle;
import csc2a.px.model.shape.Shape;
import csc2a.px.model.shape.Triangle;
import csc2a.px.model.visitor.DrawShapesVisitor;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class GameCanvas extends Canvas {
	ESHAPE_TYPE[] shapes;
	int[] chances;
	ArrayList<Town> towns;
	DrawShapesVisitor visitor;
	Route route;
	
	public GameCanvas() {
		setWidth(500);
		setHeight(500);
		
		visitor = new DrawShapesVisitor();
		visitor.setGc(getGraphicsContext2D());
		
		
	}
	
	public void setShapes(ESHAPE_TYPE[] shapes) {
		this.shapes = shapes;
		
	}
	
	public void bindProperties(DoubleProperty widthProperty, DoubleProperty heightProperty) {
		widthProperty.bind(widthProperty);
		heightProperty.bind(heightProperty);
	}
	
	public void redrawCanvas(float deltaTime) {
		getGraphicsContext2D().clearRect(0, 0, this.getWidth(), this.getHeight());
		visitor.setGc(getGraphicsContext2D());
		route.update(visitor, 0, deltaTime);
		//TODO Move to GameSpace
		Random random = new Random();
		for (Town town : towns) {
			if (town.generateGoods()) {
				int total = 0;
				for (int i : chances) {
					total += i;
				}
				int rng = random.nextInt(total) + 1;
				int buffer = 0;
				for (int i = 0; i < chances.length; i++) {
					buffer += chances[i];
					if (rng < buffer) {
						if (shapes[i] != town.getShape().getType()) {
							town.addGoods(createGoods(shapes[i], town.getPos()));
							break;
						}
					}
				}
			}
		}
	}
	
	//TODO Move to GameSpace
	public Shape createGoods(ESHAPE_TYPE type, Point2D pos) {
		switch(type) {
		case CIRCLE:
			return new Circle(Color.GRAY, pos, 4);
		case RECTANGLE:
			return new Rectangle(Color.GRAY, pos, 4, 4);
		case TRIANGLE:
			return new Triangle(Color.GRAY, pos, 2);
		default:
			return null;
		}
	}
	
	public void setImage(Image image) {
		towns = new ArrayList<>(Arrays.asList(
				new Town(new Circle(Color.GRAY, new Point2D(100, 400), 10)),
				new Town(new Circle(Color.RED, new Point2D(50, 50), 10)),
				new Town(new Rectangle(Color.GRAY, new Point2D(100, 50), 10, 20)),
				new Town(new Triangle(Color.ALICEBLUE, new Point2D(250, 100), 20))));
		
		route = new Route(Color.PURPLE, image, 10, 20, 0);
		
		shapes = new ESHAPE_TYPE[] {ESHAPE_TYPE.CIRCLE, ESHAPE_TYPE.RECTANGLE, ESHAPE_TYPE.TRIANGLE};
		chances = new int[] {(4-2), (4-1), (4-1)};
		
		route.linkTowns(towns.get(0), towns.get(1));
		route.linkTowns(towns.get(1), towns.get(2));
		route.linkTowns(towns.get(2), towns.get(3));
		route.addWagon();
	}
}
