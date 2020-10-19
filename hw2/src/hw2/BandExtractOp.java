package hw2;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

import pixeljelly.scanners.Location;
import pixeljelly.scanners.RasterScanner;
import pixeljelly.utilities.ColorUtilities;
import pixeljelly.utilities.SimpleColorModel;

public class BandExtractOp extends NullOp implements  BufferedImageOp, pixeljelly.ops.PluggableImageOp
{
	private enum BandToExtract 
	{
		RED(0),
		GREEN(1),
		BLUE(2),
		YLUMINANCE(0),
		INPHASE(1),
		QUADRATURE(2),
		HUE(0),
		SATURATION(1),
		VALUE(2);
		
		private int BandNumber;
		
		private BandToExtract(int bandNumber)
		{
			this.BandNumber = bandNumber;
		}
		
		public int getBandNumber()
		{
			return this.BandNumber;
		}
		
	};
		
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
		
		WritableRaster destRaster = dest.getRaster();
		
		RasterScanner scan = new RasterScanner(src, false);
        for (Location pt : scan) 
        {
        	int rgb = src.getRGB(pt.col, pt.row);
        	float[] pixel = ColorUtilities.fromRGB(rgb, this.simpleColorModel);
        	
        	int bandNumberToExtract = this.bandToExtract.getBandNumber();
        	float sampleValue = pixel[bandNumberToExtract];

            int sample = ColorUtilities.scale(sampleValue, this.simpleColorModel, bandNumberToExtract);
            destRaster.setSample(pt.col, pt.row, 0, sample);
        }
		return dest;
	}
	
	public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel destCM) 
	{
		  return new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
	}

}
