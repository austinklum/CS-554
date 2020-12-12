package hw5;

public class DCTCompressor extends Compressor
{
	public static void main(String args[]) throws Exception
	{
		Compressor.run(args, Type.DCT, new DCTEncoder(), new DCTDecoder());
	}
}
