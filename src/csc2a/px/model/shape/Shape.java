/**
 * 
 */
package csc2a.px.model.shape;

import csc2a.px.model.visitor.IDrawVisitor;
import csc2a.px.model.visitor.IDrawable;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

/**
 * @author JC Swanzen (220134523)
 * @version PX
 *
 */
public abstract class Shape implements IDrawable{
	protected Point2D pos;
	protected Color c;
	protected ESHAPE_TYPE type;
	
	public Shape(Color c, ESHAPE_TYPE type, Point2D pos) {
		this.c = c;
		this.type = type;
		this.pos = pos;
	}
	
	@Override
	public abstract void draw(IDrawVisitor v, boolean hasFill);
	
	/**
	 * @return the c
	 */
	public Color getC() {
		return c;
	}
	
	/**
	 * @param c the c
	 */
	public void setC(Color c) {
		this.c = c;
	}

	/**
	 * @return the type
	 */
	public ESHAPE_TYPE getType() {
		return type;
	}
	
	public Point2D getPos() { return pos; }
	public void setPos(Point2D pos) { this.pos = pos; }
}
