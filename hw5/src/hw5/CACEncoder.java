package hw5;

import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import hw5.Compressor.Model;

public class CACEncoder implements Encoder
{
	@Override
	public void encode(BufferedImage image, Model model, int[] N, File output) throws Exception
	{
		DataOutputStream out = new DataOutputStream(new FileOutputStream(output));
		writeHeader(image, out);
		for (int band = 0; band < image.getSampleModel().getNumBands(); band++)
		{
			write(image, out, 0, 0, band, image.getWidth(), image.getHeight(), N);
		}
		out.close();
	}

	private void write(BufferedImage image, DataOutputStream out, int x, int y, int band, int w, int h, int[] N) throws Exception
	{
		if (w <= 0 || h <= 0) return;
		
		double avg = getAvg(image, x, y, band, w, h);
		double sigma = getSigma(image, x, y, band, w, h, avg);
		
		if (sigma < N[band] || (w == 1 && h == 1))
		{
			int sample = Math.max((int) Math.round(avg), 1);
			out.write(sample);
		}
		else
		{
			out.write(0);
			write(image, out, x, y, band, w / 2, h /2, N);
            write(image, out, x + w / 2, y, band, w - w / 2, h / 2, N);
            write(image, out, x, y + h / 2, band, w / 2, h - h / 2, N);
            write(image, out, x + w / 2, y + h / 2, band, w - w / 2, h - h / 2, N);
		}
	}
	
	private double getAvg(BufferedImage image, int x, int y, int band, int w, int h)
	{
		long sum = 0;
		for (int col = x; col < x + w; col++)
		{
			for (int row = y; row < y; row++)
			{
				sum += image.getRaster().getSample(col, row, band);
			}
		}
		double avg = sum / (double) (w*h);
		return avg;
	}
	
	private double getSigma(BufferedImage image, int x, int y, int band, int w, int h, double avg)
	{
		double sum = 0;
		for (int col = x; col < x + w; col++)
		{
			for (int row = y; row < y + h; row++)
			{
				double difference = image.getRaster().getSampleDouble(col, row, band) - avg;
				sum += difference * difference;
			}
		}
		double sigma = Math.sqrt(sum / (w*h));
		return sigma;
	}
	
	@Override
	public String getMagicWord() 
	{
		return "CAC";
	}
}
