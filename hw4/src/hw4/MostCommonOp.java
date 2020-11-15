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

public class MostCommonOp extends NullOp
{
	private int m;
	private int n;
	
	private ImagePadder padder;
	private Mask mask;
	
	public MostCommonOp(int m, int n) 
	{
		setM(m);
		setN(n);
		padder = ReflectivePadder.getInstance();
		mask = createMask(m, n);
	}
	
	public MostCommonOp() 
	{
		this(9, 9);
	}
	
	private Mask createMask(int m, int n)
	{
		boolean[] maskArr = new boolean[m*n];
		Arrays.fill(maskArr, true);
		Mask mask = new Mask(m, n, maskArr);
		return mask;
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
			HashMap<Integer, Integer> sampleToCount = new HashMap<>();
			for (Location otherPt : new RasterScanner(mask.getBounds()))
			{
				int sample = padder.getSample(src, pt.col + otherPt.col, pt.row + otherPt.row, pt.band);
				addToMap(sampleToCount, sample);
			}
			int mode = getMode(sampleToCount);
			destRaster.setSample(pt.col, pt.row, pt.band, mode);	
		}

		return dest;
	}

	private void addToMap(HashMap<Integer, Integer> sampleToCount, int sample)
	{
		if(!sampleToCount.containsKey(sample))
		{
			sampleToCount.put(sample, 0);
		}
		sampleToCount.put(sample, sampleToCount.get(sample) + 1);
	}
	
	private int getMode(HashMap<Integer, Integer> sampleToCount)
	{
		// Create a list from elements of HashMap 
        List<Entry<Integer, Integer>> list = new LinkedList<Map.Entry<Integer, Integer> >(sampleToCount.entrySet()); 
  
        // Sort the list 
        Collections.sort(list, new Comparator<Map.Entry<Integer, Integer> >() { 
            public int compare(Map.Entry<Integer, Integer> o1,  
                               Map.Entry<Integer, Integer> o2) 
            { 
                return (o1.getValue()).compareTo(o2.getValue()); 
            } 
        }); 
        int mode = list.get(0).getKey();
		return mode;
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
