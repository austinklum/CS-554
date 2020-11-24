/*** @author Austin Klum*/
package hw4;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

import pixeljelly.ops.BandExtractOp;
import pixeljelly.ops.ConvolutionOp;
import pixeljelly.scanners.Location;
import pixeljelly.scanners.RasterScanner;
import pixeljelly.utilities.NonSeperableKernel;
import pixeljelly.utilities.SimpleColorModel;

public class DitherOp extends NullOp
{
	enum Type { STUCKI, JARVIS, FLOYD_STEINBURG, SIERRA, SIERRA_2_4A }
	
	private Type type;
	private Color[] palette;
	
	
	public DitherOp(Type type, int paletteSize) 
	{
		setType(type);
		setPalette(generatePalette(paletteSize));
	}
	
	public DitherOp(Type type, Color[] palette) 
	{
		setType(type);
		setPalette(palette);
	}
	
	public DitherOp()
	{
		this(Type.JARVIS, 16);
	}
	
	@Override
	public BufferedImage filter(BufferedImage src, BufferedImage dest) 
	{
		dest = getDestImage(src, dest);
		
		
		WritableRaster srcRaster = src.getRaster();
		WritableRaster destRaster = dest.getRaster();
		
		RasterScanner rs = new RasterScanner(src, false);
		for(Location pt : rs)
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

	private Color[] generatePalette(int paletteSize)
	{
		// TODO Generate optimal palette using the median cut algorithm 
		return null;
	}
	
	public Type getType()
	{
		return type;
	}

	private void setType(Type type)
	{
		this.type = type;
	}

	private Color[] getPalette()
	{
		return palette;
	}

	private void setPalette(Color[] palette) 
	{
		nonNullPalette(palette);
		
		this.palette = palette;
	}

	private void nonNullPalette(Color[] palette) {
		if (palette == null)
		{
			throw new IllegalArgumentException("Palette is null");		
		}
		
		for (Color color : palette)
		{
			if (color == null) 
			{
				throw new IllegalArgumentException("Palette value is null");
			}
		}
	}

}
