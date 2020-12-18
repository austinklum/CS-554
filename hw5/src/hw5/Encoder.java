package hw5;

import java.awt.image.BufferedImage;
import java.io.DataOutput;
import java.io.File;

import hw5.Compressor.Model;

public interface Encoder 
{
	public String getMagicWord();
	public void encode(BufferedImage image, Model model, int[] N, File output) throws Exception;
	public default void writeHeader(BufferedImage image, DataOutput out) throws Exception
	{
		out.writeUTF(getMagicWord());
		out.writeShort(image.getWidth());
		out.writeShort(image.getHeight());
		out.writeInt(image.getType());
	}
}
