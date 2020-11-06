package hw3;

import java.io.IOException;

import javax.naming.directory.InvalidAttributesException;

public class ImageDatabase 
{

	public static void main(String[] args)
	{
		try {
			if(args[0].equals("create"))
			{
				//parseCreateArgs(args);
			}
			else if (args[0].equals("query"))
			{
				//parseQueryArgs(args)
			}
			else
			{
				throw new InvalidAttributesException();
			}
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
	
	
}
