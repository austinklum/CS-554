package hw5;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public interface Decoder 
{
	public String getMagicWord();
	public void decode(File input, File output) throws Exception;
	public default boolean canDecode(DataInputStream in)
	{
		String magicWord = null;
		try
		{
			magicWord = in.readUTF();
		}
		catch (Exception e)
		{
			return false;
		}
		return magicWord != null && magicWord.equals(getMagicWord());
	}
	
	public default BufferedImage readHeader(DataInputStream in) throws Exception
	{
		short width = in.readShort();
		short height = in.readShort();
		int imageType = in.readInt();
		return new BufferedImage(width, height, imageType);
	}
}
