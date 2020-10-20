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
			
			double distanceBetween = getL2Distance(pixel);
        	
        	double highlightSaturation = pixel[1] * 1.1 * Math.pow(Math.E, -3*distanceBetween);
        	double newSaturation = Math.min(1, highlightSaturation);
        	
        	// System.out.println(newSaturation);
        	
        	pixel[1] = (float) newSaturation;
        	
        	dest.setRGB(pt.col, pt.row, ColorUtilities.HSVtoPackedRGB(pixel));
		}

		return dest;
	}

	private double getL2Distance(float[] pixel)
	{
		float[] targetPixel = ColorUtilities.RGBtoHSV(targetColor.getRGB()); 
		
		float h1 = pixel[0];
		float h2 = targetPixel[0];
		float s1 = pixel[1];
		float s2 = targetPixel[1];
		float b1 = pixel[2];
		float b2 = targetPixel[2];
		
		double hueDiffSquared = Math.pow((s1 * Math.cos(2*Math.PI*h1))-(s2 * Math.cos(2*Math.PI*h2)), 2);
		double satDiffSquared = Math.pow((s1 * Math.sin(2*Math.PI*h1))-(s2 * Math.sin(2*Math.PI*h2)), 2);
		double valDiffSquared = Math.pow((b1 - b2), 2);

		double hsvDiffSum = hueDiffSquared + satDiffSquared + valDiffSquared;
		
		double L2Distance = Math.sqrt(hsvDiffSum);
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
