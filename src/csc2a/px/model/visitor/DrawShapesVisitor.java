package csc2a.px.model.visitor;

import csc2a.px.model.shape.Line;
import javafx.scene.canvas.GraphicsContext;

public class DrawShapesVisitor implements IDrawVisitor {
	private GraphicsContext gc;
	
	@Override
	public void visit(Line l) {
		gc.setFill(l.getC());
		gc.setLineWidth(5);
		gc.strokeLine(l.getxCoords()[0], l.getxCoords()[0], l.getxCoords()[1], l.getxCoords()[1]);
		gc.strokeLine(l.getxCoords()[1], l.getxCoords()[1], l.getxCoords()[2], l.getxCoords()[2]);
		gc.stroke();
	}

}
