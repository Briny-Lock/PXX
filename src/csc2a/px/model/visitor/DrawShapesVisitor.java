package csc2a.px.model.visitor;

import java.awt.Color;

import csc2a.px.model.game.Carriage;
import csc2a.px.model.shape.Circle;
import csc2a.px.model.shape.Line;
import csc2a.px.model.shape.Rectangle;
import csc2a.px.model.shape.Triangle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;

public class DrawShapesVisitor implements IDrawVisitor {
	private GraphicsContext gc;
	
	/**
	 * @param gc the gc to set
	 */
	public void setGc(GraphicsContext gc) {
		this.gc = gc;
	}
	
	@Override
	public void visit(Line l) {
		clear();
		gc.setStroke(l.getC());
		gc.setLineWidth(5);
		gc.strokeLine(l.getxCoords()[0], l.getyCoords()[0], l.getxCoords()[1], l.getyCoords()[1]);
		gc.strokeLine(l.getxCoords()[1], l.getyCoords()[1], l.getxCoords()[2], l.getyCoords()[2]);
		
		if (l.getBridgeXCoords() != null && l.getBridgeYCoords() != null && l.getBridgeXCoords().length == l.getBridgeYCoords().length) {
			gc.setStroke(l.getBridgeColor());
			gc.setLineWidth(7);
			for (int i = 1; i < l.getBridgeXCoords().length; i++) {
				gc.strokeLine(l.getBridgeXCoords()[i - 1], l.getBridgeYCoords()[i - 1], l.getBridgeXCoords()[i], l.getBridgeYCoords()[i]);
			}
		}
		
		
		gc.stroke();
	}

	@Override
	public void visit(Circle c, boolean hasFill) {
		clear();
		gc.setLineWidth(3);
		if (hasFill) {
			gc.setFill(c.getC());
			gc.fillOval(c.getX(), c.getY(), c.getR() * 2, c.getR() * 2);
		} else {
			gc.setStroke(c.getC());
			gc.strokeOval(c.getX(), c.getY(), c.getR() * 2, c.getR() * 2);
		}
	}

	@Override
	public void visit(Rectangle r, boolean hasFill) {
		clear();
		gc.setLineWidth(3);
		if (hasFill) {
			gc.setFill(r.getC());
			gc.fillRect(r.getX() - r.getW()/2, r.getY() - r.getH()/2, r.getW(), r.getH());
		} else {
			gc.setStroke(r.getC());
			gc.strokeRect(r.getX() - r.getW()/2, r.getY() - r.getH()/2, r.getW(), r.getH());
		}
	}

	@Override
	public void visit(Triangle t, boolean hasFill) {
		clear();
		gc.setLineWidth(3);
		if (hasFill) {
			gc.setFill(t.getC());
			gc.fillPolygon(t.getxCoords(), t.getyCoords(), 3);
		} else {
			gc.setStroke(t.getC());
			gc.strokePolygon(t.getxCoords(), t.getyCoords(), 3);
		}		
	}

	@Override
	public void visit(Carriage c) {
		clear();
		float[] hsb = Color.RGBtoHSB((int) c.getC().getRed(), (int) c.getC().getGreen(), (int) c.getC().getBlue(), null);
		ColorAdjust effect = new ColorAdjust();
		effect.setHue(hsb[0]);
		effect.setSaturation(hsb[1]);
		effect.setBrightness(hsb[2]);
		gc.setEffect(effect);
		gc.drawImage(c.getCarriageImage(), c.getPosition().getX(), c.getPosition().getY());
	}
	
	public void clear() {
		gc.setEffect(null);
	}
	
}
