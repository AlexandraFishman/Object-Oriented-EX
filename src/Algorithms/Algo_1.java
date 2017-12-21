package Algorithms;

import Global.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class Algo_1 {

	/** First Algorithm that takes 3 coordinations and returns one new coordinate that closest to them all
	 * @param mac	MAC contains 6 pairs of hexadecimal numbers and separated by ':'  
	 * @return	Full_Coordinate object
	 * @see Full_Coordinate
	 * @author Alona+Alex
	 */
	public static Full_Coordinate algorithm_1(String mac) {
		//////////// sort By MAC: /////////////////////////////
		List<CSV_Merged_Row> rowMergeMACList = new ArrayList<CSV_Merged_Row>();
		List<CSV_Merged_Row> rowMergeList = toCSVtoKML.getRowMergeList();
		rowMergeMACList = rowMergeList.stream().filter(r ->  r.compareByMAC(mac)).collect(Collectors.toList());
		String buffer = "";
		Full_Coordinate[]C = new Full_Coordinate[rowMergeMACList.size()];
		int indexSig = 0;
		int[]a = new int[rowMergeMACList.size()];
		for (int i = 0; i < rowMergeMACList.size(); i++) {
			if(rowMergeMACList.get(i).compareByMAC(mac)){
				String alt = rowMergeMACList.get(i).getAlt();
				String lat = rowMergeMACList.get(i).getLat();
				String lon = rowMergeMACList.get(i).getLon();
				String sig = rowMergeMACList.get(i).getMacSignal(mac);

				a[indexSig] = Integer.parseInt(sig);
				buffer = lat+","+lon+","+alt+","+sig;
				Full_Coordinate temp = new Full_Coordinate(buffer);
				C[indexSig++] = temp; 

			}
		}
		///////SET 3 coordinates by the biggest signals///////
		Arrays.sort(a);

		double sig1=0, sig2=0, sig3=0;
		int i=a.length-1;
		if(i>=0) sig1 = a[i];
		else sig1 = -120;
		if(i-1>=0) sig2 = a[i-1];
		else sig2 = -120;
		if(i-2>=0) sig3 = a[i-2];
		else sig3 = -120;

		Full_Coordinate coo1 = new Full_Coordinate();
		Full_Coordinate coo2 = new Full_Coordinate();
		Full_Coordinate coo3 = new Full_Coordinate();
		for(int j=0; j<C.length; j++) {
			if(C[j] != null) {
				if(C[j].getSignal() == sig1 && coo1.getAlt()==1) {
					coo1 = C[j];
				}
				else if(C[j].getSignal() == sig2 && coo2.getAlt()==1) {
					coo2 = C[j];
				}
				else if(C[j].getSignal() == sig3 && coo3.getAlt()==1) {
					coo3 = C[j];
				}
			}
		}

		/////////////////////the MAC_Algo/////////////////////
		Full_Coordinate final_coo = new Full_Coordinate();
		double signal_weight_1 = 1/(Math.pow(coo1.getSignal(),2));
		double signal_weight_2 = 1/(Math.pow(coo2.getSignal(),2));
		double signal_weight_3 = 1/(Math.pow(coo3.getSignal(),2));

		double wLat1 = signal_weight_1*coo1.getLat();
		double wLat2 = signal_weight_2*coo2.getLat();
		double wLat3 = signal_weight_3*coo3.getLat();

		double wLon1 = signal_weight_1*coo1.getLon();
		double wLon2 = signal_weight_2*coo2.getLon();
		double wLon3 = signal_weight_3*coo3.getLon();

		double wAlt1 = signal_weight_1*coo1.getAlt();
		double wAlt2 = signal_weight_2*coo2.getAlt();
		double wAlt3 = signal_weight_3*coo3.getAlt();

		final_coo.setSignal(signal_weight_1+signal_weight_2+signal_weight_3);
		final_coo.setLat((wLat1+wLat2+wLat3)/final_coo.getSignal());
		final_coo.setLon((wLon1+wLon2+wLon3)/final_coo.getSignal());
		final_coo.setAlt((wAlt1+wAlt2+wAlt3)/final_coo.getSignal());
		//////////////////////////////////////////////////////
		return final_coo;
	}

	/**@param filename a string  that represents a file path
	 *@return creates new file in chosen location, this file contains all MAC's and coordinates
	 */
	public static void runAlgo1(String filename) {
		String line=null;
		try {
			PrintWriter pw = new PrintWriter(new File("C:/ex0/ex2/out/MAC_Algo1.csv"));
			FileReader fr = new FileReader(filename);
			BufferedReader bf = new BufferedReader(fr);
			StringBuilder sb = new StringBuilder();
			line = bf.readLine();
			line = bf.readLine();
			sb.append("MAC");
			sb.append(',');
			sb.append("Alt");
			sb.append(',');
			sb.append("Lat");
			sb.append(',');
			sb.append("Lon");
			sb.append('\n');
			while(line != null) {
				String [] strs = line.split(",");
				for(int i=6; i<strs.length; i=i+4) {
					Full_Coordinate fc = algorithm_1(strs[i]);
					sb.append(strs[i]);
					sb.append(',');
					sb.append(fc.getAlt());
					sb.append(',');
					sb.append(fc.getLat());
					sb.append(',');
					sb.append(fc.getLon());
					sb.append('\n');
				}
				line = bf.readLine();
			}
			fr.close();
			bf.close();
			pw.write(sb.toString());
			pw.close();
		}
		catch (IOException e){e.printStackTrace();}

		System.out.println("Done running Algo1 --> Location: C:/ex0/ex2/out/");
	}

	public static void main(String[] args) {
		toCSVtoKML.run();
		String filename = "C:/ex0/ex2/RESULT_Merged_test_Algo1.csv";
		runAlgo1(filename);
//		String mac = "bc:ae:c5:c3:7f:89";
//		System.out.println("\n"+algorithm_1(mac));
	}

}