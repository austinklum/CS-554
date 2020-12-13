package hw5;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

public class CACDecoder implements Decoder
{

	@Override
	public void decode(File input, File output) throws Exception
	{
		DataInputStream in = new DataInputStream(new FileInputStream(input));
		if (!canDecode(in)) return;
		
		BufferedImage image = readHeader(in);
		for (int band = 0; band < image.getSampleModel().getNumBands(); band++)
		{
			read(image, in, 0, 0, band, image.getWidth(), image.getHeight());
		}
		in.close();
	}

	private void read(BufferedImage image, DataInputStream in, int x, int y, int band, int w, int h) throws Exception
	{
		if (w <= 0 || h <= 0) return;
		
		int sample = in.read();
		if(sample != 0)
		{
	        for (int col = x; col < x + w; col++) {
	            for (int row = y; row < y + h; row++) {
	                image.getRaster().setSample(col, row, band, sample);
	            }
	        }
		}
		else
		{
			read(image, in, x, y, band, w / 2, h / 2);
            read(image, in, x + w / 2, y, band, w - w / 2, h / 2);
            read(image, in, x, y + h / 2, band, w / 2, h - h / 2);
            read(image, in, x + w / 2, y + h / 2, band, w - w / 2, h - h / 2);
		}
	}
	
	@Override
	public String getMagicWord() 
	{
		return "CAC";
	}

}
