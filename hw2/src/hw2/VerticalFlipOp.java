package hw2;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.WritableRaster;

import pixeljelly.ops.NullOp;
import pixeljelly.scanners.Location;
import pixeljelly.scanners.RasterScanner;

public class VerticalFlipOp extends NullOp 
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
			int destCol = src.getWidth() - pt.col - 1;
			int sample = srcRaster.getSample(pt.col,pt.row, pt.band);
			destRaster.setSample(destCol, pt.row, pt.band, sample);	
		}

		return dest;
	}

	
	public BufferedImageOp getDefault(BufferedImage src)
	{
		return new VerticalFlipOp();
	}
	

}
