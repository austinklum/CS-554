/*** @author Austin Klum*/


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import pixeljelly.ops.FastMedianOp;
import pixeljelly.ops.GeometricTransformOp;
import pixeljelly.utilities.BilinearInterpolant;
import pixeljelly.utilities.FisheyeMapper;


public class Tester {

	public static void main(String[] args) throws IOException
	{
		BufferedImage image = new BufferedImage(256, 144, BufferedImage.TYPE_INT_RGB);
		image = ImageIO.read(new File("Trump.jpg"));
		GeometricTransformOp op = new GeometricTransformOp(new FisheyeMapper(5, false), new BilinearInterpolant());
		BufferedImage newImage = op.filter(image, null);
		ImageIO.write(newImage, "jpg", new File("out.jpg"));
	}

}
