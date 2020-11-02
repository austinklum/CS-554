/*** @author Austin Klum*/
package hw3;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

import pixeljelly.scanners.Location;
import pixeljelly.scanners.RasterScanner;

public class GaussianOp extends NullOp implements  BufferedImageOp, pixeljelly.ops.PluggableImageOp
{
	
	private double alpha;
	private double sigma;
	
	public GaussianOp() 
	{
		this(2, 3);
	}
	
	public GaussianOp(double alpha, double sigma) 
	{
		this.setAlpha(alpha);
		this.setSigma(sigma);
	}

	@Override
	public BufferedImage filter(BufferedImage src, BufferedImage dest) 
	{
		if (dest == null)
		{
			dest = createCompatibleDestImage(src, src.getColorModel());
		}
		
		WritableRaster srcRaster = src.getRaster();
		WritableRaster destRaster = dest.getRaster();
		
		RasterScanner rs = new RasterScanner(src, true);
		for(Location pt : rs)
		{
			int sample = srcRaster.getSample(pt.col,pt.row, pt.band);
			destRaster.setSample(pt.row, pt.col, pt.band, sample);	
		}

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

	public double getAlpha()
	{
		return alpha;
	}

	public void setAlpha(double alpha)
	{
		this.alpha = alpha;
	}

	public double getSigma() 
	{
		return sigma;
	}

	public void setSigma(double sigma) 
	{
		this.sigma = sigma;
	}

}
