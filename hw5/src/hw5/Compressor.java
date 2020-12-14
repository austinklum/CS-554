package hw5;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;

public abstract class Compressor
{
	public enum Mode { ENCODE, DECODE };
	public enum Model { HSB, RGB };
	public enum Type { DCT, CAC, RLE, DMOD }
	
	private static Mode mode;
	private static String input;
	private static Model model;
	private static int[] N;
	private static String output;
	
	private static void processArgs(String[] args, Type type)
	{
		int i = 0;
		setMode(Mode.valueOf(args[i++].toUpperCase()));
		setInput(args[i++]);
		i = setupEncode(i, args, type);
		setOutput(args[i]);
	}
	
	public static void run(String[] args, Type type, Encoder encoder, Decoder decoder) throws Exception
	{
		processArgs(args, type);
		if (getMode() == Mode.ENCODE)
		{
			BufferedImage image = ImageIO.read(new URL(input));
			encoder.encode(image, model, N, new File(output));
		}
		else
		{
			BufferedImage image = decoder.decode(new File(input), new File(output));
			if (image != null)
			{
				ImageIO.write(image, "PNG", new FileOutputStream(output));
			}
		}
	}

	private static int setupEncode(int i, String[] args, Type type) 
	{
		N = new int[3];
		if(getMode() != Mode.ENCODE)
		{
			return i;
		}
		
		if (type == Type.DCT)
		{
			N[0] = Integer.parseInt(args[i++]);
		}
		else
		{
			setModel(Model.valueOf(args[i++].toUpperCase()));
			if (type == Type.CAC)
			{
				N[0] = Integer.parseInt(args[i++]);
				N[1] = Integer.parseInt(args[i++]);
				N[2] = Integer.parseInt(args[i++]);
			}
			if (type == Type.DMOD)
			{
				N[0] = (int) Double.parseDouble(args[i++]);
				N[1] = (int) Double.parseDouble(args[i++]);
				N[2] = (int) Double.parseDouble(args[i++]);
			}
		}
		return i;
	}
	
	public static Mode getMode() {
		return mode;
	}

	public static void setMode(Mode mode) {
		Compressor.mode = mode;
	}

	public static String getInput() {
		return input;
	}

	public static void setInput(String input) {
		Compressor.input = input;
	}

	public static Model getModel() {
		return model;
	}

	public static void setModel(Model model) {
		Compressor.model = model;
	}

	public String getOutput() {
		return output;
	}

	public static void setOutput(String output) {
		Compressor.output = output;
	}
	
	public static int[] getN()
	{
		return N;
	}
}
