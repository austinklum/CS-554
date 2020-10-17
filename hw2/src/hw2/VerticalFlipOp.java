package hw2;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.WritableRaster;

import pixeljelly.scanners.Location;
import pixeljelly.scanners.RasterScanner;

public class VerticalFlipOp extends NullOp implements  BufferedImageOp, pixeljelly.ops.PluggableImageOp
{
	public VerticalFlipOp() 
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
			int destRow = src.getHeight() - pt.row - 1;
			int sample = srcRaster.getSample(pt.col,pt.row, pt.band);
			destRaster.setSample(pt.col, destRow, pt.band, sample);	
		}

		return dest;
	}

	@Override
	public BufferedImageOp getDefault(BufferedImage src)
	{
		return new VerticalFlipOp();
	}

}
