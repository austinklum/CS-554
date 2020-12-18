package exam;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

public class SeamCarver
{
	private enum Mode { ERASE, SIZE }
	
	private Mode mode;
	private BufferedImage input;
	private String output;
	
	private List<Parameter> parameters;
	

	
	public static void main(String args[])
	{
		SeamCarver carver = new SeamCarver(args);
		carver.run();
	}
	
	public SeamCarver(String[] args)
	{
		try
		{
			processArgs(args);
		}
		catch (Exception e)
		{
			System.out.println("Error Processing Arguments!");
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());
			System.exit(-1);
		}
	}
	
	private void run()
	{
		double[][] energy = createEnergyMap();
	}

	private double[][] createEnergyMap()
	{
		return null;
	}
	
	private void processArgs(String[] args) throws Exception
	{
		int i = 0;
		setInput(args[i++]);
		setOutput(args[i++]);
		setMode(Mode.valueOf(args[i++].replace("-", "").toUpperCase()));
		setParameters(i, args);
	}
	
	private void setParameters(int i, String[] args)
	{
		parameters = new LinkedList<Parameter>();
		if (getMode() == Mode.SIZE)
		{
			String[] size = args[i].split(",");
			int w = Integer.parseInt(size[0]);
			int h = Integer.parseInt(size[1]);
			parameters.add(new Parameter(w,h));
			return;
		}
		
		while(i < args.length)
		{
			String[] params = args[i++].split(",");
			int x = Integer.parseInt(params[0]);
			int y = Integer.parseInt(params[1]);
			int w = Integer.parseInt(params[2]);
			int h = Integer.parseInt(params[3]);
			parameters.add(new Parameter(x,y,w,h));
		}
		
	}
	
	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}

	public BufferedImage getInput() {
		return input;
	}

	public void setInput(String input) throws Exception
	{
		BufferedImage img = ImageIO.read(new URL(input));
		this.input = img;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}
	
	private class Parameter
	{
		int x;
		int y;
		int w;
		int h;
		public Parameter(int x, int y, int w, int h)
		{
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
		}
		public Parameter(int w, int h)
		{
			this(-1,-1,w,h);
		}
	}
}
