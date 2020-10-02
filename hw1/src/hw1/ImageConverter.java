package hw1;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ImageConverter {
    
    public static BufferedImage toBufferedImage(DigitalImage src) {
    	BufferedImage image = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_RGB);
    	for (int x = 0; x < image.getWidth(); x++)
    	{
    		for (int y = 0; y < image.getHeight(); y++)
    		{
    			int[] pixel = src.getPixel(x, y);
    			int rgb = new Color(pixel[0], pixel[1], pixel[2]).getRGB();
    			image.setRGB(x, y, rgb);
    		}
    	}
        return image;
    }
    
    public static DigitalImage toDigitalImage(BufferedImage src) {
        DigitalImage image = new ();
        return null;
    }
}
