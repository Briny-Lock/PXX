package csc2a.px.model;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.paint.Color;

public class Common {
	public static double mapColor(double value, double start, double stop, double targetStart, double targetStop) {
        return targetStart + (targetStop - targetStart) * ((value - start) / (stop - start));
	}
	
	public static ColorAdjust getColorAdjust(Color c) {
		ColorAdjust effect = new ColorAdjust();
		
		double hue = mapColor((c.getHue() + 180) % 360, 0, 360, -1, 1);
		effect.setHue(hue);
	
		double saturation = c.getSaturation();
		effect.setSaturation(saturation);
	
		double brightness = mapColor( c.getBrightness(), 0, 1, -1, 0);
		effect.setBrightness(brightness);
		
		return effect;
	}
	
	public static boolean isBetween(double a, double b, double value) {
		// ensures a is min and b is max
		if (a > b) {
			double c = a;
			a = b;
			b = c;
		}		
		return (value > a && value < b);
	}
}
