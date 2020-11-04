/*** @author Austin Klum*/
package hw3;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.lang.reflect.InvocationTargetException;

import pixeljelly.ops.ConvolutionOp;
import pixeljelly.scanners.Location;
import pixeljelly.scanners.RasterScanner;
import pixeljelly.utilities.Kernel2D;
import pixeljelly.utilities.ReflectivePadder;
import pixeljelly.utilities.SeperableKernel;

public class GaussianOp extends ConvolutionOp implements  BufferedImageOp, pixeljelly.ops.PluggableImageOp
{
	
	private double alpha;
	private double sigma;
	
	public GaussianOp() 
	{
		this(2, 3);
	}
	
	public GaussianOp(double alpha, double sigma) 
	{
		super(createGuassianKernal(alpha,sigma), true, ReflectivePadder.getInstance());
		this.setAlpha(alpha);
		this.setSigma(sigma);
		if (isEdgeCase())
		{
			Kernel2D newKernel = new SeperableKernel(new float[] { 1.0f }, new float[] { 1.0f });
			this.setKernel(newKernel);
		}		
	}

	private boolean isEdgeCase()
	{
		return (this.getKernel().getWidth() == 0 || this.getKernel().getHeight() == 0) ||
			   (this.getKernel().getWidth() == 1 && this.getKernel().getHeight() == 1);
	}
	
	private static Kernel2D createGuassianKernal(double alpha, double sigma)
	{
		int w = (int) Math.ceil(alpha * sigma);
		  float[] result = new float[w * 2 + 1];
	        for (int n = 0; n <= w; n++) {
	            double value = Math.exp(-(n * n) / (2.0f * sigma * sigma)) / (Math.sqrt(6.283185307179586) * sigma);
	            result[result.length / 2 + n] = (float) value;
	            result[result.length / 2 - n] = (float) value;
	        }
	        Kernel2D kernel = new SeperableKernel(result, result);
	        return kernel;
	}
	
	public double getAlpha()
	{
		return alpha;
	}

	public void setAlpha(double alpha)
	{
		this.alpha = alpha;
	}

	public double getSigma() 
	{
		return sigma;
	}

	public void setSigma(double sigma) 
	{
		this.sigma = sigma;
	}

	@Override
	public String getAuthorName()
	{
		return "Austin Klum";
	}

	@Override
	public BufferedImageOp getDefault(BufferedImage arg0) 
	{
		try 
		{
			return this.getClass().getConstructor().newInstance();
		} 
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			System.exit(-1);
		}
		throw new IllegalArgumentException();
	}

}
