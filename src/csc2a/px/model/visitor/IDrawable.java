package csc2a.px.model.visitor;

/**
 * @author JC Swanzen (220134523)
 * @version PXX
 *
 */
public interface IDrawable {
	public void draw(IDrawVisitor v, boolean hasFill);
}
