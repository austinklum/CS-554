package hw2;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

import pixeljelly.scanners.Location;
import pixeljelly.scanners.RasterScanner;
import pixeljelly.utilities.ColorUtilities;

public class ColorHighlightOp extends NullOp implements BufferedImageOp, pixeljelly.ops.PluggableImageOp
{
	private Color targetColor;
	
	public ColorHighlightOp() 
	{
		this(new Color(220, 50, 50));
	}
	
	public ColorHighlightOp(Color targetColor)
	{
		setTargetColor(targetColor);
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
			float[] pixel = ColorUtilities.RGBtoHSV(rgb);
			
			double distanceBetween = getL2Distance(pixelColor);
        	
        	double highlightSaturation = pixel[1] * 1.1 * Math.pow(Math.E, -3*distanceBetween);
        	double newSaturation = Math.min(1, highlightSaturation);
        	
        	// System.out.println(newSaturation);
        	
        	pixel[1] = (float) newSaturation;
        	
        	dest.setRGB(pt.col, pt.row, ColorUtilities.HSVtoPackedRGB(pixel));
		}

		return dest;
	}

	private double getL2Distance(Color pixelColor)
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
	
	public void setTargetColor(Color targetColor)
	{
		this.targetColor = targetColor;
	}
	
	public Color getTargetColor()
	{
		return this.targetColor;
	}
}
