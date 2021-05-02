package csc2a.px.model.visitor;

import csc2a.px.model.shape.Line;

public interface IDrawVisitor {
	public void visit(Line l);
//	public void visit(Circle c);
//	public void visit(Rectangle r);
//	public void visit(Triangle t);
}
