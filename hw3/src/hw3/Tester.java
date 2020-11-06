/*** @author Austin Klum*/
package hw3;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Tester {

	public static void main(String[] args) throws IOException
	{
		BufferedImage image = new BufferedImage(256, 144, BufferedImage.TYPE_INT_RGB);
		image = ImageIO.read(new File("Trump.jpg"));
		NullOp op = new FastMedianOp();
		BufferedImage newImage = op.filter(image, null);
		ImageIO.write(newImage, "jpg", new File("out.jpg"));
	}

}
