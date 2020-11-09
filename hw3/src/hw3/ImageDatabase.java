package hw3;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.naming.directory.InvalidAttributesException;

import org.ujmp.core.DenseMatrix;
import org.ujmp.core.Matrix;

import pixeljelly.features.Histogram;
import pixeljelly.scanners.Location;
import pixeljelly.scanners.RasterScanner;

public class ImageDatabase 
{
	public static void main(String[] args)
	{
		try {
			ImageDatabase imgDB = new ImageDatabase(args);
			imgDB.run();
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
			System.out.println(
					"Usage: create <Xn> <Yn> <Zn> <URL_FILENAME> <DB_FILENAME> <COLOR_MODEL>\n"
					+ " OR\n "
					+ "query <Q_URL> <DB_FILENAME> <RESPONSE_FILENAME> <K>");
			System.exit(-1);
		}
	}
	
	private enum ColorModel
	{
		RGB,
		HSB
	}
	
	private enum CommandType
	{
		CREATE,
		QUERY
	}
	private CommandType commandType;
	private int xn;
	private int yn;
	private int zn;
	private String urlFile;
	private String dbFile;
	private ColorModel colorModel;
	
	private String queryUrl;
	private String responseFile;
	private int kNumberOfImages;
	
	private static final int MAX_RGB = 256;

	public ImageDatabase(String[] args) throws InvalidAttributesException 
	{
		setDefaults();
		commandType = CommandType.valueOf(args[0].toUpperCase());
		if(isCreate())
		{
			parseCreateArgs(args);
		}
		else if (isQuery())
		{
			parseQueryArgs(args);
		}
		else
		{
			throw new InvalidAttributesException();
		}
	}
	
	private void setDefaults()
	{
		commandType = null;
		
		xn = 2;//-1;
		yn = 2;//-1;
		zn = 2;//-1;
		urlFile = null;
		dbFile = null;
		colorModel = null;
		
		queryUrl = null;
		responseFile = null;
		kNumberOfImages = -1;
	}
	
	private boolean isCreate()
	{
		return commandType == CommandType.CREATE;
	}
	
	private boolean isQuery()
	{
		return commandType == CommandType.QUERY;
	}
	
	private void parseCreateArgs(String[] args) throws InvalidAttributesException
	{
		xn = (int) Math.pow(Integer.parseInt(args[1]), 2);
		yn = (int) Math.pow(Integer.parseInt(args[2]), 2);
		zn = (int) Math.pow(Integer.parseInt(args[3]), 2);
		if((xn + yn + zn) > 1024)
		{
			throw new InvalidAttributesException("Sum of xn yn zn is greater than 10");
		}
		urlFile = args[4];
		dbFile = args[5];
		colorModel = ColorModel.valueOf(args[6]);
	}
	
	private void parseQueryArgs(String[] args)
	{
		queryUrl = args[1];
		dbFile = args[2];
		responseFile = args[3];
		kNumberOfImages = Integer.parseInt(args[4]);
	}
	
	private void run()
	{
		try
		{
			if(isCreate())
			{
				runCreate();
			}
			else if (isQuery())
			{
				runQuery();
			}
		}
		catch (Exception ex)
		{
			System.out.println(ex);
			System.out.println(ex.getStackTrace());
			System.out.println("Exiting program...");
			System.exit(-1);
		}
	}
	
	private void runCreate()
	{
		// loadFile
		// LoopOverFile
		// constructHistogram(image);
		// writeDB(histograms);
	}
	
	private void runQuery() throws IOException
	{
		 List<double[]> DB = loadDB();
		BufferedImage queryImage = ImageIO.read(new URL(queryUrl));
		double[] histogram = constructHistogram(queryImage);
		 HashMap<double[], Double> histogramSimilarity = computeSimilarites(histogram, DB);
		// HashMap<Histogram, Double> topImages = filterTopKImages();
		// createResponseFile(topImages);
	}
	
	private double[] constructHistogram(BufferedImage img)
	{
		double[] histogram = new double[xn*yn*zn];
		
		WritableRaster imgRaster = img.getRaster();
		for(Location pt : new RasterScanner(img, false))
		{
			int[] rgb = new int[3];
			imgRaster.getPixel(pt.col, pt.row, rgb);
			Color color = new Color(rgb[0], rgb[1], rgb[2]);
			int index = getIndex(color);
			histogram[index]++;
		}
		//printHistogram(histogram);
		return histogram;
	}
	
