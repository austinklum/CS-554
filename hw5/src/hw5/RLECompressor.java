package hw5;

import hw5.Compressor.Type;

public class RLECompressor extends Compressor
{
	public static void main(String args[]) throws Exception
	{
		Compressor.run(args, Type.RLE, new RLEEncoder(), new RLEDecoder());
	}
}
