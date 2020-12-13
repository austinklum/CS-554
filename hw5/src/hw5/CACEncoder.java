package hw5;

import java.awt.image.BufferedImage;
import java.io.DataOutput;

import hw5.Compressor.Model;

public class CACEncoder implements Encoder
{

	@Override
	public void writeHeader(BufferedImage image, DataOutput out) throws Exception
	{
		
	}

	@Override
	public void encode(BufferedImage image, Model model, int[] N, String output) throws Exception
	{

	}

	@Override
	public String getMagicWord() 
	{
		return "CAC";
	}

}
