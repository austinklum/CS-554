package hw5;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class DeltaDecoder implements Decoder
{
	@Override
	public BufferedImage decode(File input, File output) throws Exception 
	{
		
	}
	
	@Override
	public String getMagicWord() 
	{
		return "Delta";
	}
}