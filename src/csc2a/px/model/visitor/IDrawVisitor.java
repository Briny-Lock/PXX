package csc2a.px.model.visitor;

import csc2a.px.model.game.Carriage;
import csc2a.px.model.shape.Circle;
import csc2a.px.model.shape.Line;
import csc2a.px.model.shape.Polygon;
import csc2a.px.model.shape.Rectangle;
import csc2a.px.model.shape.Triangle;

/**
 * @author JC Swanzen (220134523)
 * @version PXX
 *
 */
public interface IDrawVisitor {
	/**
	 * @param l Line visiting
	 */
	public void visit(Line l);
	/**
	 * @param c Circle visiting
	 * @param hasFill specifies whether shape should be filled
	 */
	public void visit(Circle c, boolean hasFill);
	/**
	 * @param r Rectangle visiting
	 * @param hasFill specifies whether shape should be filled
	 */
	public void visit(Rectangle r, boolean hasFill);
	/**
	 * @param t Triangle visiting
	 * @param hasFill specifies whether shape should be filled
	 */
	public void visit(Triangle t, boolean hasFill);
	/**
	 * @param c Carriage visiting
	 */
	public void visit(Carriage c);
	/**
	 * @param p Polygon visiting
	 * @param hasFill specifies whether shape should be filled
	 */
	public void visit(Polygon p, boolean hasFill);
}
