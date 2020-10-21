/*** @author Austin Klum*/
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
		
		RasterScanner rs = new RasterScanner(src, true);
		for(Location pt : rs)
		{
			int destCol = src.getWidth() - pt.col - 1;
			int sample = srcRaster.getSample(pt.col,pt.row, pt.band);
			destRaster.setSample(destCol, pt.row, pt.band, sample);	
		}

		return dest;
	}

}
