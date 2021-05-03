package csc2a.px.model.abstract_factory;

import csc2a.px.model.shape.Circle;
import csc2a.px.model.shape.Line;
import csc2a.px.model.shape.Rectangle;
import csc2a.px.model.shape.Triangle;
import csc2a.px.model.shape.Shape;
import javafx.scene.paint.Color;

public class ShapeFactory implements AbstractFactory<Shape> {

	@Override
	public Shape createLine(Color c, double startX, double startY, double destX, double destY) {
		return new Line(c, startX, startY, destX, destY);
	}

	@Override
	public Shape createLine(Color c, double startX, double startY, double destX, double destY, double[] bridgeXCoords,
			double[] bridgeYCoords) {
		return new Line(c, startX, startY, destX, destY, bridgeXCoords, bridgeYCoords);
	}

	@Override
	public Shape createCircle(Color c, double x, double y, double r) {
		return new Circle(c, x, y, r);
	}

	@Override
	public Shape createTriangle(Color c, double centX, double centY, double size) {
		return new Triangle(c, centX, centY, size);
	}

	@Override
	public Shape createRectangle(Color c, double x, double y, double w, double h) {
		return new Rectangle(c, x, y, w, h);
	}

}
