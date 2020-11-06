/*** @author Austin Klum*/
package hw3;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;

import pixeljelly.scanners.Location;
import pixeljelly.scanners.RasterScanner;
import pixeljelly.utilities.Mask;
import pixeljelly.utilities.ReflectivePadder;

public class FastMedianOp extends NullOp implements  BufferedImageOp, pixeljelly.ops.PluggableImageOp
{
	private int m;
	private int n;
	
	private int median;
	private int middle;
	private int cdf;
	private Mask mask;
	
	public FastMedianOp() 
	{
		this(9,9);
	}
	
	public FastMedianOp(int m, int n)
	{
		this.m = m;
		this.n = n;
		this.mask = createMask(m, n);
        this.median = 0;
        this.middle = (this.mask.getSize() + 1) / 2;
        this.cdf = 0;
	}

	private Mask createMask(int m, int n) {
		boolean[] maskArr = new boolean[m*n];
		for(boolean maskValue : maskArr)
		{
			maskValue = true;
		}
		Mask mask = new Mask(m,n, maskArr);
		return mask;
	}
	

	@Override
	public BufferedImage filter(BufferedImage src, BufferedImage dest) 
	{
		if (dest == null)
		{
			dest = createCompatibleDestImage(src, src.getColorModel());
		}
		
		int histSize = (int) Math.pow(2.0, src.getSampleModel().getSampleSize(0));
		int[] histogram = new int[histSize];
		
		for (int b = 0; b < src.getRaster().getNumBands(); b++) 
		{
			for (Location pt : new RasterScanner(src, false))
			{
				pt.band = b;
				histogram = manageHistogram(src, histogram, pt);
				dest.getRaster().setSample(pt.col, pt.row, pt.band, this.median);
			}
		}
		return dest;
	}

	private int[] manageHistogram(BufferedImage src, int[] histogram, Location pt)
	{
		if (pt.col == 0)
		{
			histogram = this.setHistogram(src, pt);
			cdfSumMedianMiddle(histogram);
		}
		else 
		{
			RasterScanner maskScan = new RasterScanner(this.mask.getBounds());
			for (Location otherPt : maskScan )
			{
				histogram = checkLeft(src, histogram, pt, otherPt);
				histogram = checkRight(src, histogram, pt, otherPt);
				findMedian(histogram);
			}
		}
		return histogram;
	}

	private void findMedian(int[] histogram) 
	{
        while (this.cdf < this.middle)
        {
            this.cdf += histogram[++this.median];
        }
        while (isMedianTooHigh(histogram)) 
        {
            this.cdf -= histogram[this.median--];
        }
        clampMedian(histogram);
	}

	private void clampMedian(int[] histogram) 
	{
		if (this.median < 0) 
        {
            this.median = 0;
            this.cdf = histogram[0];
        }
	}

	private boolean isMedianTooHigh(int[] histogram) {
		return this.cdf > this.middle && this.median >= 0 && this.cdf - histogram[this.median] >= this.middle;
	}

	private int[] checkRight(BufferedImage src, int[] histogram, Location pt, Location otherPt)
	{
		histogram = updateSide(1, src, histogram, pt, otherPt);
		return histogram;
	}

	private int[] checkLeft(BufferedImage src, int[] histogram, Location pt, Location otherPt)
	{
		histogram = updateSide(-1, src, histogram, pt, otherPt);
		return histogram;
	}
	
	private int[] updateSide(int neighbor, BufferedImage src, int[] histogram, Location pt, Location otherPt)
	{
		if (this.mask.isIncluded(otherPt.col, otherPt.row) && !this.mask.isIncluded(otherPt.col + neighbor, otherPt.row))
		{
			int col = pt.col + otherPt.col + neighbor;
			int row = pt.row + otherPt.row;
			if (neighbor > 0)
			{
				col -= neighbor;
			}
			
			int sample = ReflectivePadder.getInstance().getSample(src, col, row, pt.band);

			histogram[sample] += neighbor;
			if (sample <= this.median) 
			{
			    this.cdf += neighbor;
			}
		}
		return histogram;
	}

	
	
	private void cdfSumMedianMiddle(int[] histogram)
	{
		while (medianIsntMiddle(histogram))
		{
		    this.cdf += histogram[this.median];
		    this.median++;
		}
		this.median -= 1;
	}

	private int[] setHistogram(BufferedImage src, Location loc)
	{
		this.median = 0;
		this.cdf = 0;
		int histSize = (int)Math.pow(2.0, src.getSampleModel().getSampleSize(0));
		int[] histogram = new int[histSize];
		RasterScanner scanMask = new RasterScanner(this.mask.getBounds());
		for (final Location pt : scanMask) 
		{
			if (this.mask.isIncluded(pt.col, pt.row)) 
			{
				int sample = ReflectivePadder.getInstance().getSample(src, loc.col + pt.col, loc.row + pt.row, loc.band);
		        histogram[sample]++;
		    }
		}
		return histogram;
	}
	
	

	private boolean medianIsntMiddle(int[] histogram) 
	{
		return this.median < histogram.length && this.cdf < this.middle;
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

