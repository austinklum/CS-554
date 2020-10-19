package hw2;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

import pixeljelly.scanners.Location;
import pixeljelly.scanners.RasterScanner;
import pixeljelly.utilities.SimpleColorModel;

public class BandExtractOp extends NullOp implements  BufferedImageOp, pixeljelly.ops.PluggableImageOp
{
	private enum BandToExtract {RED, GREEN, BLUE, YLUMINANCE, INPHASE, QUADRATURE, HUE, SATURATION, VALUE};
		
	private BandToExtract bandToExtract;
	private SimpleColorModel simpleColorModel;
	
	public BandExtractOp() 
	{
		this('H');
	}
	
	public BandExtractOp(char band)
	{
		setBand(band);
	}
	
	public void setBand(char band)
	{
		switch(band)
		{
			case 'R':
				this.simpleColorModel = SimpleColorModel.RGB;
				this.bandToExtract = BandToExtract.RED;
				break;
			case 'G':
				this.simpleColorModel = SimpleColorModel.RGB;
				this.bandToExtract = BandToExtract.GREEN;
				break;
			case 'B':
				this.simpleColorModel = SimpleColorModel.RGB;
				this.bandToExtract = BandToExtract.BLUE;
				break;
			case 'Y':
				this.simpleColorModel = SimpleColorModel.YUV;
				this.bandToExtract = BandToExtract.YLUMINANCE;
				break;
			case 'I':
				this.simpleColorModel = SimpleColorModel.YUV;
				this.bandToExtract = BandToExtract.INPHASE;
				break;
			case 'Q':
				this.simpleColorModel = SimpleColorModel.YUV;
				this.bandToExtract = BandToExtract.QUADRATURE;
				break;
			case 'H':
				this.simpleColorModel = SimpleColorModel.HSV;
				this.bandToExtract = BandToExtract.HUE;
				break;
			case 'S':
				this.simpleColorModel = SimpleColorModel.HSV;
				this.bandToExtract = BandToExtract.SATURATION;
				break;
			case 'V':
				this.simpleColorModel = SimpleColorModel.HSV;
				this.bandToExtract = BandToExtract.VALUE;
				break;
			default:
				throw new IllegalArgumentException();
		}
	}
	
	public char getBand()
	{
		return this.bandToExtract.toString().charAt(0);
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
			int sample = srcRaster.getSample(pt.col,pt.row, pt.band);
			destRaster.setSample(pt.row, pt.col, pt.band, sample);	
		}

		return dest;
	}
	
	public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel destCM) 
	{
		  return new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
	}

}
