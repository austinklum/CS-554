package hw2;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

import pixeljelly.scanners.Location;
import pixeljelly.scanners.RasterScanner;
import pixeljelly.utilities.ColorUtilities;

public class ColorHighlightOp extends NullOp implements BufferedImageOp, pixeljelly.ops.PluggableImageOp
{
	private Color targetColor;
	
	public ColorHighlightOp() 
	{
		this(new Color(220, 50, 50));
	}
	
	public ColorHighlightOp(Color targetColor)
	{
		setTargetColor(targetColor);
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
		
		RasterScanner scan = new RasterScanner(src, false);
		for (Location pt : scan)
		{
			
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
	
	public void setTargetColor(Color targetColor)
	{
		this.targetColor = targetColor;
	}
	
	public Color getTargetColor()
	{
		return this.targetColor;
	}
}
