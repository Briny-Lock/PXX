package csc2a.px.model.ui;

import csc2a.px.model.game.GameController;
import csc2a.px.model.visitor.DrawShapesVisitor;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseButton;

/**
 * @author JC Swanzen (220134523)
 * @version PXX
 *
 */
public class GameCanvas extends Canvas {
	private DrawShapesVisitor visitor;
	private GameController controller;
	private Point2D dragStart, dragEnd;
	private boolean isDragged = false;
	private boolean isPaused = true;
	
	public GameCanvas() {		
		visitor = new DrawShapesVisitor();
		visitor.setGc(getGraphicsContext2D(), this.getWidth(), this.getHeight());
		
		setOnMousePressed(event -> {
			if (!isPaused) {
				if (event.getButton() == MouseButton.PRIMARY)
					dragStart = new Point2D(event.getX(), event.getY());
			}
		});
		
		setOnMouseDragged(event -> {
			if (!isPaused) {
				if (event.getButton() == MouseButton.PRIMARY)
					dragEnd = new Point2D(event.getX(), event.getY());
				else
					dragEnd = null;
				isDragged = true;
			}
		});

		setOnMouseReleased(event -> {
			if (!isPaused) {
				isDragged = false;
				if (event.getButton() == MouseButton.PRIMARY) {
					Point2D dragEnd = new Point2D(event.getX(), event.getY());
					if (controller.isNearTown(dragStart) && controller.isNearTown(dragEnd)) {
						controller.linkTowns(dragStart, dragEnd);
					}
				} else if (event.getButton() == MouseButton.SECONDARY) {
					Point2D pos = new Point2D(event.getX(), event.getY());
					controller.removeTown(pos);
				}
			}
		});
	}
	
	public void redrawCanvas() {
		visitor.setGc(getGraphicsContext2D(), this.getWidth(), this.getHeight());
		if(isDragged)
			drawDragLine();
		controller.draw(visitor);
	}
	
	public void drawDragLine() {
		if (!isPaused) {
			if (controller.isNearTown(dragStart) && dragEnd != null && dragStart != null) {
				getGraphicsContext2D().setLineWidth(3);
				getGraphicsContext2D().setStroke(controller.getCurrentColor());
				getGraphicsContext2D().strokeLine(dragStart.getX(), dragStart.getY(), dragEnd.getX(), dragEnd.getY());
			}
		}
	}
	
	public void clear() {
		getGraphicsContext2D().clearRect(0, 0, getWidth(), getHeight());
	}
	
	public void setController(GameController controller) { this.controller = controller; }

	public void play() {
		this.isPaused = false;
	}

	public void pause() {
		this.isPaused = true;
	}
}
