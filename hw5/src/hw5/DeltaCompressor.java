package hw5;

import hw5.Compressor.Type;

public class DeltaCompressor extends Compressor 
{

	public static void main(String[] args) throws Exception 
	{
		Compressor.run(args, Type.DMOD, new DeltaEncoder(), new DeltaDecoder());
	}

}
