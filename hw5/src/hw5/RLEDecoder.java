package hw5;

import java.io.File;

public class RLEDecoder implements Decoder
{
	@Override
	public void decode(File input, File output) 
	{
		if (!canDecode(output))
		{
			return;
		}

	}
	@Override
	public String getMagicWord() 
	{
		return "RLE";
	}
}
