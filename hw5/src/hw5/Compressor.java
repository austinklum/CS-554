package hw5;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public abstract class Compressor
{
	public enum Mode { ENCODE, DECODE };
	public enum Model { HSB, RGB };
	public enum Type { DCT, CAC, RLE, DMOD }
	
	private Mode mode;
	private String input;
	private Model model;
	int[] N;
	private String output;
	private Type type;
	
	private Encoder encoder;
	
	public Compressor(String[] args, Type type, Encoder encoder)
	{
		processArgs(args, type);
		setEncoder(encoder);
	}
	
	private void processArgs(String[] args, Type type)
	{
		int i = 1;
		setType(type);
		setMode(Mode.valueOf(args[i++]));
		setInput(args[i++]);
		if (mode == Mode.ENCODE)
		{
			i = setupEncode(i, args);
		}
		setOutput(args[i]);
	}
	
	public void run() throws Exception
	{
		if (mode == Mode.ENCODE)
		{
			BufferedImage image = ImageIO.read(new URL(input));
			encoder.encode(image, model, N, output);
		}
		else
		{
			//decoder.decode();
		}
	}

	private int setupEncode(int i, String[] args) 
	{
		if (type == Type.DCT)
		{
			N[0] = Integer.parseInt(args[i++]);
		}
		else
		{
			model = Model.valueOf(args[i++]);
			if (type == Type.CAC || type == Type.DMOD)
			{
				N[0] = Integer.parseInt(args[i++]);
				N[1] = Integer.parseInt(args[i++]);
				N[2] = Integer.parseInt(args[i++]);
			}
		}
		return i;
	}
	
	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}
	
	public int[] getN()
	{
		return N;
	}

	public void setN(int[] N)
	{
		this.N = N;
	}
	public Type getType()
	{
		return type;
	}
	private void setType(Type type)
	{
		this.type = type;
	}

	public Encoder getEncoder() {
		return encoder;
	}

	public void setEncoder(Encoder encoder) {
		this.encoder = encoder;
	}
}
