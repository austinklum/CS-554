package hw3;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import org.ujmp.core.DenseMatrix;
import org.ujmp.core.Matrix;

import hw3.ImageDatabase.ColorModel;

import pixeljelly.scanners.Location;
import pixeljelly.scanners.RasterScanner;

public class ColorHistogram implements Comparable
{
	private String[] urls;
	private double[] histogram;
	
	private int xn;
	private int yn;
	private int zn;
	private hw3.ImageDatabase.ColorModel colorModel;
	
	private BufferedImage image;
	
	public static final int MAX_RGB = 256;
	
	private double distance;
	
	public ColorHistogram(String[] urls, int xn, int yn, int zn, ColorModel colorModel) throws MalformedURLException, IOException
	{
		this.urls = urls;
		this.xn = xn;
		this.yn = yn;
		this.zn = zn;
		this.colorModel = colorModel;
		
		image = ImageIO.read(new URL(urls[1]));
		histogram = constructHistogram(image);
	}
	
	public ColorHistogram(String urlQuery, int xn, int yn, int zn, ColorModel colorModel) throws MalformedURLException, IOException
	{
		this(createUrls(urlQuery), xn, yn, zn, colorModel);
	}
	
	public ColorHistogram(String[] urls, int xn, int yn, int zn, ColorModel colorModel, double[] histogram)
	{
		this.urls = urls;
		this.xn = xn;
		this.yn = yn;
		this.zn = zn;
		this.colorModel = colorModel;
		this.histogram = histogram;
	}
	
	private static String[] createUrls(String urlQuery)
	{
		 String[] urls = new String[3];
		 urls[1] = urlQuery;
		 return urls;
	}
	
	public double[] constructHistogram(BufferedImage img)
	{
		double[] histogram = new double[xn*yn*zn];
		
		WritableRaster imgRaster = img.getRaster();
		int totalPixels = 0;
		for(Location pt : new RasterScanner(img, false))
		{
			int[] rgb = new int[3];
			imgRaster.getPixel(pt.col, pt.row, rgb);
			Color color = new Color(rgb[0], rgb[1], rgb[2]);
			int index = getIndex(color);
			histogram[index]++;
			totalPixels++;
		}
		for (int i = 0; i < histogram.length; i++)
		{
			histogram[i] /= totalPixels;
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
	


	public static double getHistogramDistance(Matrix histogramVector, Matrix similarityMatrix, Matrix histVector) {
		Matrix vectorDifference = histogramVector.minus(histVector);
		Matrix distanceMatrix = vectorDifference.transpose().mtimes(similarityMatrix).mtimes(vectorDifference);
		double distance = distanceMatrix.getAsDouble(0,0);
		double distanceSqrt = Math.sqrt(distance);
		return distanceSqrt;
	}
	
	public Matrix computeSimilarityMatrix()
	{
		double maxDistance = -1;
		Matrix similarityMatrix = DenseMatrix.Factory.zeros(histogram.length, histogram.length); 
		for (int i = 0; i < histogram.length; i++)
		{
			for (int j = 0; j < histogram.length; j++)
			{
				Color colorAtI = getCenter(histogram, i);
				Color colorAtJ = getCenter(histogram, j);
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
	
	public Matrix createHistogramVector()
	{
		Matrix histogramVector = DenseMatrix.Factory.zeros(histogram.length, 1);
		for(int i = 0; i < histogram.length - 1; i++)
		{
			double value = histogram[i];
			histogramVector.setAsDouble(value, i, 0);
		}
		return histogramVector;
	}
	
	public double getDistance()
	{
		return distance;
	}
	
	public void setDistance(double distance)
	{
		this.distance = distance;
	}
	
	@Override
	public int compareTo(Object otherHistogram) {
		if (otherHistogram instanceof ColorHistogram)
		{
			return Double.compare(this.distance, ((ColorHistogram) otherHistogram).getDistance());
		}
		return 0;
	}

	public String getHTMLResponse()
	{
		return String.format(
				"<div class=\"img\">\n"
					+ "\t<a href=\"%s\" class=\"flickr\"></a>\n"
					+ "\t<a href=\"%s\">\n"
						+ "\t\t<img src=\"%s\">\n"
					+ "\t</a>\n"
					+ "\t<div class=\"distance\">\n"
						+ "\t\t\"%f\"\n"
					+ "\t</div>\n"
				+ "</div>\n"
				, urls[0], urls[2], urls[1], this.distance);
	}
	
	@Override
	public String toString()
	{
		StringBuilder hist = new StringBuilder();
		hist.append(urls[0] + " ");
		hist.append(urls[1] + " ");
		hist.append(urls[2] + " ");
		
		for(double value : histogram)
		{
			hist.append(value);
			hist.append(" ");
		}
		return hist.toString();
	}
	
}
