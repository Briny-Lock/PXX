package csc2a.px.model.visitor;

import csc2a.px.model.game.Carriage;
import csc2a.px.model.shape.Circle;
import csc2a.px.model.shape.Line;
import csc2a.px.model.shape.Polygon;
import csc2a.px.model.shape.Rectangle;
import csc2a.px.model.shape.Triangle;

public interface IDrawVisitor {
	public void visit(Line l);
	public void visit(Circle c, boolean hasFill);
	public void visit(Rectangle r, boolean hasFill);
	public void visit(Triangle t, boolean hasFill);
	public void visit(Carriage c);
	public void visit(Polygon p, boolean hasFill);
}
