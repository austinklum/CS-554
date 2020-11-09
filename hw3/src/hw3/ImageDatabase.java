package hw3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import javax.naming.directory.InvalidAttributesException;

import org.ujmp.core.Matrix;

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
	
	public enum ColorModel
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
		xn = (int) Math.pow(2, Integer.parseInt(args[1]));
		yn = (int) Math.pow(2, Integer.parseInt(args[2]));
		zn = (int) Math.pow(2, Integer.parseInt(args[3]));
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
	
	private void runCreate() throws MalformedURLException, IOException
	{
		List<ColorHistogram> histograms = createHistograms();
		writeDB(histograms);
	}

	private List<ColorHistogram> createHistograms() throws FileNotFoundException, MalformedURLException, IOException {
		List<ColorHistogram> histograms = new LinkedList<ColorHistogram>();
		Scanner scan = new Scanner(new File(urlFile));
		while(scan.hasNext())
		{
			String[] urls = new String[3];
			urls[0] = scan.nextLine();
			urls[1] = scan.nextLine();
			urls[2] = scan.nextLine();
			
			ColorHistogram histogram = new ColorHistogram(urls, xn, yn, zn, colorModel);
			histograms.add(histogram);
		}
		scan.close();
		return histograms;
	}
	
	private void writeDB(List<ColorHistogram> histograms) throws IOException
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(dbFile));
		String bins = xn + " " + yn + " " + zn + " " + colorModel.toString() + "\n";
		writer.write(bins);
		for (ColorHistogram histogram : histograms)
		{
			writer.write(histogram.toString() + "\n");
		}
		
		writer.close();
	}
	
	private void runQuery() throws IOException
	{
		 List<ColorHistogram> DB = loadDB();

		 ColorHistogram histogram = new ColorHistogram(queryUrl, xn, yn, zn, colorModel);
		 computeSimilarites(histogram, DB);
		 List<ColorHistogram> topImages = filterTopKImages(DB);
		 createResponseFile(topImages);
	}
	
	private List<ColorHistogram> loadDB()
	{
		LinkedList<ColorHistogram> DB = new LinkedList<>();
		// readHeaderData();
		// LoopOverFile
		// addToDB
		return DB;
	}
	
	private List<ColorHistogram> computeSimilarites(ColorHistogram histogram, List<ColorHistogram> DB)
	{
		Matrix histogramVector = histogram.createHistogramVector();
		Matrix similarityMatrix = histogram.computeSimilarityMatrix();
		for(ColorHistogram hist : DB)
		{
			Matrix histVector = hist.createHistogramVector();
			double distance = ColorHistogram.getHistogramDistance(histogramVector, similarityMatrix, histVector);
			hist.setDistance(distance);
		}
		return DB;
	}
	
	private List<ColorHistogram> filterTopKImages(List<ColorHistogram> histograms)
	{
		List<ColorHistogram> topKImages = histograms.stream()
													.sorted()
													.limit(kNumberOfImages)
													.collect(Collectors.toList());
		return topKImages;
	}
	
	private void createResponseFile(List<ColorHistogram> histograms) throws IOException
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(responseFile));
		
		writer.write(getHeader());
		
		for (ColorHistogram histogram : histograms)
		{
			writer.write(histogram.getHTMLResponse());
		}
		
		writer.write("\t</body>\n");
		writer.write("</html>");
		
		writer.close();
	}
	
	private String getHeader()
	{
		StringBuilder head = new StringBuilder();
		head.append("<!DOCTYPE html>\n");
		head.append("<head>\n");
		head.append("\t<title>Pictures</title>\n");
		head.append("\t<link href=\"style.css\" rel=\"stylesheet\"\n");
		head.append("</head>\n");
		head.append("<body style=\"opacity: 100;\" class=\"vsc-initalized\">\n");
		
		return head.toString();
	}
	
	
}
