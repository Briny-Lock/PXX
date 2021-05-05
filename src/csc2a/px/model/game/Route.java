/**
 * 
 */
package csc2a.px.model.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import csc2a.px.model.abstract_factory.ShapeFactory;
import csc2a.px.model.shape.Line;
import javafx.geometry.Point2D;

/**
 * @author JC Swanzen (220134523)
 * @version PXX
 *
 */
public class Route {
	private ShapeFactory factory;
	private ArrayList<Line> lines;
	private ArrayList<Wagon> wagons;
	private ArrayList<Town> towns;
	
	public Route() {
		lines = new ArrayList<>();
		wagons = new ArrayList<>();
		towns = new ArrayList<>();
		factory = new ShapeFactory();
	}
	
	public void linkTowns(Town town1, Town town2) {
		int check = checkTown(town1);
		if (check == -1) {
			check = checkTown(town2);
			if (check == -1)
				return;
			addTown(town2, check);
		}
		if (check == 0) {
			towns.add(town1);
			towns.add(town2);
			renderLines();
			return;
		}
		addTown(town1, check);
	}
	
	private int checkTown(Town town) {
		// If no towns are linked
		if (towns.size() == 0) {
			return 0;
		}
		// Test if town exists in line
		for (int i = 0; i < towns.size(); i++) {
			if (town.equals(towns.get(i))) {
				return i; // town was found at position i
			}
		}
		return -1; // town was not found
	}
	
	public void addTown(Town town, int index) {
		List<Town> preTown	= towns.subList(0, index);
		List<Town> postTown	= towns.subList(index, towns.size() - 1);
		towns = new ArrayList<>(preTown);
		towns.add(town);
		towns.addAll(postTown);
	}
	
	public void renderLines() {
		
	}
	
	public void removeTown(Town town) {
		for (Town t : towns) {
			if (t.equals(town)) {
				towns.remove(t);
				return;
			}
		}
	}
	
	private void addLine(Point2D start, Point2D dest) {
		lines.add(factory.ne)
	}
	
	private void removeLine(Point2D start, Point2D dest) {
		
	}
}
