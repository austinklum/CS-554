package hw5;

import java.awt.image.BufferedImage;
import java.io.DataOutput;
import hw5.Compressor.Model;

public interface Encoder 
{
	public void writeHeader(BufferedImage image, DataOutput out) throws Exception;
	public void encode(BufferedImage image, Model model, int[] N, String output) throws Exception;;
	public String getMagicWord();
}
