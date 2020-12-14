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
	public String getMagicWord() 
	{
		return "DCT";
	}
}