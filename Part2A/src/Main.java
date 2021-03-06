import java.io.*;
import java.util.*;


public class Main {
	
	public static class DataItem {
		public String line;
		
		public int xNext;
		public int yNext;
		public int shiftNextOrig;
		public int shiftNext;
	}
	
	public static String shift(String input, int shift) {
		StringBuilder line = new StringBuilder(input);
		for(int i=0; i<line.length(); ++i) {
			char c = line.charAt(i);
			int indexShift = -1;
			if (c >= 'a' && c <= 'z' ) {
				indexShift = 'a';				
			}
			else if (c >= 'A' && c <= 'Z') {
				indexShift = 'A';
			}
			
			if(indexShift>0) {
				int effectiveShift = (c + shift) - indexShift;
				if (effectiveShift<0){
					effectiveShift = (c + shift + 26) - indexShift;
				}
				int newChar = (effectiveShift % 26) + indexShift;
				line.setCharAt(i, (char) (newChar));
			}
		}
		
		return line.toString();
	}
	
	public static void main(String[] args) throws IOException {
				
		//LinkedList<DataItem> data = new LinkedList<DataItem>();
		
		DataItem[][] data = new DataItem[255][255];
		
		// read file
		BufferedReader br = new BufferedReader(new FileReader("input.txt"));
		int firstX = Integer.parseInt(br.readLine());
		int firstY = Integer.parseInt(br.readLine());
		int firstShift = Integer.parseInt(br.readLine()) % 26;
		
		String temp;
		while((temp = br.readLine()) != null) {
			String[] array = temp.split(",");
			
			int x = Integer.parseInt(array[0]);
			int y = Integer.parseInt(array[1]);
			
			if(data[x][y] == null) {
				data[x][y] = new DataItem();
			}
			
			data[x][y].line = array[2];
			data[x][y].xNext = Integer.parseInt(array[3]);
			data[x][y].yNext = Integer.parseInt(array[4]);			
			data[x][y].shiftNextOrig = Integer.parseInt(array[5]);
			data[x][y].shiftNext = Integer.parseInt(array[5]) % 26;
		}
		
		br.close();
		
		// traverse
		StringBuilder result = new StringBuilder();
		DataItem current = data[firstX][firstY];
		int currentShift = firstShift;
		int currentShiftOrig = firstShift;
		
		//int nextShift = -1;
		while(currentShiftOrig!=0) {
			result.append(shift(current.line, currentShift));
			currentShift = current.shiftNext;
			currentShiftOrig = current.shiftNextOrig;
			current = data[current.xNext][current.yNext];
		}
		
		System.out.println(result);
	}
}
