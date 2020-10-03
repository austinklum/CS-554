package hw1;

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
    	InputStream stream = new FileInputStream(file);
    	stream.read();
    	stream.read();
    	filterWhitespace(stream);
    	int width = getNextInt(stream);
    	int height = getNextInt(stream);
    	int maxColorValue = getNextInt(stream);
    	DigitalImage image = new ImageFactory().GetImage(type, width, height);
    	streamFileToImage(stream, image);
		return image;
    }

    private static void streamFileToImage(InputStream stream, DigitalImage image) throws IOException
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
    
    private static int[] readNextPixel(InputStream stream) throws IOException
    {
    	int[] pixel = new int[3];
    	for (int i = 0; i < pixel.length; i++)
    	{
    		pixel[i] = stream.read();
    	}
    	return pixel;
    }
    
    private static int getNextInt(InputStream stream) throws IOException
    {
    	String result = "";
    	int current = stream.read();
    	while (inNumberRange(current)) 
    	{
    		result += Integer.toString(current);
    	}
    	filterWhitespace(stream);
    	return Integer.parseInt(result);
    }
    
    private static void filterWhitespace(InputStream stream) throws IOException
    {
    	int current = stream.read();
    	while (!inNumberRange(current))
    	{
    		stream.mark(1);
    		current = stream.read();
//    		if (current == '#')
//    		{
//    			skipComment(stream, current);
//    		}
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
        writer.flush();
        writer.close();
    }
	
	private static void writeHeader(BufferedWriter writer, DigitalImage image) throws IOException
	{
		String magicNumber = useBinary ? "P6\n" : "P3/n";
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
    		bytePixel[i] = Integer.valueOf(pixel[i]).byteValue();
    	}
    	stream.write(bytePixel);
    	
    	byte[] newline = new byte[1];
    	newline[0] = Integer.valueOf('\n').byteValue();
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
