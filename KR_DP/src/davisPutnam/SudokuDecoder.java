package davisPutnam;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class SudokuDecoder {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		File file = new File("damnhard.sdk.txt");
		//File file = new File("damnhard.sdk.txt");
		Scanner sc = new Scanner(file);
		FileWriter fileWriter = new FileWriter("newsudoku.txt");
		PrintWriter printWriter = new PrintWriter(fileWriter);
		
		int row = 1; 
		int column = 0;
		String nextclause = sc.nextLine();
		int thrs = nextclause.length();
		System.out.println(thrs);
		while (sc.hasNextLine()){
			int i = 0;
			inner: while (true){
				if (i == thrs) {
					break inner;
				}
				column +=1;
				if (nextclause.charAt(i)!='.') {
					printWriter.printf("%d%d%c 0%n", row,column,nextclause.charAt(i));
				}
				if (column % Math.sqrt(thrs) == 0) {
					column = 0; 
					row += 1;
				}
				i += 1;
			}
			row = 1;
			column = 0;
			nextclause = sc.nextLine();
			printWriter.printf("===%n");
		}
		printWriter.close();
	}

}
