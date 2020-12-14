package hw5;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.stream.MemoryCacheImageOutputStream;

import hw5.Compressor.Model;
import pixeljelly.scanners.Location;
import pixeljelly.scanners.RasterScanner;

public class DeltaEncoder implements Encoder
{
	@Override
	public void encode(BufferedImage image, Model model, int[] N, File output) throws Exception
	{
		MemoryCacheImageOutputStream out = new MemoryCacheImageOutputStream(new FileOutputStream(output));
		writeHeader(image, out);
		writeDeltas(N, out);
		double predict = 0;
		for (int band = 0; band < image.getRaster().getNumBands(); band++)
		{
			for (Location pt : new RasterScanner(image, false))
			{
				int sample = image.getRaster().getSample(pt.col, pt.row, band);
				if (pt.col == 0)
				{
					out.write(sample);
					predict = sample;
				}
				else if (sample > predict)
				{
					out.writeBit(1);
					predict += N[band];
				}
				else
				{
					out.writeBit(0);
					predict -= N[band];
				}
			}
		}
		
		out.close();
	}

	private void writeDeltas(int[] N, MemoryCacheImageOutputStream out) throws IOException {
		out.writeInt(N[0]);
		out.writeInt(N[1]);
		out.writeInt(N[2]);
	}

	@Override
	public String getMagicWord() 
	{
		return "Delta";
	}
}
