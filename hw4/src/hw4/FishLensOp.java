/*** @author Austin Klum*/
package hw4;

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

import pixeljelly.scanners.Location;
import pixeljelly.scanners.RasterScanner;
import pixeljelly.utilities.BilinearInterpolant;
import pixeljelly.utilities.ExtendedBorderPadder;
import pixeljelly.utilities.ImagePadder;

public class FishLensOp extends NullOp
{
	private double weight;
	private boolean isInverted;
	
	private double focalLength;
	private double scale;
	
	public FishLensOp(double weight, boolean isInverted) 
	{
		setWeight(weight);
		setIsInverted(isInverted);
	}
	
	public FishLensOp() 
	{
		this(5, false);
	}
	
	@Override
	public BufferedImage filter(BufferedImage src, BufferedImage dest) 
	{
		dest = getDestImage(src, dest);
		
		BilinearInterpolant interpolant = new BilinearInterpolant();
		ImagePadder padder = ExtendedBorderPadder.getInstance();		
		
		WritableRaster srcRaster = src.getRaster();
		WritableRaster destRaster = dest.getRaster();
		
		RasterScanner rs = new RasterScanner(dest, false);
		for(Location pt : rs)
		{
			Point2D destPoint = getDestPoint(src, pt);
			Point2D srcPoint = getSrcPoint(src, destPoint);
			
			for (int b = 0; b < srcRaster.getNumBands(); b++)
			{
				int sample = interpolant.interpolate(src, padder, srcPoint, b);
				destRaster.setSample(pt.col, pt.row, b, sample);	
			}
		}

		return dest;
	}

	private Point2D getDestPoint(BufferedImage src, Location pt) 
	{
		Rectangle rasterBounds = src.getRaster().getBounds();
		double destPtX = pt.col + rasterBounds.x;
		double destPtY = pt.row + rasterBounds.y;
		Point2D destPt = new Point2D.Double(destPtX, destPtY);
		return destPt;
	}
	
	private Point2D getSrcPoint(BufferedImage src, Point2D destPoint)
	{
		double theta = getTheta(src, destPoint);
		double radial = getRadial(src, destPoint);
		
		double srcPointX = getSrcPointX(src, radial, theta);
		double srcPointY = getSrcPointY(src, radial, theta);
		
		Point2D srcPoint = new Point2D.Double(srcPointX, srcPointY);
		
		return srcPoint;
	}
	
	private double getFocalLength(BufferedImage src)
	{
		return Math.max(src.getWidth(), src.getHeight()) / 2;
	}
	
	private double getScale(double focalLength)
	{
		return focalLength / Math.log(weight * focalLength + 1);
	}
	
	private double getCenterX(BufferedImage src)
	{
		return src.getWidth() / 2;
	}
	
	private double getCenterY(BufferedImage src)
	{
		return src.getHeight() / 2;
	}
	
	private double getTheta(BufferedImage src, Point2D pt)
	{
		double xDistance = getXDistance(src, pt);
		double yDistance = getYDistance(src, pt);
		
		double theta = Math.atan2(yDistance, xDistance);
		
		return theta;
	}

	private double getYDistance(BufferedImage src, Point2D pt)
	{
		double centerY = getCenterY(src);
		double yDistance = pt.getY() - centerY;
		return yDistance;
	}

	private double getXDistance(BufferedImage src, Point2D pt) 
	{
		double centerX = getCenterX(src);
		double xDistance = pt.getX() - centerX;
		return xDistance;
	}
	
	private double getRadial(BufferedImage src, Point2D pt)
	{
		double focalLength = getFocalLength(src);
		double scale = getScale(focalLength);
		
		double radial = getR(src, pt);
		
		if (radial < focalLength)
		{
			radial = getRadialPrime(radial, scale);
		}
		
		return radial;
	}

	private double getR(BufferedImage src, Point2D pt) 
	{
		double xDistance = getXDistance(src, pt);
		double yDistance = getYDistance(src, pt);
		
		double mag = xDistance * xDistance + yDistance * yDistance;
		
		double radial = Math.sqrt(mag);
		return radial;
	}
	
	private double getRadialPrime(double radial, double scale)
	{
		if (getIsInverted())
		{
			return scale * Math.log(getWeight() * radial + 1);
		}
		return (Math.exp(radial / scale) - 1) / getWeight();
	}
	
	private double getSrcPointX(BufferedImage src, double radial, double theta)
	{
		double centerX = getCenterX(src);
	
		double srcPointX = radial * Math.cos(theta) + .5 + centerX;
		return srcPointX;
	}
	
	private double getSrcPointY(BufferedImage src, double radial, double theta)
	{
		double centerY = getCenterY(src);
	
		double srcPointY = radial * Math.sin(theta) + .5 + centerY;
		return srcPointY;
	}
	
	public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel destCM) 
	{
		return new BufferedImage(
				destCM,
				destCM.createCompatibleWritableRaster(src.getWidth(), src.getHeight()),
				destCM.isAlphaPremultiplied(), 
				null);
	}

	public double getWeight() 
	{
		return weight;
	}

	public void setWeight(double weight)
	{
		this.weight = weight;
	}

	public boolean getIsInverted() 
	{
		return isInverted;
	}

	public void setIsInverted(boolean isInverted)
	{
		this.isInverted = isInverted;
	}

}
