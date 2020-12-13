package hw5;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class RLEDecoder implements Decoder
{
	@Override
	public void decode(File input, File output) throws Exception 
	{
		DataInputStream in = new DataInputStream(new FileInputStream(output));
		if (!canDecode(in)) return;
		
		BufferedImage image = readHeader(in);
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

	}
	@Override
	public String getMagicWord() 
	{
		return "RLE";
	}
}
