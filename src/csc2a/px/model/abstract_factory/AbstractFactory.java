package csc2a.px.model.abstract_factory;

import csc2a.px.model.visitor.IDrawable;
import javafx.scene.paint.Color;

public interface AbstractFactory<T extends IDrawable> {
	public abstract T createLine(Color c, double startX, double startY, double destX, double destY);
	public abstract T createLine(Color c, double startX, double startY, double destX, double destY, double[] bridgeXCoords, double[] bridgeYCoords);
	public abstract T createCircle(Color c, double x, double y, double r);
	public abstract T createTriangle(Color c, double centX, double centY, double size);
	public abstract T createRectangle(Color c, double x, double y, double w, double h);	
}