package hw2;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

import pixeljelly.scanners.Location;
import pixeljelly.scanners.RasterScanner;
import pixeljelly.utilities.ColorUtilities;

public class ShiftOp extends NullOp implements BufferedImageOp, pixeljelly.ops.PluggableImageOp
{
	private double hueTarget; 
	private double satScale;
	private double shiftStrength;
	
	public ShiftOp() 
	{
		this(0, 1.5, 1);
	}
	
	public ShiftOp(double hueTarget, double satScale, double shiftStrength)
	{
		setHueTarget(hueTarget);
		setSatScale(satScale);
		setShiftStrength(shiftStrength);
	}

	@Override
	public BufferedImage filter(BufferedImage src, BufferedImage dest) 
	{
		if (dest == null)
		{
			dest = createCompatibleDestImage(src, src.getColorModel());
		}
		

		WritableRaster destRaster = dest.getRaster();
		
		RasterScanner scan = new RasterScanner(src, false);
		for(Location pt : scan)
		{
			//System.out.print(pt);
			int rgb = src.getRGB(pt.col, pt.row);
        	float[] pixel = ColorUtilities.RGBtoHSV(rgb);
        	
        	pixel[0] = hShift(pixel[0]);
        	pixel[1] = (float) (pixel[1] * satScale);

        	int shiftedRgb = ColorUtilities.HSVtoPackedRGB(pixel);
        	dest.setRGB(pt.col, pt.row, shiftedRgb);

		}

		return dest;
	}

	private float hShift(float hue)
	{
		// denormalize
		float hueDegrees = hue * 360;
		double hueTargetDegrees = this.hueTarget * 360; 
		
		double difference = hueDegrees - hueTargetDegrees;
		// Figure out angle and distance in degrees
		double angle = Math.abs(difference) % 360;
		double dHDegrees = angle > 180 ? 360 - angle : angle;
		
		// normalize and shift
		double dH = dHDegrees / 360;
		double shift = Math.pow(dH, shiftStrength);
		
		int modifier = angle > 180 ? 1 : -1;
		double hShift = (hue + (shift * modifier));
		
		if(hShift < 0) hShift = 0;
		if(hShift > 1) hShift = 1;
		
		//System.out.println(" :: " + hueTarget  + "/" + hue + " => " + hShift);
		
		return (float) hShift;
	}
	
	public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel destCM) 
	{
		return new BufferedImage(
				destCM,
				destCM.createCompatibleWritableRaster(src.getWidth(), src.getHeight()),
				destCM.isAlphaPremultiplied(), 
				null);
	}
	
	public void setHueTarget(double hueTarget)
	{
		if (hueTarget < 0 || hueTarget > 1)
		{
			throw new IllegalArgumentException();
		}
		this.hueTarget = hueTarget;
	}
	
	public double getHueTarget()
	{
		return this.hueTarget;
	}
	
	public void setSatScale(double satScale)
	{
		if (satScale < 0 || satScale > 5)
		{
			throw new IllegalArgumentException();
		}
		this.satScale = satScale;
	}

	public double getSatSacle()
	{
		return this.satScale;
	}
	
	public void setShiftStrength(double shiftStrength)
	{
		this.shiftStrength = shiftStrength;
	}
	
	public double getShiftStrength()
	{
		return this.shiftStrength;
	}
	
}
