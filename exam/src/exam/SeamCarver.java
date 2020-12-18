package exam;

public class SeamCarver
{
	private static void processArgs(String[] args, Type type)
	{
		int i = 0;
		setMode(Mode.valueOf(args[i++].toUpperCase()));
		setInput(args[i++]);
		i = setupEncode(i, args, type);
		setOutput(args[i]);
	}
}
