/*** @author Austin Klum*/
package hw2;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.util.LinkedList;
import java.util.List;

import pixeljelly.scanners.Location;
import pixeljelly.scanners.RasterScanner;

public class PosterizeOp extends NullOp implements BufferedImageOp, pixeljelly.ops.PluggableImageOp
{
	
	private List<Color> possibleColors;
	
	public PosterizeOp() 
	{
		loadPossibleColors();
	}

	private void loadPossibleColors()
	{
		possibleColors = new LinkedList<>();
		possibleColors.add(Color.red);
		possibleColors.add(Color.green);
		possibleColors.add(Color.blue);
		possibleColors.add(Color.cyan);
		possibleColors.add(Color.magenta);
		possibleColors.add(Color.yellow);
		possibleColors.add(Color.black);
		possibleColors.add(Color.white);
	}
	
	@Override
	public BufferedImage filter(BufferedImage src, BufferedImage dest) 
	{
		if (dest == null)
		{
			dest = createCompatibleDestImage(src, src.getColorModel());
		}
		
		RasterScanner scan = new RasterScanner(src, false);
		for (Location pt : scan)
		{
			int rgb = src.getRGB(pt.col, pt.row);
			Color pixelColor = new Color(rgb);
			
			Color newPixelColor = getNewPixelColor(pixelColor);
        	
        	dest.setRGB(pt.col, pt.row, newPixelColor.getRGB());
		}

		return dest;
	}

	private Color getNewPixelColor(Color pixelColor)
	{
		double closestDistance = -1;
		Color closestColor = null;
		for(Color color : possibleColors)
		{
			double L2Distance = getL2Distance(pixelColor, color);
			if (L2Distance < closestDistance || closestDistance == -1)
			{
				closestDistance = L2Distance;
				closestColor = color;
			}
		}
		return closestColor;
	}
	
	private double getL2Distance(Color pixelColor, Color targetColor)
	{
		double redDiffSquared = Math.pow((targetColor.getRed() - pixelColor.getRed()), 2);
		double greenDiffSquared = Math.pow((targetColor.getGreen() - pixelColor.getGreen()), 2);
		double blueDiffSquared = Math.pow((targetColor.getBlue() - pixelColor.getBlue()), 2);
		
		double rgbDiffSum = redDiffSquared + greenDiffSquared + blueDiffSquared;
		
		double L2Distance = Math.sqrt(rgbDiffSum);
		double L2DistanceNorm =  L2Distance / Math.sqrt(3);
		// System.out.println("L2Distance is " + L2Distance + ". Might be " + L2DistanceNorm);
		
		return L2DistanceNorm;
	}
	
	public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel destCM) 
	{
		return new BufferedImage(
				destCM,
				destCM.createCompatibleWritableRaster(src.getWidth(), src.getHeight()),
				destCM.isAlphaPremultiplied(), 
				null);
	}

}
