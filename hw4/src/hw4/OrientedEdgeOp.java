/*** @author Austin Klum*/
package hw4;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

import pixeljelly.ops.BandExtractOp;
import pixeljelly.ops.ConvolutionOp;
import pixeljelly.scanners.Location;
import pixeljelly.scanners.RasterScanner;
import pixeljelly.utilities.NonSeperableKernel;
import pixeljelly.utilities.SimpleColorModel;

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
		
		ConvolutionOp horizontalOp = new ConvolutionOp(new NonSeperableKernel(3, 1, new float[] { -1.0f, 0.0f, 1.0f }), true);
	    ConvolutionOp verticalOp =  new ConvolutionOp(new NonSeperableKernel(1, 3, new float[] { -1.0f, 0.0f, 1.0f }), true);
	    final BufferedImage gray = new BandExtractOp(SimpleColorModel.HSV, 2).filter(src, null);
        final BufferedImage vGradient = verticalOp.filter(gray, null);
        final BufferedImage hGradient = horizontalOp.filter(gray, null);
		
		WritableRaster srcRaster = src.getRaster();
		WritableRaster destRaster = dest.getRaster();
		
		RasterScanner rs = new RasterScanner(src, false);
		for(Location pt : rs)
		{
			int dx = hGradient.getRaster().getSample(pt.col, pt.row, 0);
			int dy = vGradient.getRaster().getSample(pt.col, pt.row, 0);
			double theta = getTheta(dx, dy);
			
			int rgb = src.getRGB(pt.col, pt.row);
			if (withinEpsilon(theta) && mogExceedsStrength(dx,dy))
			{
				rgb = Color.RED.getRGB();
			}
			dest.setRGB(pt.col, pt.row, rgb);
		}

		return dest;
	}

	private boolean mogExceedsStrength(int dx, int dy)
	{
		boolean mogExceedsStrength = false;
		
		double mog = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)) / 256;
		
		if (mog > strength)
		{
			mogExceedsStrength = true;
		}
		
		return mogExceedsStrength;
	}

	private double getTheta(int dx, int dy) 
	{
		double tau = 2 * Math.PI;
		double unnormalizedTheta = Math.atan2(dy,dx);
		
		double normalizedTheta = unnormalizedTheta / tau;
		if (unnormalizedTheta < 0)
		{
			normalizedTheta = (unnormalizedTheta + tau) / tau;
		}
		
		return normalizedTheta;
	}


	
	public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel destCM) 
	{
		return new BufferedImage(
				destCM,
				destCM.createCompatibleWritableRaster(src.getWidth(), src.getHeight()),
				destCM.isAlphaPremultiplied(), 
				null);
	}

	private boolean withinEpsilon(double theta)
	{
		boolean withinEpsilon = false;
		
		if (theta >= orientation - epsilon && orientation + epsilon >= theta)
		{
			withinEpsilon = true;
		}
		
		return withinEpsilon;
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
