/*** @author Austin Klum*/
package hw4;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import pixeljelly.scanners.Location;
import pixeljelly.scanners.RasterScanner;
import pixeljelly.utilities.ImagePadder;
import pixeljelly.utilities.Mask;
import pixeljelly.utilities.ReflectivePadder;

public class FishLensOp extends NullOp
{
	private double weight;
	private boolean isInverted;
	
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
		
		WritableRaster srcRaster = src.getRaster();
		WritableRaster destRaster = dest.getRaster();
		
		RasterScanner rs = new RasterScanner(src, true);
		for(Location pt : rs)
		{
			int sample = srcRaster.getSample(pt.col, pt.row, pt.band);
			destRaster.setSample(pt.col, pt.row, pt.band, sample);	
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
