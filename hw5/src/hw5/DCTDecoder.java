package hw5;

import java.awt.image.BufferedImage;
import java.io.File;

public class DCTDecoder implements Decoder
{

	@Override
	public BufferedImage decode(File input, File output) throws Exception
	{
		return null;
	}

	@Override
	public String getMagicWord() {
		return "DCT";
	}

}
