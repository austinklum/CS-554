/*** @author Austin Klum*/
package hw4;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

import pixeljelly.ops.MagnitudeOfGradientOp;
import pixeljelly.scanners.Location;
import pixeljelly.scanners.RasterScanner;

public class OrientedEdgeOp extends NullOp implements  BufferedImageOp, pixeljelly.ops.PluggableImageOp
{
	private double strength;
	private double orientation;
	private double epsilon;
	
	public OrientedEdgeOp(double strength, double orientation, double epsilon) 
	{
		this.strength = strength;
		this.orientation = orientation;
		this.epsilon = epsilon;
	}
	
	public OrientedEdgeOp() 
	{
		this(.5, .75, .15);
	}
	
	@Override
	public BufferedImage filter(BufferedImage src, BufferedImage dest) 
	{
		dest = getDestImage(src, dest);
		
		MagnitudeOfGradientOp op = new MagnitudeOfGradientOp();
		dest = op.filter(src, dest);
//		WritableRaster srcRaster = src.getRaster();
//		WritableRaster destRaster = dest.getRaster();
//		
//		RasterScanner rs = new RasterScanner(src, true);
//		for(Location pt : rs)
//		{
//			int sample = srcRaster.getSample(pt.col,pt.row, pt.band);
//			destRaster.setSample(pt.row, pt.col, pt.band, sample);	
//		}

		return dest;
	}


	
	public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel destCM) 
	{
		return new BufferedImage(
				destCM,
				destCM.createCompatibleWritableRaster(src.getWidth(), src.getHeight()),
				destCM.isAlphaPremultiplied(), 
				null);
	}

	public double getStrength() 
	{
		return strength;
	}

	public void setStrength(double strength) 
	{
		this.strength = strength;
	}

	public double getOrientation() {
		return orientation;
	}

	public void setOrientation(double orientation) 
	{
		this.orientation = orientation;
	}

	public double getEpsilon()
	{
		return epsilon;
	}

	public void setEpsilon(double epsilon) 
	{
		this.epsilon = epsilon;
	}

}
