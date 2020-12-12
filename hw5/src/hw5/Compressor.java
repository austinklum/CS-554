package hw5;

public abstract class Compressor
{
	private enum Mode { ENCODE, DECODE };
	private enum Model { HSB, RGB };
	private enum Type { DCT, CAC, RLE, DMOD }
	
	private Mode mode;
	private String input;
	private Model model;
	int[] N;
	private String output;
	private Type type;
	
	
	public Compressor(Mode mode, String input, int N, String output)
	{
		
	}
	
	public Compressor(String[] args, Type type)
	{
		processArgs(args, type);
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
}
