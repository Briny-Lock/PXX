package csc2a.px.model.visitor;

public interface IDrawable {
	public void draw(IDrawVisitor v, boolean hasFill);
}
