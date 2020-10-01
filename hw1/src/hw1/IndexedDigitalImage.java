package hw1;

import java.awt.Color;

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
		super(width, height, BANDS);
		raster = new byte[width * height * BANDS];
		this.palette = palette;
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
