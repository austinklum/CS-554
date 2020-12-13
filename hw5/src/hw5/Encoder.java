package hw5;

import java.awt.image.BufferedImage;
import java.io.DataOutput;
import hw5.Compressor.Model;

public interface Encoder 
{
	public String getMagicWord();
	public void encode(BufferedImage image, Model model, int[] N, String output) throws Exception;
	public default void writeHeader(BufferedImage image, DataOutput out) throws Exception
	{
		out.writeUTF(getMagicWord());
		out.writeShort(image.getWidth());
		out.writeShort(image.getHeight());
		out.writeInt(image.getType());
	}
}
