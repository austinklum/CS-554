package exam;

public class Seam 
{

	public enum Direction {VERTICAL, HORIZONTAL}
	private double energy;
	private int[] pixels;
	private Direction direction;
	
	public static Seam createEmpty(int length, Direction direction)
	{
		Seam seam = new Seam(length, direction);
		seam.setEnergy(Double.MAX_VALUE);
		return seam;
	}
	
	public Seam(int length, Direction direction)
	{
		setPixels(length);
		setDirection(direction);
	}

	public double getEnergy()
	{
		return energy;
	}

	public void setEnergy(double energy)
	{
		this.energy = energy;
	}

	public int[] getPixels() 
	{
		return pixels;
	}

	public void setPixel(int index, int pixel)
	{
		this.pixels[index] = pixel;
	}
	
	public void setPixels(int length)
	{
		this.pixels = new int[length];
	}

	public Direction getDirection()
	{
		return direction;
	}

	public void setDirection(Direction direction) 
	{
		this.direction = direction;
	}
	
	private String getStringPixels()
	{
		String str = "";
		for(int pixel : pixels)
		{
			str += pixel + ":";
		}
		return str;
	}
	
	public String toString()
	{
		return this.direction + " " + this.energy + " " + getStringPixels();
	}
}
