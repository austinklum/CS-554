package hw5;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.stream.MemoryCacheImageInputStream;

import pixeljelly.scanners.Location;
import pixeljelly.scanners.RasterScanner;
import pixeljelly.utilities.ColorUtilities;

public class DeltaDecoder implements Decoder
{
	@Override
	public BufferedImage decode(File input, File output) throws Exception 
	{
		MemoryCacheImageInputStream in = new MemoryCacheImageInputStream(new FileInputStream(input));
		if (!canDecode(in)) return null;
		
		BufferedImage image = readHeader(in);
		int[] N = readDeltas(in);
		double sample = 0;
		for (int band = 0; band < image.getRaster().getNumBands(); band++)
		{
			for (Location pt : new RasterScanner(image, false))
			{
				if (pt.col == 0)
				{
					if (in.getBitOffset() != 0)
					{
						in.skipBytes(1);
					}
					sample = in.read();
				}
				else
				{
					int bit = in.readBit();
					if (bit == 0)
					{
						sample -= N[band];
					}
					else
					{
						sample += N[band];
					}
				}
				image.getRaster().setSample(pt.col, pt.row, band, ColorUtilities.clamp(sample));
			}
		}
		in.close();
		
		return image;
	}
	
	
	public BufferedImage readHeader(MemoryCacheImageInputStream in) throws Exception
	{
		short width = in.readShort();
		short height = in.readShort();
		int imageType = in.readInt();
		return new BufferedImage(width, height, imageType);
	}
	
	private int[] readDeltas(MemoryCacheImageInputStream in) throws IOException
	{
		int[] N = new int[3];
		N[0] = in.readInt();
		N[1] = in.readInt();
		N[2] = in.readInt();
		return N;
	}
	
	public boolean canDecode(MemoryCacheImageInputStream in)
	{
		String magicWord = null;
		try
		{
			magicWord = in.readUTF();
		}
		catch (Exception e)
		{
			return false;
		}
		return magicWord != null && magicWord.equals(getMagicWord());
	}
	
	@Override
	public String getMagicWord() 
	{
		return "Delta";
	}
}