package hw1;

import java.awt.Color;

public class IndexedDigitalImage extends AbstractDigitalImage implements DigitalImage {
	private Color[] palette;
	private static final int BPP = 8;

	@Override
	public int[] getPixel(int x, int y) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPixel(int x, int y, int[] pixel) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getSample(int x, int y, int band) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setSample(int x, int y, int band, int sample) {
		// TODO Auto-generated method stub

	}
	
	public IndexedDigitalImage(int width, int height)
	{
		super(width, height, BPP);
		palette = new Color[256];
	}
	
	public IndexedDigitalImage(int width, int height, Color[] palette)
	{
		super(width, height, BPP);
		this.palette = palette;
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
