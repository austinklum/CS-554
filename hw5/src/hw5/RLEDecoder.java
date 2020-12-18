package hw5;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

public class RLEDecoder implements Decoder
{
	@Override
	public BufferedImage decode(File input, File output) throws Exception 
	{
		DataInputStream in = new DataInputStream(new FileInputStream(input));
		if (!canDecode(in)) return null;
		
		BufferedImage image = readHeader(in);
		for (int band = 0; band < image.getSampleModel().getNumBands(); band++)
		{
			for (int bit = 0; bit < image.getSampleModel().getSampleSize(band); bit++)
			{
				for (int y = 0; y < image.getHeight(); y++)
				{
					boolean isWhite = true;
					int pixelsdecoded = 0;
					while (pixelsdecoded < image.getWidth())
					{
						int length = readRun(in);
						setRun(image, length, pixelsdecoded, y, band, bit, isWhite);
						isWhite = !isWhite;
						pixelsdecoded += length;
					}
				}
			}
		}
		return image;
	}
	private void setRun(BufferedImage image, int length, int column, int row, int band, int bit, boolean isWhite)
	{
		if (!isWhite) return;
		
		while (column < image.getWidth() && length > 0)
		{
			image.getRaster().setSample(column, row, band, setBit(image.getRaster().getSample(column, row, band), bit));
			column++;
			length--;
		}
	}
	
	private int setBit(int sample, int bit)
	{
		return sample | 1 << bit;
	}

	private int readRun(DataInputStream in) throws IOException
	{
		int result = 0;
		int read = in.read();
		while (read == 255)
		{
			result += 255;
			read = in.read();
		}
		result += read;
		return result;
	}
	@Override
	public String getMagicWord() 
	{
		return "RLE";
	}
}
