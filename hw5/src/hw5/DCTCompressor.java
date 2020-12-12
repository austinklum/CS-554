package hw5;

import java.io.IOException;

public class DCTCompressor extends Compressor
{
	public static void main(String args[]) throws Exception
	{
		DCTCompressor compressor = new DCTCompressor(args);
	}

	public DCTCompressor(String[] args) throws Exception
	{
		super(args, Type.DCT, new DCTEncoder(), new DCTDecoder());
		this.run();
	}
	

}
