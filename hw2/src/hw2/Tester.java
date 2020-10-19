package hw2;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Tester {

	public static void main(String[] args) throws IOException
	{
		BufferedImage image = new BufferedImage(3, 3, 1);
		image = ImageIO.read(new File("smol.png"));
		NullOp op = new LocalEqualizeOp(1,1,false);
		op.filter(image, null);
	}

}
