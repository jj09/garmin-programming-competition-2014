import java.io.*;
import java.util.*;


public class Main {
	
	public static class Pair {
		public int x;
		public int y;

		public Pair(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
	}
	
	public static class DataItem implements Comparable<DataItem> {
		public String line;		
		
		public int x;
		public int y;
		
		public LinkedList<Pair> next = new LinkedList<Pair>();
		
		public int shiftNext;
		public int shiftNextOrig;

		@Override
		public int compareTo(DataItem o) {
			//DataItem di = (DataItem)o;
			if (o.x == x && o.y == y) return 0;
			else return 1;
		}
	}
	
	public static class Result {
		public Result (String t, int s) {
			steps = s;
			text = t;
		}
		public int steps;
		public String text;
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
	
	public static DataItem[][] data = new DataItem[255][255];
	
	public static int maxX = 0;
	public static int maxY = 0;
	
	public static void main(String[] args) throws IOException {
				
		//LinkedList<DataItem> data = new LinkedList<DataItem>();
		
		
		
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
			
			if(x>maxX) maxX = x;
			if(y>maxY) maxY = y;
			
			if(data[x][y] == null) {
				data[x][y] = new DataItem();
			}
			
			data[x][y].x = x;
			data[x][y].y = y;
			data[x][y].line = array[2];
			
			int xPrev = x + Integer.parseInt(array[3]);
			int yPrev = y + Integer.parseInt(array[4]);
			
			data[x][y].shiftNextOrig = Integer.parseInt(array[5]);
			data[x][y].shiftNext = Integer.parseInt(array[5]) % 26;
			
			if(xPrev>254 || yPrev>254 || xPrev<0 || yPrev<0) {
				continue;
			}
			
			if(data[xPrev][yPrev] == null) {
				data[xPrev][yPrev] = new DataItem();
			}
			
			data[xPrev][yPrev].next.add(new Pair(x,y));
			
			
		}
		
		br.close();
		
		// traverse
		LinkedList<Result> results = new LinkedList<Result>();
		
		Set<DataItem> visited = new TreeSet<DataItem>();
		
		DiscoverPath("", data[firstX][firstY], results, firstShift, 0, visited);
		
		Result result = results.get(0);
		for(Result r : results) {
			if(r.steps<result.steps) {
				result = r;
			}
		}
		System.out.println(result.text);
	}
	
	public static void DiscoverPath(String result, DataItem node, LinkedList<Result> results, int shift, int step, Set<DataItem> visited) {
		result += shift(node.line, shift);
		if (node.shiftNextOrig == 0) {
			results.add(new Result(result, step));
		} else {
			for(int i=0; i<node.next.size(); ++i) {
				if(node.next.get(i).x>maxX || node.next.get(i).y>maxY
						|| node.next.get(i).x<0 || node.next.get(i).y<0) {
					continue;
				}
				DataItem next = data[node.next.get(i).x][node.next.get(i).y];
				if (!(next.x == node.x && node.y == next.y) && !visited.contains(node)) {
					int stepsX = next.x > node.x ? next.x - node.x : node.x - next.x;
					int stepsY = next.y > node.y ? next.y - node.y : node.y - next.y;
					Set<DataItem> s = new TreeSet<DataItem>();
					for(DataItem di : visited) {
						s.add(di);
					}
					s.add(node);
					DiscoverPath(result, next, results, node.shiftNext, step + stepsX + stepsY, s);
				}
			}
		}
	}
}
