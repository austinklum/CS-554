package hw5;

public class CACCompressor extends Compressor
{
	public static void main(String args[]) throws Exception
	{
		Compressor.run(args, Type.CAC, new CACEncoder(), new CACDecoder());
	}
}
