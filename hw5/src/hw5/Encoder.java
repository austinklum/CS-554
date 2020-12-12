package hw5;

import java.awt.image.BufferedImage;

import hw5.Compressor.Model;

public interface Encoder 
{
	public void encode(BufferedImage image, Model model, int[] N, String output);
}
