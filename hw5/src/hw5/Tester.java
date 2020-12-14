package hw5;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Tester 
{
	public static void main(String args[])
	{
		//create("test-file.html");
	}
	
	private void create(String input) throws IOException
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(input));
		
		writer.write(getHeader());
//		writer.write(queryHist.getHTMLResponse());
//		for (ColorHistogram histogram : histograms)
//		{
//			writer.write(histogram.getHTMLResponse());
//		}
		
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
	
//	private writeOutTest()
//	{
//		return String.format(
//				"<div class=\"img\">\n"
//					+ "\t<a href=\"%s\" class=\"flickr\"></a>\n"
//					+ "\t<a href=\"%s\">\n"
//						+ "\t\t<img src=\"%s\">\n"
//					+ "\t</a>\n"
//					+ "\t<div class=\"distance\">\n"
//						+ "\t\t\"%f\"\n"
//					+ "\t</div>\n"
//				+ "</div>\n"
//				, urls[0], urls[2], urls[1], this.distance);
//	}
}
