/*** @author Austin Klum*/
package hw2;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.RasterFormatException;
import java.awt.image.WritableRaster;

import pixeljelly.ops.HistogramEqualizeOp;
import pixeljelly.ops.PluggableImageOp;
import pixeljelly.scanners.Location;
import pixeljelly.scanners.RasterScanner;
import pixeljelly.scanners.ZigZagScanner;

public class LocalEqualizeOp extends NullOp implements PluggableImageOp, BufferedImageOp
{
	private boolean brightnessBandOnly;
	private int width;
	private int height;
	
	public LocalEqualizeOp() 
	{
		this(5, 5, false);
	}
	
	public LocalEqualizeOp(int width, int height, boolean brightnessBandOnly)
	{
		this.width = width;
		this.height = height;
		this.brightnessBandOnly = brightnessBandOnly;
	}
	
	@Override
	public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel destCM) 
	{
		return new BufferedImage(
				destCM,
				destCM.createCompatibleWritableRaster(src.getWidth(), src.getHeight()),
				destCM.isAlphaPremultiplied(), 
				null);
	}

	@Override
	public BufferedImage filter(BufferedImage src, BufferedImage dest) 
	{
		if (dest == null)
		{
			dest = createCompatibleDestImage(src, src.getColorModel());
		}
		WritableRaster destRaster = dest.getRaster();
		WritableRaster srcRaster = src.getRaster();
		HistogramEqualizeOp histOp = new HistogramEqualizeOp(brightnessBandOnly ? 1 : 0);
		
		ZigZagScanner scan = new ZigZagScanner(src, width, height);
		//RasterScanner scan = new RasterScanner(src, true);
		for (Location pt : scan)
		{
			System.out.print(pt);
			BufferedImage subImage = getSubImage(src, pt);
			BufferedImage equalizedSubImage = histOp.filter(subImage, null);

			int sample = srcRaster.getSample(pt.col,pt.row, pt.band);
			destRaster.setSample(pt.col, pt.row, pt.band, sample);
			System.out.println( " => " + sample);
			if(pt.band % 4 == 3) System.out.println("");
		}
		return dest;
	}

	private BufferedImage getSubImage(BufferedImage src, Location pt)
	{

		int maxRows = src.getHeight();
		int maxCols = src.getWidth();
		int upperMostRow = (pt.col - height) < 0 ? 0 : pt.col - height;
		int leftMostCol = (pt.row - width) < 0 ? 0 : pt.row - width;
		int lowestMostRow = (pt.col + height) > maxRows ? maxRows : pt.col + height;
		int rightMostCol = (pt.row + width) > maxCols ? maxCols : pt.row + width;
		int w = rightMostCol - leftMostCol;
		int h = lowestMostRow - upperMostRow;
		
		BufferedImage subImage = src.getSubimage(leftMostCol, upperMostRow, w, h);

		return subImage;
	}
	
}
