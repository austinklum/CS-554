package hw2;

import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

import pixeljelly.scanners.Location;
import pixeljelly.scanners.RasterScanner;

public class HorizontalFlipOp extends NullOp implements BufferedImageOp, pixeljelly.ops.PluggableImageOp
{
	public HorizontalFlipOp() 
	{
		
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
		
		for (Location pt : new RasterScanner(src, true))
		{
			int sample = srcRaster.getSample(pt.col,pt.row, pt.band);
			destRaster.setSample(pt.col, pt.row, pt.band, sample);
		}
		return dest;
	}

	@Override
	public BufferedImageOp getDefault(BufferedImage src)
	{
		return new HorizontalFlipOp();
	}
	

}
