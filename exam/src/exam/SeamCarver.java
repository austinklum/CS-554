package exam;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import exam.Seam.Direction;

public class SeamCarver
{
	private enum Mode { ERASE, SIZE }
	
	private Mode mode;
	private BufferedImage image;
	private BufferedImage ogImage;
	private String output;
	
	private List<Parameter> parameters;
	private List<Seam> seamsRemoved;
	
	public static void main(String args[]) throws Exception
	{
		SeamCarver carver = new SeamCarver(args);
		//carver.saveEnergyTable();
		carver.run();
		carver.writeOut();
		carver.saveSeamTable();
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
	
	private void run() throws Exception
	{
		if (getMode() == Mode.SIZE)
		{
			Parameter param = parameters.get(0);
			sizeImage(param.w, param.h);
		}
		else
		{
			erase();
		}

	}

	private void erase()
	{

		int[] widthArray = new int[image.getWidth()];
		int[] heightArray = new int[image.getHeight()];
		
		for(Parameter param : parameters)
		{
			for (int x = param.x; x < param.x + param.w; x++)
			{
				widthArray[x] = 1;
			}
			
			for (int y = param.y; y < param.y + param.h; y++) 
			{
				heightArray[y] = 1;
			}
		}
		
		int widthToErase = Arrays.stream(widthArray).sum();
		int heightToErase = Arrays.stream(heightArray).sum();
		
		int newWidth = image.getWidth() - widthToErase;
		int newHeight = image.getHeight() - heightToErase;
		
		sizeImage(newWidth, newHeight);
		
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
			seamsRemoved.add(seam);
			updateSeam(seam, growVertical, growHorizontal);
			if (seam.getDirection() == Direction.HORIZONTAL)
			{
				horizontalSeamsLeft--;
			}
			else 
			{
				verticalSeamsLeft--;
			}
			
			if((horizontalSeamsLeft + verticalSeamsLeft) % 10 == 0)
				System.out.println("Seams Left: " + (horizontalSeamsLeft + verticalSeamsLeft));
		}
		
	}

	private void updateSeam(Seam seam, boolean growVertical, boolean growHorizontal)
	{
		if (seam.getDirection() == Direction.HORIZONTAL)
		{
			image = growHorizontal ? addSeamHorizontal(seam) : removeSeamHorizontal(seam);
		}
		else
		{
			image = growVertical ? addSeamVertical(seam) : removeSeamVertical(seam);
		}
	}

	
	private BufferedImage addSeamHorizontal(Seam seam) 
	{
		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage newImage = new BufferedImage(width, height + 1, image.getType());
		for (int x = 0; x < width; x++) 
		{
			boolean addTo = false;
			for (int y = 0; y < height; y++)
			{
				if (seam.getPixels()[x] == y)
				{
					addTo = true;
				}
				if(addTo)
				{
					newImage.setRGB(x, y, image.getRGB(x, y));
					newImage.setRGB(x, y+1, image.getRGB(x, (y-1+height)%height));
				}
				else
				{
					newImage.setRGB(x, y, image.getRGB(x, y));
				}
			}
		}
		return newImage;
	}
	
	private BufferedImage removeSeamHorizontal(Seam seam) 
	{
		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage newImage = new BufferedImage(width, height - 1, image.getType());
		for (int x = 0; x < width; x++) 
		{
			boolean skip = false;
			for (int y = 0; y < height - 1; y++)
			{
				if (seam.getPixels()[x] == y)
				{
					skip = true;
				}
				if(skip)
				{
					newImage.setRGB(x, y, image.getRGB(x, y + 1));
				}
				else
				{
					newImage.setRGB(x, y, image.getRGB(x, y));
				}
			}
		}
		return newImage;
	}
	
	
	private BufferedImage addSeamVertical(Seam seam) 
	{
		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage newImage = new BufferedImage(width + 1, height, BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < height; y++) 
		{
			boolean addTo = false;
			for (int x = 0; x < width; x++)
			{
				if (seam.getPixels()[y] == x)
				{
					addTo = true;
				}
				if(addTo)
				{
					newImage.setRGB(x, y, image.getRGB(x, y));
					newImage.setRGB(x + 1, y, image.getRGB((x-1+width)%width, y));
				}
				else
				{
					newImage.setRGB(x, y, image.getRGB(x, y));
				}
			}
		}
		return newImage;
	}
	
