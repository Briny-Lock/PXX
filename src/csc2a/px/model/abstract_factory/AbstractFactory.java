package csc2a.px.model.abstract_factory;

import csc2a.px.model.visitor.IDrawable;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

/**
 * @author JC Swanzen (220134523)
 * @version PXX
 *
 */
public interface AbstractFactory<T extends IDrawable> {
	public abstract T createLine(Color c, Point2D start, Point2D dest);
	public abstract T createCircle(Color c, Point2D pos, double r);
	public abstract T createTriangle(Color c, Point2D refPos, double size);
	public abstract T createRectangle(Color c, Point2D pos, double w, double h);	
}