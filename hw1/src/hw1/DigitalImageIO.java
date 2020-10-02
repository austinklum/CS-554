package hw1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
    	for (int row = 0; row < image.getHeight(); row++)
    	{
    		for (int col = 0; col < image.getWidth(); col++)
    		{
    			int[] pixel = scanNextPixel(scan);
    			image.setPixel(col, row, pixel);
    		}
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
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writeHeader(writer, image);
        writeImageToFile(writer, image);
        writer.flush();
        writer.close();
    }
	
	private static void writeHeader(BufferedWriter writer, DigitalImage image) throws IOException
	{
		writer.write("P3\n");
		writer.write(image.getWidth() + " " + image.getHeight() + "\n");
		writer.write("255\n");
	}
	
    private static void writeImageToFile(BufferedWriter writer, DigitalImage image) throws IOException 
    {
    	for (int x = 0; x < image.getHeight(); x++)
    	{
    		for (int y = 0; y < image.getWidth(); y++)
    		{
    			writeNextPixel(writer, image.getPixel(y, x));
    			writer.write("\n");
    		}
    	}
	}
    
    private static void writeNextPixel(BufferedWriter writer, int[] pixel) throws IOException
    {
    	for (int i = 0; i < pixel.length; i++)
    	{
    		writer.write(pixel[i] + " ");
    	}
    }
	
}
