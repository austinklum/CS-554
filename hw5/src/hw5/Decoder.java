package hw5;

import java.awt.image.BufferedImage;

public interface Decoder 
{
	public String getMagicWord();
	public void decode(BufferedImage image, String output);
	public void canDecode()
}
