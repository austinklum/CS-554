package hw1;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class DigitalImageIO 
{
    public enum ImageType { INDEXED, PACKED, LINEAR_ARRAY, MULTIDIM_ARRAY };
    
    public static DigitalImage read(File file, ImageType type ) throws IOException, IllegalFileFormatException
    {
    	Scanner scan = new Scanner(file);
    	DigitalImage image = getEmptyImage(type, scan);
    	scanFileToImage(scan, image);
    	
    	return image;
    }

	private static DigitalImage getEmptyImage(ImageType type, Scanner scan) throws IllegalFileFormatException {
		int paramCount = 0;
    	int width = -1;
    	int height = -1;
    	while(scan.hasNext() && paramCount < 4)
    	{
    		String next = scanNext(scan);
    		switch(paramCount)
    		{
    		case 1:
    			width = Integer.parseInt(next);
    			break;
    		case 2:
    			height = Integer.parseInt(next);
    			break;
    		}
    		paramCount++;
    	}
    	DigitalImage image = new ImageFactory().GetImage(type, width, height);
		return image;
	}
    
    private static void scanFileToImage(Scanner scan, DigitalImage image) 
    {
    	while (scan.hasNext())
    	{
    		scanNextPixel(scan);
    		//storePixel();
    		
    	}
	}
    
    private static int[] scanNextPixel(Scanner scan)
    {
    	int[] pixel = new int[3];
    	for (int i = 0; i < pixel.length; i++)
    	{
    		pixel[i] = Integer.parseInt(scanNext(scan));
    	}
    	return pixel;
    }
    
    private static String scanNext(Scanner scan) 
    {
    	String next = "";
    	while("".equals(next) && scan.hasNext())
    	{    		
    		next = scan.next();
    		if(next.startsWith("#")) 
    		{ 
    			scan.nextLine(); // Found a comment, scan out the line.
    			next = "";
    		}
    	}
    	return next;
    }

	public static void write( File file, DigitalImage image ) throws IOException
	{
        scanImageToFile(new Scanner(file), image);
    }
	
    private static void scanImageToFile(Scanner scan, DigitalImage image) 
    {
    	//writeNextPixel();
	}
	
}
