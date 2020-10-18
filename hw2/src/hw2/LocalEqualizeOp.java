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
		
		WritableRaster srcRaster = src.getRaster();
		WritableRaster destRaster = dest.getRaster();
		HistogramEqualizeOp histOp = new HistogramEqualizeOp(brightnessBandOnly ? 1 : 0);
		for (Location pt : new RasterScanner(src, true))
		{
			BufferedImage subImageOfSizeWxH = getSubImage(src, pt);
			//BufferedImage equalizedSubImage ;
			//histOp.filter(subImageOfSizeWxH, equalizedSubImage);
			int sample = srcRaster.getSample(pt.col,pt.row, pt.band);
			destRaster.setSample(pt.col, pt.row, pt.band, sample);
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
