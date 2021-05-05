package csc2a.px.model.abstract_factory;

import csc2a.px.model.shape.Circle;
import csc2a.px.model.shape.Line;
import csc2a.px.model.shape.Rectangle;
import csc2a.px.model.shape.Triangle;
import csc2a.px.model.shape.Shape;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class ShapeFactory implements AbstractFactory<Shape> {

	@Override
	public Shape createLine(Color c, Point2D start, Point2D dest) {
		return new Line(c, start, dest);
	}

	@Override
	public Shape createLine(Color c,  Point2D start, Point2D dest, double[] bridgeXCoords,
			double[] bridgeYCoords) {
		return new Line(c, start, dest, bridgeXCoords, bridgeYCoords);
	}

	@Override
	public Shape createCircle(Color c, Point2D pos, double r) {
		return new Circle(c, pos, r);
	}

	@Override
	public Shape createTriangle(Color c, Point2D refPos, double size) {
		return new Triangle(c, refPos, size);
	}

	@Override
	public Shape createRectangle(Color c, Point2D pos, double w, double h) {
		return new Rectangle(c, pos, w, h);
	}

}
