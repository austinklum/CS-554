package hw5;

import java.io.IOException;

public class DCTCompressor extends Compressor
{
	public static void main(String args[])
	{
		DCTCompressor compressor = new DCTCompressor(args);
		try 
		{
			compressor.run();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(-1);
		}
	}


	
	
	public DCTCompressor(String[] args)
	{
		super(args, Type.DCT, new DCTEncoder());
	}
	

}
