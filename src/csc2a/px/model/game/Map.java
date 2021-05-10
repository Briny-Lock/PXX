package csc2a.px.model.game;

import java.util.ArrayList;
import java.util.Random;

import csc2a.px.model.Common;
import csc2a.px.model.shape.ESHAPE_TYPE;
import csc2a.px.model.shape.Line;
import csc2a.px.model.shape.Polygon;
import csc2a.px.model.shape.Rectangle;
import csc2a.px.model.visitor.IDrawVisitor;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class Map {
	/**
	 * 
	 */
	private static final int INIT_AREA_SIZE = 90;
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
	
	public static ArrayList<Polygon> generateRandomRivers(Color riverC, int count, int maxSegmentDist, double ww, double wh, double size) {
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
	
	public Point2D[] getBridge(Point2D p1, Point2D p2) {
		Point2D[] bridge = null;
		Point2D mid = Line.calcMidpoint(p1, p2);
		double m1 = (mid.getY() - p1.getY())/(mid.getX() - p1.getX()); 
		double c1 = (-m1 * p1.getX()) + p1.getY(); // y = m(x - x1) + y1 OR y = mx + (-mx1 + y1) == y = mx + c
		double m2 = (p2.getY() - mid.getY())/(p2.getX() - mid.getX()); 
		double c2 = (-m2 * mid.getX()) + mid.getY(); // y = m(x - x1) + y1 OR y = mx + (-mx1 + y1) == y = mx + c
		for (Polygon polygon : rivers) {
			for (int i = 1; i < polygon.getCoords().length/2; i++) {
				Point2D p3 = polygon.getCoords()[i - 1], 
						p4 = polygon.getCoords()[i];
				Point2D p5 = polygon.getCoords()[polygon.getCoords().length - i - 1],
						p6 = polygon.getCoords()[polygon.getCoords().length - i - 2];
				double pm1 = (p4.getY() - p3.getY())/(p4.getX() - p3.getX()); 
				double pc1 = (-pm1 * p3.getX()) + p3.getY(); // y = mx + (-mx1 + y1)
				double pm2 = (p6.getY() - p5.getY())/(p6.getX() - p5.getX()); 
				double pc2 = (-pm2 * p5.getX()) + p5.getY(); // y = mx + (-mx1 + y1)
				// Supposed x intersections
				double x1 = (pc1 - c1)/(m1 - pm1); // tmx + tc = pmx + pc OR x = (pc - tc)/(tm - pm)
				double y1 = (m1 * x1) + c1;
				double x2 = (pc2 - c2)/(m2 - pm2); // tmx + tc = pmx + pc OR x = (pc - tc)/(tm - pm)
				double y2 = (m2 * x2) + c2;
				
				//Test if lines cross
				boolean hasBridge = false;
				Point2D start = null,
						newMid = null,
						end = null;
				if ((y1 == (pm1 * x1) + pc1) && (p1.getY() == mid.getY() || Common.isBetween(p1.getY(), mid.getY(), y1))) {
					double newX = (pc1 - c1)/(m1 - pm1);
					double newY = (m1 * newX) + c1;
					start = new Point2D(x1, y1);
					newMid = new Point2D(newX, newY);
					hasBridge = true;
				}
				if ((y2 == (pm2 * x2) + pc2) && (mid.getY() == p2.getY() || Common.isBetween(mid.getY(), p2.getY(), y1))) {
					double newX = (pc2 - c2)/(m2 - pm2);
					double newY = (m2 * newX) + c2;
					newMid = new Point2D(x2, y2);
					end = new Point2D(newX, newY);
					hasBridge = true;
				}
				if (hasBridge) {
					if (start != null && end != null) {
						bridge = new Point2D[3];
						bridge[0] = start;
						bridge[1] = mid;
						bridge[2] = end;
					} else if (start != null) {
						bridge = new Point2D[2];
						bridge[0] = start;
						bridge[1] = newMid;
					} else {
						bridge = new Point2D[2];
						bridge[0] = newMid;
						bridge[1] = end;
					}
					return bridge;
				}
			}
		}
		return bridge;
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
