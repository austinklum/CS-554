package hw5;

import java.awt.image.BufferedImage;
import java.io.DataOutput;
import java.io.File;

import hw5.Compressor.Model;

public class DCTEncoder implements Encoder {
	@Override
	public void encode(BufferedImage image, Model model, int[] N, File output) throws Exception 
	{
		
	}

	@Override
	public void writeHeader(BufferedImage image, DataOutput out) throws Exception 
	{
		out.writeUTF(getMagicWord());
		out.writeShort(image.getWidth());
		out.writeShort(image.getHeight());
	}

	@Override
	public String getMagicWord() 
	{
		return "DFT";
	}
}