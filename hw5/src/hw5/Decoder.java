package hw5;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

public interface Decoder 
{
	public String getMagicWord();
	public void decode(File input, File output);
	public default boolean canDecode(File output)
	{
		String magicWord = null;
		try
		{
			DataInputStream in = new DataInputStream(new FileInputStream(output));
			magicWord = in.readUTF();
			in.close();
		}
		catch (Exception e)
		{
			return false;
		}
		return magicWord != null && magicWord.equals(getMagicWord());
	}
}
