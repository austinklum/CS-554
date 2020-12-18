package hw5;

import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

import hw5.Compressor.Model;
import pixeljelly.scanners.Location;
import pixeljelly.scanners.ZigZagScanner;

public class DCTEncoder implements Encoder {
	
	int[][] quantiztion = {
		{16 , 11 , 10 , 16 , 24 , 40 , 51 , 61},
		{12 , 12 , 14 , 19 , 26 , 58 , 60 , 55},
		{14 , 13 , 16 , 24 , 40 , 57 , 69 , 56},
		{14 , 17 , 22 , 29 , 51 , 87 , 80 , 62},
		{18 , 22 , 37 , 56 , 68 , 109 , 103 , 77},
		{24 , 35 , 55 , 64 , 81 , 104 , 113 , 92},
		{49 , 64 , 78 , 87 , 103, 121 , 120 , 101},
		{72 , 92 , 95 , 98 , 112, 100 , 103 , 99}
	};
	@Override
	public void encode(BufferedImage image, Model model, int[] N, File output) throws Exception 
	{
		DataOutputStream out = new DataOutputStream(new FileOutputStream(output));
		writeHeader(image, out);
		double[][] tile = new double[8][8];
	    for (int band = 0; band < image.getSampleModel().getNumBands(); band++) 
	    {
			for(Location pt : new ZigZagScanner(image, 8, 8))
			{
				tile[pt.row % 8 ][pt.col % 8] = image.getRaster().getSample(pt.row, pt.col, pt.band) - 128;
				if(pt.row % 8 == 0 && pt.col % 8 == 0)
				{
					tile = forwardDCT(tile);
					write(out, tile, N);
				}
			}
			
		}
		out.close();
	}

	 private void write(DataOutputStream out, double[][] tile, int[] N) throws IOException
	 {
		quantitze(tile);
		double[] zig = getZigZag(tile);
		for (int i = 0; i < zig.length || i < N[0]; i++) {
			out.writeDouble(zig[0]);
		}
	 }

	private void quantitze(double[][] tile) {
		for (int i = 0; i < tile.length; i++) {
			for (int j = 0; j < tile[i].length; j++) {
				tile[i][j] = Math.floor(tile[i][j] / quantiztion[i][j]);
			}
		}
	}

	public static double[] forwardDCT(double[] data) {
		   final double alpha0 = Math.sqrt(1.0 / data.length);
		   final double alphaN = Math.sqrt(2.0 / data.length);
		   double[] result = new double[data.length];

		   for (int u = 0; u < result.length; u++) {
		     for (int x = 0; x < data.length; x++) {
		       result[u] += data[x]*Math.cos((2*x+1)*u*Math.PI/(2*data.length));
		     }
		     result[u] *= (u == 0 ? alpha0 : alphaN);
		   }
		   return result;
		}


	 public static double[][] forwardDCT(double[][] data)
	 {
		 double[][] result = new double[data.length][data.length];
		 for (int u = 0; u < result.length; u++) {
			result[u] = forwardDCT(data[u]);
		 }
		 
		 double[] column = new double[data.length];
		 for (int v = 0; v < column.length; v++) {
			for (int row = 0; row < data.length; row++) {
				column[row] = result[row][v];
			}
			
			double[] temp = forwardDCT(column);
			for (int row = 0; row < data.length; row++) {
				result[row][v] = temp[row];
			}
		}
		 return result;
	 }
	 
	    public double[] getZigZag(double[][] matrix) {
	  
	        int m = matrix.length;
	        int n = matrix[0].length;
	        double[] result = new double[n * m];
	        int t = 0;

	        for (int i = 0; i < n + m - 1; i++) {
	            if (i % 2 == 1) {
	                // down left
	                int x = i < n ? 0 : i - n + 1;
	                int y = i < n ? i : n - 1;
	                while (x < m && y >= 0) {
	                    result[t++] = matrix[x++][y--];
	                }
	            } else {
	                // up right
	                int x = i < m ? i : m - 1;
	                int y = i < m ? 0 : i - m + 1;
	                while (x >= 0 && y < n) {
	                    result[t++] = matrix[x--][y++];
	                }
	            }
	        }
	        return result;
	    }
	 
	@Override
	public String getMagicWord() 
	{
		return "DCT";
	}
}