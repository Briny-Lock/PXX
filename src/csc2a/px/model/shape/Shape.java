/**
 * 
 */
package csc2a.px.model.shape;

import csc2a.px.model.visitor.IDrawVisitor;
import csc2a.px.model.visitor.IDrawable;
import javafx.scene.paint.Color;

/**
 * @author JC Swanzen (220134523)
 * @version PX
 *
 */
public abstract class Shape implements IDrawable{
	protected Color c;
	protected ESHAPE_TYPE type;
	
	public Shape(Color c, ESHAPE_TYPE type) {
		this.c = c;
		this.type = type;
	}
	
	@Override
	public abstract void draw(IDrawVisitor v);
	
	/**
	 * @return the c
	 */
	public Color getC() {
		return c;
	}

	/**
	 * @return the type
	 */
	public ESHAPE_TYPE getType() {
		return type;
	}
}
