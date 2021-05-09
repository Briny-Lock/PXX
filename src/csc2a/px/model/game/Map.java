package csc2a.px.model.game;

import java.util.ArrayList;
import java.util.Random;

import csc2a.px.model.Common;
import csc2a.px.model.shape.ESHAPE_TYPE;
import csc2a.px.model.shape.Polygon;
import csc2a.px.model.shape.Rectangle;
import csc2a.px.model.visitor.IDrawVisitor;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class Map {
	/**
	 * 
	 */
	private static final int INIT_AREA_SIZE = 70;
	private static final float AREA_SHIFT = 0.2f;
	
	private Rectangle land;
	private ArrayList<Polygon> rivers;
	private Color landC;
	private Color riverC;
	private double ww;
	private double wh;
	
	private Point2D[] drawable;
	
	public Map(Color landC, Color riverC, double ww, double wh) {
		this.landC = landC; 
		this.riverC = riverC;
		this.ww = ww;
		this.wh = wh;
		land = new Rectangle(landC, new Point2D(ww/2, wh/2), ww, wh);
		rivers = new ArrayList<>();
	};
	
	public void calcDrawable(Point2D refPos) {
		boolean isSafe = false;
		Point2D centre = refPos;
		drawable = new Point2D[2];
		while (!isSafe) {
			if (!inRiver(centre)) {
				drawable[0] = centre.subtract(INIT_AREA_SIZE, INIT_AREA_SIZE); // Top Left
				drawable[1] = centre.add(INIT_AREA_SIZE, INIT_AREA_SIZE); // Bottom Right
				isSafe = true;
			} else {
				centre = centre.add(randPoint(-INIT_AREA_SIZE, INIT_AREA_SIZE));
				centre = new Point2D((int) ((centre.getX() < 0) ? 0 : Math.min(ww - INIT_AREA_SIZE, centre.getX())), 
						(int) ((centre.getY() < 0) ? 0 : Math.min(wh - INIT_AREA_SIZE, centre.getY())));
			}
		}		
	}
	
	private Point2D randPoint(int min, int max) {
		Random random = new Random();
		Point2D p = new Point2D(random.nextInt(max - min + 1) + min, random.nextInt(max - min + 1) + min);
		return p;
	}

	public boolean inRiver(Point2D point) {
		for (Polygon river : rivers) {
			for(int i = 0; i < river.getCoords().length/2; i++) {
				if ((Common.isBetween(river.getxCoords()[i], river.getxCoords()[river.getxCoords().length - i - 1], point.getX())) || 
						(Common.isBetween(river.getyCoords()[i], river.getyCoords()[river.getyCoords().length - i - 1], point.getY()))) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void updateDrawable() {
		drawable[0] = drawable[0].subtract(AREA_SHIFT, AREA_SHIFT); // Top Left
		drawable[1] = drawable[1].add(AREA_SHIFT, AREA_SHIFT); // Bottom Right
	}
	
	public Point2D[] getDrawable() { return drawable; }
	
	public void addRiver(ArrayList<Point2D> river, double size, boolean isHorizontal) {
		Point2D[] realRiver = getRealRiver(river, size, isHorizontal, ww, wh);
		rivers.add(new Polygon(riverC, ESHAPE_TYPE.POLYGON, realRiver[0]));
		rivers.get(rivers.size() - 1).setCoords(realRiver);
		calcDrawable(new Point2D(ww/2, wh/2));
	}
	
	public static Point2D[] getRealRiver(ArrayList<Point2D> river, double size, boolean isHorizontal, double ww, double wh) {
		Point2D[] realRiver = new Point2D[river.size() * 2];
		if (isHorizontal) {
			for (int i = 0; i < river.size(); i++) {			
				Point2D calc = river.get(i).add(0, size);
				realRiver[i] = new Point2D(calc.getX(), Math.min(calc.getY(), wh));
				calc = river.get(i).subtract(0, size);
				realRiver[realRiver.length - i - 1] = new Point2D(calc.getX(), Math.max(calc.getY(), 0));
			} 
		} else {
			for (int i = 0; i < river.size(); i++) {
				Point2D calc = river.get(i).add(size, 0);
				realRiver[i] = new Point2D(Math.min(calc.getX(), ww), calc.getY());
				calc = river.get(i).subtract(size, 0);
				realRiver[realRiver.length - i - 1] = new Point2D(Math.max(calc.getX(), 0), calc.getY());
			}
		}
		return realRiver;
	}
	
	public void updateWH(double ww, double wh) {
		this.ww = ww;
		this.wh = wh;
		land = new Rectangle(landC, new Point2D(0, 0), ww, wh);
	}
	
	public void draw(IDrawVisitor v) {
		updateDrawable();
		land.draw(v, true);
		for (Polygon r : rivers) {
			r.draw(v, true);
		}
	}
	
	public static ArrayList<Polygon> generateRandomRivers(Color riverC, int count, int maxSegmentDist, double ww, double wh, int size) {
		ArrayList<Polygon> rivers = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			ArrayList<Point2D> river = new ArrayList<>();
			float rotation;
			Random random = new Random();
			boolean isHorizontal = random.nextInt() % 2 == 0;
			if (isHorizontal) {
				// Horizontal
				river.add(new Point2D(0, random.nextInt((int) wh + 1)));
				boolean atEdge = false;
				do {
					switch (random.nextInt(3)) {
					case 0:
						rotation = 0f;
						break;
					case 1:
						rotation = 45f;
						break;
					case 2:
						rotation = -45f;
						break;
					default:
						rotation = 0f;
					}
					int dist = random.nextInt(maxSegmentDist) + 1;
					double x = river.get(river.size() - 1).getX() + (dist * Math.cos(Math.toRadians(rotation)));
					double y = river.get(river.size() - 1).getY() + (dist * Math.sin(Math.toRadians(rotation)));
					if (y >= wh) {
						y = wh;
						x = river.get(river.size() - 1).getX() - ((river.get(river.size() - 1).getY() - y) * Math.tan(Math.toRadians(rotation)));
					} else if (y < 0) {
						y = 0;
						x = river.get(river.size() - 1).getX() - ((river.get(river.size() - 1).getY() - y) * Math.tan(Math.toRadians(rotation)));
					}
					if (x >= ww) {
						x = ww;
						if (rotation != 0)
							y = river.get(river.size() - 1).getY() - (river.get(river.size() - 1).getX() - x)/Math.tan(Math.toRadians(rotation));
						atEdge = true;
					}
					river.add(new Point2D((int) x, (int) y));
				} while (!atEdge);
			} else {
				// Vertical
				river.add(new Point2D(random.nextInt((int) ww + 1), 0));
				boolean atEdge = false;
				do {
					switch (random.nextInt(3)) {
					case 0:
						rotation = 90f;
						break;
					case 1:
						rotation = 45f;
						break;
					case 2:
						rotation = 135f;
						break;
					default:
						rotation = 90f;
					}
					int dist = random.nextInt(maxSegmentDist) + 1;
					double x = river.get(river.size() - 1).getX() + (dist * Math.cos(Math.toRadians(rotation)));
					double y = river.get(river.size() - 1).getY() + (dist * Math.sin(Math.toRadians(rotation)));
					if (x >= ww) {
						x = ww;
						y = river.get(river.size() - 1).getY() - (river.get(river.size() - 1).getX() - x)/Math.tan(Math.toRadians(rotation));
					} else if (x < 0) {
						x = 0;
						y = river.get(river.size() - 1).getY() - (river.get(river.size() - 1).getX() - x)/Math.tan(Math.toRadians(rotation));
					}
					if (y >= wh) {
						y = wh;
						if (rotation != 90)
							x = river.get(river.size() - 1).getX() - ((river.get(river.size() - 1).getY() - y) * Math.tan(Math.toRadians(rotation)));
						atEdge = true;
					}
					river.add(new Point2D((int) x, (int) y));
				} while (!atEdge);
			}
			Point2D[] realRiver = getRealRiver(river, size, isHorizontal, ww, wh);
			Polygon p = new Polygon(riverC, ESHAPE_TYPE.POLYGON, realRiver[0]);
			p.setCoords(realRiver);
			rivers.add(p);
		}
		
		return rivers;
	}

	/**
	 * @return the landC
	 */
	public Color getLandC() {
		return landC;
	}

	/**
	 * @param landC the landC to set
	 */
	public void setLandC(Color landC) {
		this.landC = landC;
	}

	/**
	 * @return the riverC
	 */
	public Color getRiverC() {
		return riverC;
	}

	/**
	 * @param riverC the riverC to set
	 */
	public void setRiverC(Color riverC) {
		this.riverC = riverC;
	}

	/**
	 * @return the ww
	 */
	public double getWw() {
		return ww;
	}

	/**
	 * @param ww the ww to set
	 */
	public void setWw(double ww) {
		this.ww = ww;
	}

	/**
	 * @return the wh
	 */
	public double getWh() {
		return wh;
	}

	/**
	 * @param wh the wh to set
	 */
	public void setWh(double wh) {
		this.wh = wh;
	}

	/**
	 * @return the land
	 */
	public Rectangle getLand() {
		return land;
	}

	/**
	 * @return the rivers
	 */
	public ArrayList<Polygon> getRivers() {
		return rivers;
	}
	
	/**
	 * @param the rivers to set
	 */
	public void setRivers(ArrayList<Polygon> rivers) {
		this.rivers = rivers;
		land = new Rectangle(landC, new Point2D(ww/2, wh/2), ww, wh);
		calcDrawable(new Point2D(ww/2, wh/2));
	}
}