	private BufferedImage removeSeamVertical(Seam seam) 
	{
		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage newImage = new BufferedImage(width - 1, height, BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < height; y++) 
		{
			boolean skip = false;
			for (int x = 0; x < width - 1; x++)
			{
				if (seam.getPixels()[y] == x)
				{
					skip = true;
				}
				if(skip)
				{
					newImage.setRGB(x, y, image.getRGB(x + 1, y));
				}
				else
				{
					newImage.setRGB(x, y, image.getRGB(x, y));
				}
			}
		}
		return newImage;
	}
	
	private Seam getSeam(int horizontalSeamsLeft, int verticalSeamsLeft)
	{
		double[][] energy = createEnergyMap();
		Seam seam = null;
		Seam horizontalSeam = Seam.createEmpty(image.getWidth(), Direction.HORIZONTAL);
		Seam verticalSeam = Seam.createEmpty(image.getHeight(), Direction.VERTICAL);
		if (horizontalSeamsLeft > 0)
		{
			horizontalSeam = getHorizontalSeam(energy);
		}
		
		if (verticalSeamsLeft > 0)
		{
			verticalSeam = getVerticalSeam(energy);
		}
		
		if (horizontalSeam.getEnergy() < verticalSeam.getEnergy())
		{
			seam = horizontalSeam;
		}
		else
		{
			seam = verticalSeam;
		}
		return seam;
	}
	
	private Seam getVerticalSeam(double[][] energy)
	{
		int width = image.getWidth();
		int height = image.getHeight();
		
		Seam seam = new Seam(height, Direction.VERTICAL);
		
		double[][] dynamic = new double[width][height];
		int[][] backtrack = new int[width][height];
		
		determineVerticalEnergies(energy, dynamic, backtrack);
		
		int minPos = setVerticalMinEnergy(seam, dynamic);
		//printDynamic(dynamic);
		setPixelsPathVertical(seam, backtrack, minPos);
		
		return seam;
	}
	
