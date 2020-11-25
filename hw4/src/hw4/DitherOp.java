/*** @author Austin Klum*/
package hw4;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

import pixeljelly.features.Palette;
import pixeljelly.ops.FloydSteinbergColorDitheringOp;
import pixeljelly.ops.JarvisJudicNinkeColorDitheringOp;
import pixeljelly.ops.SierraColorDitheringOp;
import pixeljelly.ops.StuckiColorDitheringOp;
import pixeljelly.scanners.Location;
import pixeljelly.scanners.RasterScanner;

public class DitherOp extends NullOp
{
	enum Type { STUCKI, JARVIS, FLOYD_STEINBURG, SIERRA, SIERRA_2_4A }
	
	private Type type;
	private Palette palette;
	private int paletteSize;
	
	public DitherOp(Type type, int paletteSize) 
	{
		setType(type);
		this.paletteSize = paletteSize;
	}
	
	public DitherOp(Type type, Color[] paletteColors) 
	{
		setType(type);
		setPalette(paletteColors);
	}

	private void setPalette(Color[] paletteColors) {
		palette = new Palette();
		
		if (paletteColors == null)
		{
			throw new IllegalArgumentException("Palette is null");		
		}

		for(Color color : paletteColors)
		{
			if (color == null) 
			{
				throw new IllegalArgumentException("Palette value is null");
			}
			palette.add(color);
		}
	}
	
	public DitherOp()
	{
		this(Type.JARVIS, 16);
	}
	
	@Override
	public BufferedImage filter(BufferedImage src, BufferedImage dest) 
	{
		Palette palette = getPalette(src);
		
		pixeljelly.ops.NullOp ditherOp = getOpByType(palette);
		
		dest = ditherOp.filter(src, dest);
		
		return dest;
	}

	
	private pixeljelly.ops.NullOp getOpByType(Palette palette)
	{
		switch(type)
		{
			case FLOYD_STEINBURG:
				return new FloydSteinbergColorDitheringOp(palette);
			case STUCKI:
				return new StuckiColorDitheringOp(palette);
			case JARVIS:
				return new JarvisJudicNinkeColorDitheringOp(palette);
			case SIERRA:
				return new SierraColorDitheringOp(palette);
			case SIERRA_2_4A: 
				return new Sierra_2_4AColorDitheringOp(palette);
		}
		throw new IllegalArgumentException("Invalid type");
	}
	
	public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel destCM) 
	{
		return new BufferedImage(
				destCM,
				destCM.createCompatibleWritableRaster(src.getWidth(), src.getHeight()),
				destCM.isAlphaPremultiplied(), 
				null);
	}

	private Palette generatePalette(BufferedImage src)
	{
		Palette palette = Palette.getPalette(src, paletteSize);
		
		return palette;
	}
	
	public Type getType()
	{
		return type;
	}

	private void setType(Type type)
	{
		this.type = type;
	}

	private Palette getPalette(BufferedImage src)
	{
		if (palette != null)
		{
			return palette;
		}
		return generatePalette(src);
	}

	private void nonNullPalette(Color[] palette) {
	
	}

}
