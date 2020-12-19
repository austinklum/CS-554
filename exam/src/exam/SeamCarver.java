package exam;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import exam.Seam.Direction;

public class SeamCarver
{
	private enum Mode { ERASE, SIZE }
	
	private Mode mode;
	private BufferedImage image;
	private String output;
	
	private List<Parameter> parameters;
	
	public static void main(String args[])
	{
		SeamCarver carver = new SeamCarver(args);
		carver.run();
	}
	
	public SeamCarver(String[] args)
	{
		try
		{
			processArgs(args);
		}
		catch (Exception e)
		{
			System.out.println("Error Processing Arguments!");
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());
			System.exit(-1);
		}
	}
	
	private void run()
	{
		if (getMode() == Mode.SIZE)
		{
			Parameter param = parameters.get(0);
			sizeImage(param.w, param.h);
		}

	}

	private void sizeImage(int width, int height)
	{
		
		int horizontalSeamsLeft = Math.abs(image.getHeight() - height);
		int verticalSeamsLeft =  Math.abs(image.getWidth() - width);
		
		boolean growHorizontal = image.getHeight() - height < 0;
		boolean growVertical = image.getWidth() - width < 0;
		
		while ((horizontalSeamsLeft + verticalSeamsLeft) > 0)
		{
			Seam seam = getSeam(horizontalSeamsLeft, verticalSeamsLeft);
			
		}
		
	}

	private Seam getSeam(int horizontalSeamsLeft, int verticalSeamsLeft)
	{
		double[][] energy = createEnergyMap();
		Seam horizontalSeam = null;
		Seam verticalSeam = null;
		if (horizontalSeamsLeft > 0)
		{
			horizontalSeam = getHorizontalSeam(energy);
		}
		
		if (verticalSeamsLeft > 0)
		{
			verticalSeam = getVerticalSeam(energy);
		}
		
		return null;
	}
	
	private Seam getVerticalSeam(double[][] energy)
	{
		int width = image.getWidth();
		int height = image.getHeight();
		
		Seam seam = new Seam(width, Direction.VERTICAL);
		
		double[][] dynamic = new double[width][height];
		int[][] backtrack = new int[width][height];
		
		determineVerticalEnergies(energy, dynamic, backtrack);
		
		int minPos = setVerticalMinEnergy(seam, dynamic);
		
		setPixelsPathVertical(seam, backtrack, minPos);
		
		return seam;
	}
	
	private Seam getHorizontalSeam(double[][] energy)
	{
		int width = image.getWidth();
		int height = image.getHeight();
		
		Seam seam = new Seam(width, Direction.HORIZONTAL);
		
		double[][] dynamic = new double[width][height];
		int[][] backtrack = new int[width][height];
		
		determineHorizontalEnergies(energy, dynamic, backtrack);
		
		int minPos = setHorizontalMinEnergy(seam, dynamic);
		
		setPixelsPathHorizontal(seam, backtrack, minPos);
		
		return seam;
	}

	private void setPixelsPathHorizontal(Seam horizontalSeam, int[][] backtrack, int minPos) 
	{
		int width = image.getWidth();
		for (int x = width-1; x >= 0; x--)
		{
			horizontalSeam.setPixel(x, minPos);
			minPos = backtrack[x][minPos];
		}
	}
	
	private void setPixelsPathVertical(Seam seam, int[][] backtrack, int minPos) 
	{
		int height = image.getHeight();
		for (int y = height-1; y >= 0; y--)
		{
			seam.setPixel(y, minPos);
			minPos = backtrack[minPos][y];
		}
	}

	private int setHorizontalMinEnergy(Seam horizontalSeam, double[][] dynamic)
	{
		int width = image.getWidth();
		int height = image.getHeight();
		
		double minEnergy = dynamic[width-1][0];
		int minPos = 0;
		
		for (int y = 0; y < height; y++)
		{
			if (minEnergy > dynamic[width-1][y])
			{
				minEnergy = dynamic[width-1][y];
				minPos = y;
			}
		}
		
		horizontalSeam.setEnergy(minEnergy);
		return minPos;
	}
	
	private int setVerticalMinEnergy(Seam seam, double[][] dynamic)
	{
		int width = image.getWidth();
		int height = image.getHeight();
		
		double minEnergy = dynamic[0][height-1];
		int minPos = 0;
		
		for (int x = 0; x < width; x++)
		{
			if (minEnergy > dynamic[x][height-1])
			{
				minEnergy = dynamic[x][height-1];
				minPos = x;
			}
		}
		
		seam.setEnergy(minEnergy);
		return minPos;
	}

	private void determineHorizontalEnergies(double[][] energy, double[][] dynamic, int[][] backtrack)
	{
		int width = image.getWidth();
		int height = image.getHeight();
		
		for (int y = 0; y < height; y++)
		{
			dynamic[0][y] = energy[0][y];
			backtrack[0][y] = -1;
		}
		
		for (int x = 1; x < width; x++) 
		{
			for (int y = 0; y < height; y++) 
			{
				double min = getHorizontalMin(dynamic, backtrack, x, y);
				dynamic[x][y] = energy[x][y] + min;
			}
		}
	}

	private void determineVerticalEnergies(double[][] energy, double[][] dynamic, int[][] backtrack)
	{
		int width = image.getWidth();
		int height = image.getHeight();
		
		for (int x = 0; x < width; x++)
		{
			dynamic[x][0] = energy[x][0];
			backtrack[x][0] = -1;
		}
		
		for (int x = 0; x < width; x++) 
		{
			for (int y = 1; y < height; y++) 
			{
				double min = getVerticalMin(dynamic, backtrack, x, y);
				dynamic[x][y] = energy[x][y] + min;
			}
		}
	}
	
	private double getHorizontalMin(double[][] dynamic, int[][] backtrack, int x, int y) {
		double min;
		if (isTopEdge(y))
		{
			min = minTopEdge(dynamic, backtrack, x, y);
		}
		else if (isBottomEdge(y))
		{
			min = getBottomMin(dynamic, backtrack, x, y);
		}
		else
		{
			min = getHorizontalMiddleMin(dynamic, backtrack, x, y);
		}
		return min;
	}

	private double getVerticalMin(double[][] dynamic, int[][] backtrack, int x, int y) {
		double min;
		if (isLeftEdge(x))
		{
			min = minLeftEdge(dynamic, backtrack, x, y);
		}
		else if (isRightEdge(x))
		{
			min = getRightMin(dynamic, backtrack, x, y);
		}
		else
		{
			min = getVerticalMiddleMin(dynamic, backtrack, x, y);
		}
		return min;
	}
	
	private double getHorizontalMiddleMin(double[][] dynamic, int[][] backtrack, int x, int y)
	{
		double min = Math.min(dynamic[x-1][y], Math.min(dynamic[x-1][y-1], dynamic[x-1][y+1]));
		if (min == dynamic[x-1][y])
		{
			backtrack[x][y] = y;
		}
		else if (min == dynamic[x-1][y-1])
		{
			backtrack[x][y] = y-1;
		}
		else
		{
			backtrack[x][y] = y+1;
		}
		return min;
	}

	private double getVerticalMiddleMin(double[][] dynamic, int[][] backtrack, int x, int y)
	{
		double min = Math.min(dynamic[x][y-1], Math.min(dynamic[x-1][y-1], dynamic[x+1][y-1]));
		if (min == dynamic[x][y-1])
		{
			backtrack[x][y] = x;
		}
		else if (min == dynamic[x-1][y-1])
		{
			backtrack[x][y] = x-1;
		}
		else
		{
			backtrack[x][y] = x+1;
		}
		return min;
	}
	
	private double getBottomMin(double[][] dynamic, int[][] backtrack, int x, int y)
	{
		double min = Math.min(dynamic[x-1][y], dynamic[x-1][y-1]);
		if (min == dynamic[x-1][y])
		{
			backtrack[x][y] = y;
		}
		else
		{
			backtrack[x][y] = y-1;
		}
		return min;
	}
	

	private double getRightMin(double[][] dynamic, int[][] backtrack, int x, int y)
	{
		double min = Math.min(dynamic[x][y-1], dynamic[x-1][y-1]);
		if (min == dynamic[x][y-1])
		{
			backtrack[x][y] = x;
		}
		else
		{
			backtrack[x][y] = x-1;
		}
		return min;
	}

	private double minTopEdge(double[][] dynamic, int[][] backtrack, int x, int y)
	{
		double min = Math.min(dynamic[x-1][y], dynamic[x-1][y+1]);
		if (min == dynamic[x-1][y])
		{
			backtrack[x][y] = y;
		}
		else
		{
			backtrack[x][y] = y+1;
		}
		return min;
	}
	
	private double minLeftEdge(double[][] dynamic, int[][] backtrack, int x, int y)
	{
		double min = Math.min(dynamic[x][y-1], dynamic[x+1][y-1]);
		if (min == dynamic[x][y-1])
		{
			backtrack[x][y] = x;
		}
		else
		{
			backtrack[x][y] = x+1;
		}
		return min;
	}
	
	private void outputEnergyMap(double[][] energyTable)
	{
		 BufferedImage energy_image = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

        //find the max value in the energy table
        double maxValue = energyTable[0][0];
        for(int i = 0; i < energyTable.length; i++)
            for(int j = 0; j < energyTable[0].length;j++)
                maxValue = Math.max(maxValue, energyTable[i][j]);

        // loop over each pixel
        for(int i = 0; i < energy_image.getWidth(); i++)
        {
            for(int j = 0; j < energy_image.getHeight(); j++)
            {
                // calculate the rgb value (scaled for grayscale)
                int gray = (int) ((energyTable[i][j]/maxValue)*256);
                int rgb = (gray << 16) + (gray << 8) + gray;
                energy_image.setRGB(i,j,rgb);
            }
        }
        //save the energy table image
        try {
            File output_file = new File("energyTable.jpg");
            ImageIO.write(energy_image, "jpg", output_file);
        } catch (IOException e)
        {
            System.out.println("Cannot create file for energy table.");
        }
    }
	
	private double[][] createEnergyMap()
	{
		double[][] energy = new double[image.getWidth()][image.getHeight()];
		
		for (int y = 0; y < image.getHeight(); y++) 
		{
            for (int x = 0; x < image.getWidth(); x++)
            {
                energy[x][y] = getEnergyValue(x, y);
            }
        }
		
		return energy;
	}

	private double getEnergyValue(int x, int y)
	{
		double xEnergy = getXEnergy(x, y);
		double yEnergy = getYEnergy(x, y);

		return xEnergy + yEnergy;
	}
	
	private double getYEnergy(int x, int y)
	{
		int upPixel = getUpPixel(x, y);
		int downPixel = getDownPixel(x, y);
		
		double energy = getEnergy(upPixel, downPixel);
		
		return energy;
	}
	
	private double getXEnergy(int x, int y)
	{
		int leftPixel = getLeftPixel(x, y);
		int rightPixel = getRightPixel(x, y);
		
		double energy = getEnergy(leftPixel, rightPixel);
		
		return energy;
	}

	private int getUpPixel(int x, int y) 
	{
		if (!isTopEdge(y))
		{
			y--;
		} 
		return image.getRGB(x, y);
	}
	
	private int getDownPixel(int x, int y) 
	{
		if (!isBottomEdge(y))
		{
			y++;
		} 
		return image.getRGB(x, y);
	}
	
	private int getLeftPixel(int x, int y) 
	{
		if (!isLeftEdge(x))
		{
			x--;
		} 
		return image.getRGB(x, y);
	}
	
	private int getRightPixel(int x, int y) 
	{
		if (!isRightEdge(x))
		{
			x++;
		} 
		return image.getRGB(x, y);
	}

	private boolean isLeftEdge(int x)
	{
		return x == 0;
	}
	
	private boolean isRightEdge(int x)
	{
		return (x + 1) == image.getWidth();
	}
	
	private boolean isTopEdge(int y)
	{
		return y == 0;
	}
	
	private boolean isBottomEdge(int y)
	{
		return (y + 1) == image.getHeight();
	}
	
	private double getEnergy(int pixel, int otherPixel)
	{
		double redDiff = getRedDifference(pixel, otherPixel);
		double greenDiff = getGreenDifference(pixel, otherPixel);
		double blueDiff = getBlueDifference(pixel, otherPixel);
		
		return redDiff*redDiff + greenDiff*greenDiff + blueDiff*blueDiff;
	}
	
	private double getBlueDifference(int pixel, int otherPixel) 
	{
		return (pixel) & 0xff - (otherPixel) & 0xff;
	}

	private double getGreenDifference(int pixel, int otherPixel) 
	{
		return ((pixel >> 8) & 0xff) - ((otherPixel >> 8) & 0x0000ff00);
	}

	private double getRedDifference(int pixel, int otherPixel) 
	{
		return ((pixel >> 16) & 0xff) - ((otherPixel >> 16) & 0xff) ;
	}
	
	private void processArgs(String[] args) throws Exception
	{
		int i = 0;
		setImage(args[i++]);
		setOutput(args[i++]);
		setMode(Mode.valueOf(args[i++].replace("-", "").toUpperCase()));
		setParameters(i, args);
	}
	
	private void setParameters(int i, String[] args)
	{
		parameters = new LinkedList<Parameter>();
		if (getMode() == Mode.SIZE)
		{
			String[] size = args[i].split(",");
			int w = Integer.parseInt(size[0]);
			int h = Integer.parseInt(size[1]);
			parameters.add(new Parameter(w,h));
			return;
		}
		
		while(i < args.length)
		{
			String[] params = args[i++].split(",");
			int x = Integer.parseInt(params[0]);
			int y = Integer.parseInt(params[1]);
			int w = Integer.parseInt(params[2]);
			int h = Integer.parseInt(params[3]);
			parameters.add(new Parameter(x,y,w,h));
		}
		
	}
	
	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(String input) throws Exception
	{
		BufferedImage img = ImageIO.read(new URL(input));
		this.image = img;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}
	
	private class Parameter
	{
		int x;
		int y;
		int w;
		int h;
		public Parameter(int x, int y, int w, int h)
		{
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
		}
		public Parameter(int w, int h)
		{
			this(-1,-1,w,h);
		}
	}
}