	private int getIndex(Color color)
	{
		int red = color.getRed();
		int green = color.getGreen();
		int blue = color.getBlue();
		
		int redPrime = getPrime(red, xn, MAX_RGB);
		int greenPrime = getPrime(green, yn, MAX_RGB);
		int bluePrime = getPrime(blue, zn, MAX_RGB);
		
		int index = (redPrime * xn * zn) + (greenPrime * yn) + (bluePrime);
		
		return index;
	}
	
	private int getPrime(int value, int numberOfBins, int maxValue)
	{
		int prime = Math.floorDiv(value * numberOfBins, maxValue);
		return prime;
	}
	
	private void printHistogram(double[] histogram)
	{
		for(int i = 0; i < histogram.length; i++)
		{
			System.out.print(i + ":" + histogram[i] + ", ");
		}
	}
	
	private List<double[]> loadDB()
	{
		LinkedList<double[]> DB = new LinkedList<>();
		// readHeaderData();
		// LoopOverFile
		// addToDB
		return DB;
	}
	
	private HashMap<double[], Double> computeSimilarites(double[] histogram, List<double[]> DB)
	{
		HashMap<double[], Double> histograms = new HashMap<>();
		Matrix histogramVector = createHistogramVector(histogram);
		Matrix similarityMatrix = computeSimilarityMatrix(histogram);
		for(double[] hist : DB)
		{
			Matrix histVector = createHistogramVector(hist);
			double distance = getHistogramDistance(histogramVector, similarityMatrix, histVector);
			histograms.put(hist, distance);
		}
		return histograms;
	}

	private double getHistogramDistance(Matrix histogramVector, Matrix similarityMatrix, Matrix histVector) {
		Matrix vectorDifference = histogramVector.minus(histVector);
		Matrix distanceMatrix = vectorDifference.transpose().mtimes(similarityMatrix).mtimes(vectorDifference);
		double distance = distanceMatrix.getAsDouble(0,0);
		double distanceSqrt = Math.sqrt(distance);
		return distanceSqrt;
	}
	
	private Matrix computeSimilarityMatrix(double[] histogram)
	{
		double maxDistance = -1;
		Matrix similarityMatrix = DenseMatrix.Factory.zeros(histogram.length, histogram.length); 
		for (int i = 0; i < histogram.length; i++)
		{
			for (int j = 0; j < histogram.length; j++)
			{
				Color colorAtI = getCenter(histogram, i);
				Color colorAtJ = getCenter(histogram, i);
				double distance = getColorDistance(colorAtI, colorAtJ);
				
				if (maxDistance < distance)
				{
					maxDistance = distance;
				}

				similarityMatrix.setAsDouble(1 - distance, i, j);
			}
		}
		similarityMatrix.divide(maxDistance);
		return similarityMatrix;
	}
	
	private Color getCenter(double[] histogram, int i)
	{
		int red = i / (yn * zn);
		int green = (i / zn) % yn;
		int blue = i % zn;
		
		int redPrime = getPrime(red, xn, MAX_RGB);
		int greenPrime = getPrime(green, yn, MAX_RGB);
		int bluePrime = getPrime(blue, zn, MAX_RGB);
		
		int redCenter = getCenter(redPrime, xn, MAX_RGB);
		int greenCenter = getCenter(greenPrime, yn, MAX_RGB);
		int blueCenter = getCenter(bluePrime, zn, MAX_RGB);
		
		Color color = new Color(redCenter, greenCenter, blueCenter);
 
		return color;
	}
	
	private int getCenter(int primeValue, int numberOfBins, int maxValue)
	{
		int halfMax = (maxValue / 2);
		int maxValueDividedByBins = maxValue / numberOfBins;
		int halfMaxDividedByBins = halfMax / numberOfBins;
		
		int centerValue = primeValue * maxValueDividedByBins + halfMaxDividedByBins;
		
		return centerValue;
	}
	
	private double getColorDistance(Color color1, Color color2)
	{
		int redDiff = color1.getRed() - color2.getRed();
		int greenDiff = color1.getGreen() - color2.getGreen();
		int blueDiff = color1.getBlue() - color2.getBlue();
		
		double distance = Math.sqrt(redDiff*redDiff + greenDiff*greenDiff + blueDiff*blueDiff);
		return distance;
	}
	
	private Matrix createHistogramVector(double[] histogram)
	{
		Matrix histogramVector = DenseMatrix.Factory.zeros(histogram.length, 1);
		for(int i = 0; i < histogram.length; i++)
		{
			histogramVector.setAsDouble(histogram[i], i);
		}
		return histogramVector;
	}
	
}
