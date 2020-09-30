package hw1;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class DigitalImageIO {
    public enum ImageType { INDEXED, PACKED, LINEAR_ARRAY, MULTIDIM_ARRAY };
    private static String magicNumber;
    private int width;
    private int height;
    private int maxColorValue;
    
    
    public static DigitalImage read( File file, ImageType type ) throws IOException, IllegalFileFormatException {
    	Scanner scan = new Scanner(file);
    	magicNumber = scan.next();
    	
    	
    	
    	return null;
    }
    
    public static void write( File file, DigitalImage image ) throws IOException {
        // TO-DO : write this method
    }
}
