package hw1;

import java.util.IllegalFormatException;
import hw1.DigitalImageIO.ImageType;

public class ImageFactory 
{
	public DigitalImage GetImage(ImageType type, int width, int height)
	{
		switch (type)
		{
			case INDEXED:
				return new IndexedDigitalImage(width, height, null);
			case PACKED: 
				return new PackedPixelImage();
			case LINEAR_ARRAY:
				return new LinearArrayDigitalImage();
			case MULTIDIM_ARRAY :
				return new ArrayDigitalImage();
			default:
				throw new IllegalFormatException();
			
		}
	}
}
