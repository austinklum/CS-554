package hw5;

import hw5.Compressor.Type;

public class CACCompressor extends Compressor
{
	public static void main(String args[]) throws Exception
	{
		Compressor.run(args, Type.CAC, new DCTEncoder(), new DCTDecoder());
	}
}
