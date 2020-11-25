/*** @author Austin Klum*/


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import pixeljelly.features.Palette;
import pixeljelly.ops.FastMedianOp;
import pixeljelly.ops.GeometricTransformOp;
import pixeljelly.ops.NullOp;
import pixeljelly.ops.OrientationOfGradientOp;
import pixeljelly.utilities.BilinearInterpolant;
import pixeljelly.utilities.FisheyeMapper;


public class Tester {

	public static void main(String[] args) throws IOException
	{
		BufferedImage image = ImageIO.read(new File("Trump.jpg"));
		Palette p = Palette.getPalette(image, 8);
		NullOp op = new Sierra_2_4AColorDitheringOp(p);
		BufferedImage newImage = op.filter(image, null);
		ImageIO.write(newImage, "jpg", new File("out.jpg"));
		System.out.println("Wrote the out!");
	}

}
