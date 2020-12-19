package exam;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

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
		double[][] energy = createEnergyMap();
		//outputEnergyMap(energy);
		if (getMode() == Mode.SIZE)
		{
			Parameter param = parameters.get(0);
			sizeImage(param.w, param.h);
		}

	}

	private void sizeImage(int width, int height)
	{
		int horizontalSeams = Math.abs(image.getHeight() - height);
		int verticalSeams =  Math.abs(image.getWidth() - width);
		
		boolean growHorizontal = image.getHeight() - height < 0;
		boolean growVertical = image.getWidth() - width < 0;
		
		while ((horizontalSeams + verticalSeams) > 0)
		{
			
		}
		
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
