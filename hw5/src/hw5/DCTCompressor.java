package hw5;

public class DCTCompressor extends Compressor
{
	public static void main(String args[])
	{
		DCTCompressor compressor = new DCTCompressor(args);
		//compressor.run();
	}


	
	
	public DCTCompressor(String[] args)
	{
		super(args, Type.DCT);
	}
	

}
