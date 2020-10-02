package hw1;

import hw1.DigitalImageIO.ImageType;

public class ImageFactory 
{
	public DigitalImage GetImage(ImageType type, int width, int height) throws IllegalFileFormatException
	{
		switch (type)
		{
			case INDEXED:
				return new IndexedDigitalImage(width, height);
			case PACKED: 
				return new PackedPixelImage(width, height, 3);
			case LINEAR_ARRAY:
				return new LinearArrayDigitalImage(width, height, 3);
			case MULTIDIM_ARRAY :
				return new ArrayDigitalImage(width, height, 3);
			default:
				throw new IllegalFileFormatException("Type of " + type + " was not found");
		}
	}
}
