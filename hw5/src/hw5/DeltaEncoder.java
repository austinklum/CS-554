package hw5;

import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import hw5.Compressor.Model;

public class DeltaEncoder implements Encoder
{
	@Override
	public void encode(BufferedImage image, Model model, int[] N, File output) throws Exception
	{
		DataOutputStream out = new DataOutputStream(new FileOutputStream(output));
		writeHeader(image, out);
		
		
		out.close();
	}

	@Override
	public String getMagicWord() 
	{
		return "Delta";
	}
}
