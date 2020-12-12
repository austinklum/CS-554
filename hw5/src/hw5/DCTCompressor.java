package hw5;

public class DCTCompressor
{
	public static void main(String args[])
	{
		
	}

	private enum Mode { ENCODE, DECODE };
	
	private Mode mode;
	private String input;
	private int N;
	private String output;
	
	
	public DCTCompressor(Mode mode, String input, int N, String output)
	{
		this.mode = mode;
		this.input = input;
		this.N = N;
		this.output = output;
	}
	
	private DCTCompressor processArgs(String[] args)
	{
		int i = 1;
		Mode mode = Mode.valueOf(args[i++]);
		String input = args[i++];
		int N = -1;
		if (mode.equals("encode"))
		{
			N = Integer.parseInt(args[i++]);
		}
		String output = args[i];
		return new DCTCompressor(mode, input, N, output);
	}
}
