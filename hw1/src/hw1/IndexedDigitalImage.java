package hw1;

import java.awt.Color;



/**
 * Implementation of IndexedDigitalImage using a Linearized array for a raster.
 * This implementation assumes there is only 1 band but leaves the code open to be easily change for more bands.
 * 
 * @author Austin Klum
 */
public class IndexedDigitalImage extends AbstractDigitalImage implements DigitalImage {
	private static final int BANDS = 1;
	private static final int MAX_PALETTE_SIZE = 256;

	private int[] raster;
	private Color[] palette;
	
	public IndexedDigitalImage(int width, int height)
	{
		super(width, height, BANDS);
		raster = new int[width * height * BANDS];
		palette = new Color[MAX_PALETTE_SIZE];
		generatePalette();
	}
	
	public IndexedDigitalImage(int width, int height, Color[] palette)
	{
		this(width, height);
        this.palette = palette;
	}
	
	@Override
	public int[] getPixel(int x, int y)
	{
        int[] result = new int[bands];
        System.arraycopy(raster, bands * (x + y * width), result, 0, bands);
        return result;
	}


	public void setPixel(int x, int y, int[] pixel)
	{
		int paletteIndex = findColorPaletteIndex(pixel);
		
		for (int i = 0; i < bands; i++)
		{
			raster[bands * (x + y * width) + i] = paletteIndex;
		}
	}

	@Override
	public int getSample(int x, int y, int band)
	{
		return raster[bands * (x + y * width) + band];
	}

	@Override
	public void setSample(int x, int y, int band, int sample)
	{
		 raster[bands * (x + y * width) + band] = sample;
	}

	public void setPaletteColor(int paletteIndex, Color color)
	{
		this.palette[paletteIndex] = color;
	}
	
	public Color getPaletteColor(int paletteIndex)
	{
		return palette[paletteIndex];
	}
	private int findColorPaletteIndex(int[] pixel) 
	{
		int index = -1;
		
		Color color = new Color(pixel[0], pixel[1], pixel[2]);
		int closestColor = -1;
		int i = 0;
		for (Color c : palette)
		{
			int distance = getColorDistance(c, color);
			if (distance < closestColor || closestColor == -1) 
			{
				closestColor = distance;
				index = i;
			}
			i++;
		}
		return index;
	}
	
	private int getColorDistance(Color colorToCheckAgainst, Color colorToAdd)
	{
		int redDiff = colorToCheckAgainst.getRed() - colorToAdd.getRed();
		int greenDiff = colorToCheckAgainst.getGreen() - colorToAdd.getGreen();
		int blueDiff = colorToCheckAgainst.getBlue() - colorToAdd.getBlue();
		
		int results = (int) Math.sqrt(redDiff*redDiff + greenDiff*greenDiff + blueDiff*blueDiff);
		return results;
	}
	
	public static byte hashPixel(int[] pixel)
	{
		String hash = convertPixelToHexCode(pixel);
		int hashCode = Integer.parseInt(hash, 16);
		return (byte) (hashCode % MAX_PALETTE_SIZE);
	}

	private static String convertPixelToHexCode(int[] pixel) 
	{
		String hash = "";
		for (int band : pixel)
		{
			int quotient = band / 16;
			float remainder = (float)band % 16;
			hash += Integer.toHexString(quotient);
			hash += Float.toHexString(remainder);
		}
		return hash;
	}
	
	private void generatePalette() {
		int paletteCount = 0;
		int incrementX8 = 36;
		int remainderX8 = 3;
		int incrementX4 = 85;
		
		for (int r = 0; r + remainderX8  < MAX_PALETTE_SIZE; r += incrementX8)
		{
			for (int g = 0; g + remainderX8 < MAX_PALETTE_SIZE; g += incrementX8)
			{
				for (int b = 0; b < MAX_PALETTE_SIZE; b += incrementX4)
				{
					 palette[paletteCount++] = new Color(r,g,b);
				}
			}
		}
	}
}
