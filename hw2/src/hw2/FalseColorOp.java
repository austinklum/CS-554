package hw2;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.util.LinkedList;
import java.util.List;

import pixeljelly.scanners.Location;
import pixeljelly.scanners.RasterScanner;

public class FalseColorOp extends NullOp implements BufferedImageOp, pixeljelly.ops.PluggableImageOp
{
	private Color[] palette;
	
	public FalseColorOp() 
	{

	}
	
	public FalseColorOp(Color[] palette)
	{
		setPalette(palette);
	}
	
	@Override
	public BufferedImage filter(BufferedImage src, BufferedImage dest) 
	{
		if (dest == null)
		{
			dest = createCompatibleDestImage(src, src.getColorModel());
		}
		
		RasterScanner scan = new RasterScanner(src, false);
		for (Location pt : scan)
		{
			int rgb = src.getRGB(pt.col, pt.row);
			Color pixelColor = new Color(rgb);
        	
        	dest.setRGB(pt.col, pt.row, pixelColor.getRGB());
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

	public void setPalette(Color[] palette)
	{
		// TODO check for an invalid palette
		this.palette = palette;
	}
	
	public Color[] getPalette()
	{
		return this.palette;
	}
}
