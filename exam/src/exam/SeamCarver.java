package exam;

import java.util.LinkedList;
import java.util.List;

public class SeamCarver
{
	private enum Mode { ERASE, SIZE }
	
	private Mode mode;
	private String input;
	private String output;
	
	private List<Parameter> parameters;
	
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
	
	public static void main(String args[])
	{
		SeamCarver carver = new SeamCarver(args);
		//carver.run();
	}
	
	public SeamCarver(String[] args)
	{
		processArgs(args);
	}
	
	
	private void processArgs(String[] args)
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

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}
}
