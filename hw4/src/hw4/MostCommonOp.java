/*** @author Austin Klum*/
package hw4;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

import pixeljelly.ops.MagnitudeOfGradientOp;
import pixeljelly.scanners.Location;
import pixeljelly.scanners.RasterScanner;

public class MostCommonOp extends NullOp
{
	private int m;
	private int n;
	
	public MostCommonOp(int m, int n) 
	{
		setM(m);
		setN(n);
	}
	
	public MostCommonOp() 
	{
		this(9, 9);
	}
	
	@Override
	public BufferedImage filter(BufferedImage src, BufferedImage dest) 
	{
		dest = getDestImage(src, dest);
		
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

	private void setM(int m)
	{
		this.m = m;
	}

	public int getN()
	{
		return n;
	}

	private void setN(int n)
	{
		this.n = n;
	}
}
