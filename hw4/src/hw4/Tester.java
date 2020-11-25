/*** @author Austin Klum*/
package hw4;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import hw4.DitherOp.Type;


public class Tester {

	public static void main(String[] args) throws IOException
	{
		BufferedImage image = ImageIO.read(new File("Trump.jpg"));
		NullOp op = new DitherOp(Type.SIERRA_2_4A, 8);
		BufferedImage newImage = op.filter(image, null);
		ImageIO.write(newImage, "jpg", new File("out.jpg"));
	}

}
