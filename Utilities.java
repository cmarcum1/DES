import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;


public class Utilities {
	
	public static String debugKey ="Start Testing of the DES Encryption Keys:\r\n";
	public static String debugMessage ="Start Testing of the DES Encryption Message:\r\n";
	public static String finalOutputs ="Final Outputs for program\r\n";
	

	public static void createText(String fileName,String info) {
		//Saves the information into a text document
		try {
			File f1 = new File(fileName+".txt");
			f1.createNewFile();
			PrintWriter out = new PrintWriter(f1);
			out.println(info);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String readFile(){
		//Reads the plain text from a file to be Encrypted
		try {
			String info = "";
			File f1 = new File("plaintext.txt");
			Scanner in = new Scanner(f1);
			while(in.hasNext()){
				info += in.nextLine();
			}
			in.close();
			return info;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void printAndDocument(String statement,boolean newLine, String Class){
		//Prints and documents any debugging information that the program has
		if(newLine){
			if(Class.equals("Key")){
				debugKey+=statement +"\r\n";
			}else if(Class.equals("Final")){
				finalOutputs +=statement +"\r\n";
			}else{
				debugMessage +=statement +"\r\n";
			}
		}else{
			if(Class.equals("Key")){
				debugKey+=statement;
			}else if(Class.equals("Final")){
				finalOutputs +=statement;
			}else{
				debugMessage +=statement;
			}
		}
	}
	public static void SaveDebugLog(){
		//Saves any debugging information that the program has to a text file
		File f1;
		File f2;
		File f3;
		int file = 0;
		String [] fileNames = {"Debug Log Key 1","Debug Log Key 2","Debug Log Key 3","Debug Log Key 4","Debug Log Key 5","Debug Log Key 6","Debug Log Key 7","Debug Log Key 8"};
		String [] fileMessageNames = {"Debug Log Message 1","Debug Log Message 2","Debug Log Message 3","Debug Log Message 4","Debug Log Message 5","Debug Log Message 6","Debug Log Message 7","Debug Log Message 8"};
		String [] fileOutputNames = {"Final Output 1","Final Output 2","Final Output 3","Final Output 4","Final Output 5","Final Output 6","Final Output 7","Final Output 8"};
		for(int i=0;i<fileNames.length;i++){
			f1 = new File(fileNames[i]+".txt");
			f2 = new File(fileMessageNames[i]+".txt");
			f3 = new File(fileOutputNames[i]+".txt");
			if(!f1.exists() && !f2.exists() && !f3.exists()){
				file = i;
				break;
			}
		}
		createText(fileNames[file],debugKey);
		createText(fileMessageNames[file],debugMessage);
		createText(fileOutputNames[file],finalOutputs);
		debugKey ="Start Testing of the DES Encryption Keys:\r\n";
		debugMessage ="Start Testing of the DES Encryption Message:\r\n";
		finalOutputs ="Final Outputs for program\r\n";
	}
}
