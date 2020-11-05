/*** @author Austin Klum*/
package hw3;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

import pixeljelly.scanners.Location;
import pixeljelly.scanners.RasterScanner;

public class FastMedianOp extends NullOp implements  BufferedImageOp, pixeljelly.ops.PluggableImageOp
{
	private int m;
	private int n;
	
	public FastMedianOp() 
	{
		this(9,9);
	}
	
	public FastMedianOp(int m, int n)
	{
		this.m = m;
		this.n = n;
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

	public int getM()
	{
		return m;
	}
	
	public int getN()
	{
		return n;
	}
	
}