	private void printDynamic(double[][] dynamic)
	{
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 5; y++) {
				System.out.print(dynamic[x][y] + ":");
			}
			System.out.println();
		}
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
		
		for (int x = 0; x < width; x++) 
		{
			for (int y = 0; y < height; y++) 
			{
				if (x == 0)
				{
					dynamic[x][y] = energy[x][y];
					backtrack[x][y] = -1;
					continue;
				}
				double min = getHorizontalMin(dynamic, backtrack, x, y);
				dynamic[x][y] = energy[x][y] + min;
			}
		}
	}

	private void determineVerticalEnergies(double[][] energy, double[][] dynamic, int[][] backtrack)
	{
		int width = image.getWidth();
		int height = image.getHeight();
		
		for (int y = 0; y < height; y++) 
		{
			for (int x = 0; x < width; x++) 
			{
				if (y == 0)
				{
					dynamic[x][y] = energy[x][y];
					backtrack[x][y] = -1;
					continue;
				}
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
	
    public void saveEnergyTable() throws IOException
    {
        // calculate the energy table
        double[][] energyTable = createEnergyMap();

        // create the energy image
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
            System.out.println("Energy table has been created.");
        } catch (IOException e)
        {
            System.out.println("Cannot create file for energy table.");
            throw (e);
        }
    }

	private double[][] createEnergyMap()
	{
		double[][] energy = new double[image.getWidth()][image.getHeight()];
		
		for (int x = 0; x < image.getWidth(); x++) 
		{
            for (int y = 0; y < image.getHeight(); y++)
            {
                energy[x][y] = getEnergyValue(x, y);
            }
        }
		
		return energy;
	}

	private boolean withinEraseRegion(int x, int y)
	{
		boolean isWithin = false;
		for (Parameter param : parameters)
		{
			if ((x >= param.x && x <= param.x + param.w) || (y >= param.y && y <= param.y + param.h))
			{
				isWithin = true;
				break;
			}
		}
		
		return isWithin;
	}
	
	private double getEnergyValue(int x, int y)
	{
		int modifer = withinEraseRegion(x,y) ? -1 : 1;
		double xEnergy = getXEnergy(x, y);
		double yEnergy = getYEnergy(x, y);
	
		double energyValue = (xEnergy + yEnergy) * modifer;
		
		return energyValue;
	}
	
	private double getYEnergy(int x, int y)
	{
		int height = image.getHeight();
		int upPixel = image.getRGB(x,(y-1+height)%height);
		int downPixel =  image.getRGB(x, (y+1+height)%height);
		
		double energy = getEnergy(upPixel, downPixel);
		
		return energy;
	}
	
	private double getXEnergy(int x, int y)
	{
		int width = image.getWidth();
		int leftPixel = image.getRGB((x-1+width)%width, y);
		int rightPixel = image.getRGB((x+1+width)%width, y);
		
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
		
		return Math.sqrt(redDiff*redDiff + greenDiff*greenDiff + blueDiff*blueDiff);
	}
	
	private double getBlueDifference(int pixel, int otherPixel) 
	{
		return ((pixel) & 0xff) - ((otherPixel) & 0xff);
	}

	private double getGreenDifference(int pixel, int otherPixel) 
	{
		return ((pixel >> 8) & 0xff) - ((otherPixel >> 8) & 0xff);
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
		seamsRemoved = new LinkedList<>();
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
		this.ogImage = ImageIO.read(new URL(input));
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}
	
	public void writeOut() throws Exception
	{
		ImageIO.write(image, "jpg", new File(output));
	}
	
    public void saveSeamTable() throws IOException
    {
        System.out.println("Creating seam table...");
//        for(Seam seam : seamsRemoved)
//        {
//        	System.out.println(seam);
//        }
        int maxWidth = Math.max(ogImage.getWidth(), image.getWidth());
        int maxHeight = Math.max(ogImage.getHeight(), image.getHeight());
        // 2d array will keep the rgb values for the seam table image
        int[][] seam_image_array = new int[maxWidth+1][maxHeight+1];


        int current_width = image.getWidth();
        int current_height = image.getHeight();

        // copy the carved image to the array
        for(int i = 0; i < current_width; i++)
            for(int j = 0; j < current_height; j++)
                seam_image_array[i][j] = image.getRGB(i,j);


        // loop over the seams
        for(int s = seamsRemoved.size()-1; s >= 0; s--)
        {
            // for each seam
            Seam seam = seamsRemoved.get(s);
            if(seam.getDirection() == Direction.HORIZONTAL)
            {
                for(int i = 0; i < current_width; i++)
                {
                    int coord = seam.getPixels()[i];
                    for(int j = current_height; j > coord; j--)
                        seam_image_array[i][j] = seam_image_array[i][j-1];
                    seam_image_array[i][coord] = (0xffffff);

                }
                current_height++;
            }
            else
            {
                for(int j = 0; j < current_height; j++)
                {
                    //System.out.println(j + " " + current_height);
                    int coord = seam.getPixels()[j];
                    for(int i = current_width; i > coord; i--)
                        seam_image_array[i][j] = seam_image_array[i-1][j];
                    seam_image_array[coord][j] = (0xffffff);
                }

                current_width++;
            }

        }


        // create the seam image
        BufferedImage seam_image = new BufferedImage(ogImage.getWidth(), ogImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        // set rgb of the image using the array
        for(int i = 0; i < seam_image.getWidth(); i++)
        {
            for(int j = 0; j < seam_image.getHeight(); j++)
            {
                seam_image.setRGB(i,j,seam_image_array[i][j]);
            }
        }


        // save the seam table
        try {
            File output_file = new File("seamTable.jpg");
            ImageIO.write(seam_image, "jpg", output_file);
            System.out.println("Seam table has been created.");
        } catch (IOException e)
        {
            System.out.println("Cannot create file for seam table.");
            throw (e);
        }
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
