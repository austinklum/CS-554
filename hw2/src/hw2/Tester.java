package hw2;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Tester {

	public static void main(String[] args) throws IOException
	{
		BufferedImage image = new BufferedImage(800, 800, BufferedImage.TYPE_BYTE_GRAY);
		image = ImageIO.read(new File("candyGray.jpg"));
		NullOp op = new FalseColorOp();
		BufferedImage newImage = op.filter(image, null);
		ImageIO.write(newImage, "jpg", new File("out.jpg"));
	}

}
