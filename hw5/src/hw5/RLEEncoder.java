package hw5;

import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import hw5.Compressor.Model;

public class RLEEncoder implements Encoder
{

	@Override
	public void encode(BufferedImage image, Model model, int[] N, File output) throws Exception
	{
		DataOutputStream out = new DataOutputStream(new FileOutputStream(output));
		writeHeader(image, out);
		
		for (int band = 0; band < image.getSampleModel().getNumBands(); band++)
		{
			for (int bit = 0; bit < image.getSampleModel().getSampleSize(band); bit++)
			{
				for (int y = 0; y < image.getHeight(); y++)
				{
					boolean isWhite = true;
					int pixelsEncoded = 0;
					while (pixelsEncoded < image.getWidth())
					{
						int length = getRun(image, pixelsEncoded, y, band, bit, isWhite);
						isWhite = !isWhite;
						writeRun(length, out);
						pixelsEncoded += length;
					}
				}
			}
		}
		out.close();
	}
	
	private int getRun(BufferedImage image, int column, int row, int band, int bit, boolean isWhite)
	{
		int result = 0;
		while (column < image.getWidth() && getBit(image.getRaster().getSample(column, row, band), bit) == isWhite)
		{
			column++;
			result++;
		}
		return result;
	}
	
	private boolean getBit(int sample, int bit)
	{
		return (sample & (0x1 << bit)) != 0;
	}

	private void writeRun(int length, DataOutputStream out) throws IOException
	{
		while (length >= 255)
		{
			out.write(255);
			length -= 255;
		}
		out.write(length);
	}
	@Override
	public String getMagicWord() 
	{
		return "RLE";
	}

}
