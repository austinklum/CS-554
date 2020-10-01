package hw1;

import java.awt.Color;



/**
 * Implementation of IndexedDigitalImage using a Linearized byte array for a raster.
 * This implementation assumes there is only 1 band but leaves the code open to be easily change for more bands.
 * 
 * @author Austin Klum
 */
public class IndexedDigitalImage extends AbstractDigitalImage implements DigitalImage {
	private static final int BANDS = 1;
	private static final int MAX_PALETTE_SIZE = 256;

	private byte[] raster;
	private Color[] palette;
	
	public IndexedDigitalImage(int width, int height)
	{
		super(width, height, BANDS);
		raster = new byte[width * height * BANDS];
		palette = new Color[MAX_PALETTE_SIZE];
	}
	
	public IndexedDigitalImage(int width, int height, Color[] palette)
	{
		this(width, height);
        System.arraycopy(palette, 0, this.palette, 0, palette.length); // Copy passed in palette to full sized palette.
	}
	
	@Override
	public int[] getPixel(int x, int y)
	{
        int[] result = new int[bands];
        System.arraycopy(raster, bands * (x + y * width), result, 0, bands);
        return result;
	}

	@Override
	public void setPixel(int x, int y, int[] pixel)
	{
		System.arraycopy(pixel, 0, raster, bands * (x + y * width), bands);
	}

	@Override
	public int getSample(int x, int y, int band)
	{
		return raster[bands * (x + y * width) + band];
	}

	@Override
	public void setSample(int x, int y, int band, int sample)
	{
		 raster[bands * (x + y * width) + band] = (byte) sample;
	}

	public void setPaletteColor(int paletteIndex, Color color)
	{
		this.palette[paletteIndex] = color;
	}
	
	public Color getPaletteColor(int paletteIndex)
	{
		return palette[paletteIndex];
	}
}
