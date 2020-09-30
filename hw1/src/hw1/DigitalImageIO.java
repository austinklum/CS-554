package hw1;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class DigitalImageIO {
    public enum ImageType { INDEXED, PACKED, LINEAR_ARRAY, MULTIDIM_ARRAY };
    private static String magicNumber;
    private int maxColorValue;
    
    
    public static DigitalImage read( File file, ImageType type ) throws IOException, IllegalFileFormatException {
    	Scanner scan = new Scanner(file);
    	magicNumber = scan.next();
    	int width = Integer.parseInt(scan.next());
    	int height = Integer.parseInt(scan.next());
    	
    	DigitalImage image = new ImageFactory().GetImage(type, width, height);
    	scanFileToImage(scan, file, image);
    	
    	return image;
    }
    
    private static void scanFileToImage(Scanner scan, File file, DigitalImage image) {
 
	}

	public static void write( File file, DigitalImage image ) throws IOException {
        // TO-DO : write this method
    }
}
