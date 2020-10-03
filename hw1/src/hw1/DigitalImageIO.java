package hw1;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

public class DigitalImageIO 
{
    public enum ImageType { INDEXED, PACKED, LINEAR_ARRAY, MULTIDIM_ARRAY };
    public static boolean useBinary;
    public static DigitalImage read(File file, ImageType type) throws IOException, IllegalFileFormatException
    {
    	InputStream stream = new FileInputStream(file);
    	String magicNumber = (char)stream.read() + "" + (char)stream.read();
    	stream.close();
    	if (magicNumber.equals("P6"))
    	{
    		return readInBinary(file, type);
    	}
		return readInNonBinary(file, type);
    }
    
    private static DigitalImage readInNonBinary(File file, ImageType type) throws FileNotFoundException, IllegalFileFormatException {
    	Scanner scan = new Scanner(file);
        String magicNumber = scanNext(scan);
    	DigitalImage image = getEmptyImage(type, scan);
    	scanFileToImage(scan, image);
    	scan.close();
    	return image;
    }
    
    private static DigitalImage readInBinary(File file, ImageType type) throws IOException {
    	BufferedInputStream stream = new BufferedInputStream(new FileInputStream(file));
    	int current = stream.read();
    	current = stream.read();
    	current = stream.read();
    	filterWhitespace(stream, current);
    	int width = getNextInt(stream, true);
    	int height = getNextInt(stream, true);
    	int maxColorValue = getNextInt(stream, false);
    	DigitalImage image = new ImageFactory().GetImage(type, width, height);
    	streamFileToImage(stream, image);
		return image;
    }

    private static void streamFileToImage(BufferedInputStream stream, DigitalImage image) throws IOException
    {
    	for (int row = 0; row < image.getHeight(); row++)
    	{
    		for (int col = 0; col < image.getWidth(); col++)
    		{
    			int[] pixel = readNextPixel(stream);
    			image.setPixel(col, row, pixel);
    		}
    	}
    }
    
    private static int[] readNextPixel(BufferedInputStream stream) throws IOException
    {
    	int[] pixel = new int[3];
    	for (int i = 0; i < pixel.length; i++)
    	{
    		pixel[i] = stream.read();
    	}
    	return pixel;
    }
    
    private static int getNextInt(BufferedInputStream stream, boolean filter) throws IOException
    {
    	String result = "";
    	int current = stream.read();
    	while (inNumberRange(current)) 
    	{
    		result += (char)current;
    		current = stream.read();
    	}
    	if(filter) filterWhitespace(stream, current);
    	return Integer.parseInt(result);
    }
    
    private static void filterWhitespace(BufferedInputStream stream, int current) throws IOException
    {
    	boolean inComment = false;
    	while ((!inNumberRange(current) || inComment) && current != 0)
    	{
    		stream.mark(1);
    		current = stream.read();
    		if (current == '#')
    		{
    			inComment = true;
    		}
    		if (current == '\n' || current == 13 || current == 10)
    		{
    			inComment = false;
    		}
    	} 
    	stream.reset();
    }

	private static void skipComment(InputStream stream, int current) throws IOException {
		while (current != '\n' && current != 13 && current != 10 ) 
		{
			current = stream.read();
		}
		stream.mark(1);
	}
    
    private static boolean inNumberRange(int value)
    {
    	return value >= 48 && value <= 57;
    }
    
	private static DigitalImage getEmptyImage(ImageType type, Scanner scan) throws IllegalFileFormatException {
		int paramCount = 0;
    	int width = -1;
    	int height = -1;
    	while(scan.hasNext() && paramCount < 3)
    	{
    		String next = scanNext(scan);
    		switch(paramCount)
    		{
    		case 0:
    			width = Integer.parseInt(next);
    			break;
    		case 1:
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
        writeImageToFile(writer, image, file);
        if (!useBinary) 
        {
	        writer.flush();
	        writer.close();
        }
    }
	
	private static void writeHeader(BufferedWriter writer, DigitalImage image) throws IOException
	{
		String magicNumber = useBinary ? "P6\n" : "P3\n";
		writer.write(magicNumber);
		writer.write(image.getWidth() + " " + image.getHeight() + "\n");
		writer.write("255\n");
	}
	
    private static void writeImageToFile(BufferedWriter writer, DigitalImage image, File file) throws IOException 
    {
    	OutputStream stream = null;
    	if (useBinary)
    	{
    		writer.flush();
    		writer.close();
    		stream = new FileOutputStream(file, true);
    	}
    	for (int x = 0; x < image.getHeight(); x++)
    	{
    		for (int y = 0; y < image.getWidth(); y++)
    		{
    			if (useBinary) {
    				writeNextPixel(stream,  image.getPixel(y, x));
	    		}
	    		else 
	    		{
	    			writeNextPixel(writer, image.getPixel(y, x));
	    		}
    		}
    	}
	}
    
    private static void writeNextPixel(OutputStream stream, int[] pixel) throws IOException
    {
    	byte[] bytePixel = new byte[pixel.length];
    	for (int i = 0; i < pixel.length; i++)
    	{
    		byte byteValue = Integer.valueOf(pixel[i]).byteValue();
    		//if(byteValue == -1) 
    		//	byteValue = Byte.toUnsignedInt(byteValue);
    		//int i2 = byteValue & 0xFF;
    		//bytePixel[i] = byteValue;
    		int ui = Byte.toUnsignedInt(byteValue);
    		stream.write(ui);
    	}
    	
    	byte[] newline = new byte[1];
    	newline[0] = Integer.valueOf(10).byteValue();
    	stream.write(newline);
    }
    
    private static void writeNextPixel(BufferedWriter writer, int[] pixel) throws IOException
    {
		for (int i = 0; i < pixel.length; i++)
		{
			writer.write(pixel[i] + " ");
		}
		writer.write("\n");
    }
    
    public static void useBinaryWrite(boolean useBinaryWrite)
    {
    	useBinary = useBinaryWrite;
    }
	
}
