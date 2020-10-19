package hw2;

import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.lang.reflect.InvocationTargetException;

import pixeljelly.ops.PluggableImageOp;
import pixeljelly.scanners.Location;
import pixeljelly.scanners.RasterScanner;

public class NullOp implements PluggableImageOp, BufferedImageOp {

	public NullOp() 
	{
	
	}
	
	@Override
	public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel destCM) 
	{
		return new BufferedImage(
				destCM,
				destCM.createCompatibleWritableRaster(src.getWidth(), src.getHeight()),
				destCM.isAlphaPremultiplied(), 
				null);
	}

	@Override
	public BufferedImage filter(BufferedImage src, BufferedImage dest) 
	{
		if (dest == null)
		{
			dest = createCompatibleDestImage(src, src.getColorModel());
		}
		
		WritableRaster srcRaster = src.getRaster();
		WritableRaster destRaster = dest.getRaster();
		
		for (Location pt : new RasterScanner(src, true))
		{
			int sample = srcRaster.getSample(pt.col,pt.row, pt.band);
			destRaster.setSample(pt.col, pt.row, pt.band, sample);
		}
		return dest;
	}

	@Override
	public BufferedImageOp getDefault(BufferedImage src)
	{
		try {
			return this.getClass().getConstructor().newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		throw new IllegalArgumentException();
	}
	
	@Override
	public Rectangle2D getBounds2D(BufferedImage src)
	{
		return src.getRaster().getBounds();
	}

	@Override
	public Point2D getPoint2D(Point2D srcPoint, Point2D destPoint) 
	{
		if (destPoint == null)
		{
			destPoint = (Point2D) srcPoint.clone();
		} 
		else
		{
			destPoint.setLocation(srcPoint);
		}
		return destPoint;
	}

	@Override
	public RenderingHints getRenderingHints() 
	{
		return null;
	}

	@Override
	public String getAuthorName()
	{
		return "Austin Klum";
	}
}
