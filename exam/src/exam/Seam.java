package exam;

public class Seam 
{

	public enum Direction {VERTICAL, HORIZONTAL}
	private double energy;
	private int[] pixels;
	private Direction direction;
	
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
}
