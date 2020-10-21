package hw2;

import java.awt.Color;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;

import com.sun.javafx.iio.ImageStorage.ImageType;

import pixeljelly.scanners.Location;
import pixeljelly.scanners.RasterScanner;
import pixeljelly.utilities.ColorUtilities;

public class FalseColorOp extends NullOp implements BufferedImageOp, pixeljelly.ops.PluggableImageOp
{
	private Color[] palette;
	
	public FalseColorOp() 
	{
		this(createDefaultPalette());
	}
	
	public FalseColorOp(Color[] palette)
	{
		setPalette(palette);
	}
	
	@Override
	public BufferedImage filter(BufferedImage src, BufferedImage dest) 
	{
		if(src.getType() != BufferedImage.TYPE_BYTE_GRAY) 
		{
			throw new IllegalArgumentException();
		}
		if (dest == null)
		{
			dest = createCompatibleDestImage(src, src.getColorModel());
		}
		
		WritableRaster srcRaster = src.getRaster();
		RasterScanner scan = new RasterScanner(src, false);
		for (Location pt : scan)
		{
			int brightness = srcRaster.getSample(pt.col, pt.row, 0);
			dest.setRGB(pt.col, pt.row, palette[brightness].getRGB());	
		}

		return dest;
	}

	public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel destCM) 
	{
		ColorModel newCM = createColorModel();
		return new BufferedImage(
				newCM,
				newCM.createCompatibleWritableRaster(src.getWidth(), src.getHeight()),
				newCM.isAlphaPremultiplied(), 
				null);

 
	}

	private IndexColorModel createColorModel()
	{
		byte[] redComp, greenComp, blueComp;
		redComp = new byte[palette.length];
		greenComp = new byte[palette.length];
		blueComp = new byte[palette.length];
		
		for (int i = 0; i < palette.length; i++) 
		{
		    redComp[i] = (byte)palette[i].getRed();
		    greenComp[i] = (byte)palette[i].getGreen();
		    blueComp[i] = (byte)palette[i].getBlue();
		}
		 return new IndexColorModel(8, palette.length, redComp, greenComp, blueComp);
	}
	
	public void setPalette(Color[] palette)
	{
		if (palette.length < 2 ) 
		{ 
			throw new IllegalArgumentException();
		}
		
		for(int i = 0; i < palette.length; i++)
		{
			if(palette[i] == null)
			{
				throw new IllegalArgumentException();
			}
		}
		this.palette = palette;
	}
	
	public Color[] getPalette()
	{
		return this.palette;
	}
	
	private static Color[] createDefaultPalette()
	{
		int paletteCount = 0;
		float incrementX8 = .125f;
		float incrementX4 = .25f;
		
		Color[] palette = new Color[256];
		float[] pixel = new float[3];
		for (float h = 0; h < 1; h += incrementX8)
		{
			for (float s = 0; s < 1; s += incrementX4)
			{
				for (float v = .1f; v < 1; v += incrementX8)
				{
					pixel[0] = h;
					pixel[1] = s;
					pixel[2] = v;
					int rgb = ColorUtilities.HSVtoPackedRGB(pixel);
					palette[paletteCount++] = new Color(rgb);
					//Color c = new Color(rgb);
					//System.out.printf("%d: <%.4f,%.4f,%.4f> => <%d,%d,%d>\n",paletteCount,h,s,v,c.getRed(),c.getGreen(), c.getBlue());
				}
			}
		}
		return palette;
	}
}
