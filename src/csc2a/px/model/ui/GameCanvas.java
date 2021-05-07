package csc2a.px.model.ui;

import java.util.ArrayList;
import java.util.Arrays;

import csc2a.px.model.game.Route;
import csc2a.px.model.game.Town;
import csc2a.px.model.game.Wagon;
import csc2a.px.model.shape.Circle;
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
	ArrayList<Shape> shapes;
	Wagon wagon;
	DrawShapesVisitor visitor;
	Route route;
	
	public GameCanvas() {
		setWidth(500);
		setHeight(500);
		
		visitor = new DrawShapesVisitor();
		visitor.setGc(getGraphicsContext2D());
		
		
	}
	
	public void setShapes(ArrayList<Shape> shapes) {
		this.shapes = shapes;
		
	}
	
	public void addWagon(Wagon wagon) {
		this.wagon = wagon;
	}
	
	public void bindProperties(DoubleProperty widthProperty, DoubleProperty heightProperty) {
		widthProperty.bind(widthProperty);
		heightProperty.bind(heightProperty);
	}
	
	public void redrawCanvas(float deltaTime) {
		getGraphicsContext2D().clearRect(0, 0, this.getWidth(), this.getHeight());
		visitor.setGc(getGraphicsContext2D());
		wagon.move(deltaTime);
		wagon.drawCarriages(visitor);
		route.update(visitor, 0, deltaTime);
	}
	
	public void setImage(Image image) {
		ArrayList<Town> towns = new ArrayList<>(Arrays.asList(
				new Town(new Circle(Color.RED, new Point2D(50, 50), 10)),
				new Town(new Rectangle(Color.GRAY, new Point2D(100, 50), 10, 20)),
				new Town(new Triangle(Color.ALICEBLUE, new Point2D(250, 100), 20))));
		
		route = new Route(Color.PURPLE, image, 10, 20, 0);
		
		route.linkTowns(towns.get(0), towns.get(1));
		route.linkTowns(towns.get(1), towns.get(2));
	}
}
