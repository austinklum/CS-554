package hw5;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

import javax.imageio.stream.MemoryCacheImageOutputStream;

import hw5.Compressor.Model;

public class DeltaEncoder implements Encoder
{
	@Override
	public void encode(BufferedImage image, Model model, int[] N, File output) throws Exception
	{
		MemoryCacheImageOutputStream out = new MemoryCacheImageOutputStream(new FileOutputStream(output));
		writeHeader(image, out);
		out.writeInt(N[0]);
		out.writeInt(N[1]);
		out.writeInt(N[2]);
		for (int band = 0; band < image.getRaster().getNumBands(); band++)
		{
			for (Location pt : new RasterScanner(image, false))
			{
				
			}
		}
		
		out.close();
	}

	@Override
	public String getMagicWord() 
	{
		return "Delta";
	}
}
