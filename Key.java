import java.util.ArrayList;
import java.util.Scanner;


public class Key {

	public static ArrayList<String> Keys = new ArrayList<String>();
	private static int[] binaryKey = new int[64];
	private static int[] permutatedKeyLeft = new int[28];
	private static int[] permutatedKeyRight = new int[28];
	
	public static void KeyProcess(){
		//Does the entire key process for the DES Algorithm
		getKey();
		permutateKey();
		generateKeys();
		printKeys();
	}
	private static void permutateKey() {
		//Declaration of P1 array
		int[] p1 = getP1();
		Utilities.printAndDocument("Permutated Key:", true, "Key");
		//Permutates the Left side of the key
		for(int i=0;i<permutatedKeyLeft.length;i++){
			permutatedKeyLeft[i] = binaryKey[p1[i]-1];
			Utilities.printAndDocument(Integer.toString(permutatedKeyLeft[i]), false, "Key");
		}
		//Permutates the Right side of the key
		for(int i=28;i<56;i++){
			permutatedKeyRight[i-28] = binaryKey[p1[i]-1];
			Utilities.printAndDocument(Integer.toString(permutatedKeyRight[i-28]), false, "Key");
		}
		Utilities.printAndDocument("", true, "Key");
	}
	
	private static int[] getP1(){
		//Grabs the P1 array from the initial permutation of the Key
		int[] p1={57,49,41,33,25,17,9,1,58,50,42,34,26,18,10,2,59,51,43,35,27,
	               19,11,3,60,52,44,36,63,55,47,39,31,23,15,7,62,54,46,38,30,22,
	               14,6,61,53,45,37,29,21,13,5,28,20,12,4};
		return p1;
	}
	
	private static int[] getPC2(){
		//Grabs the P1 array from the permutation of the Key after the left shift
		int[] PC2={14,17,11,24,1,5,3,28,15,6,21,10,23,19,12,4,26,8,16,7,27,20,
				    13,2,41,52,31,37,47,55,30,40,51,45,33,48,44,49,39,56,34,53,
				    46,42,50,36,29,32};
		return PC2;
	}
	
	private static int countOnes(String binary){
		//returns the number of ones in the string
		int counter = 0;
		for(int i=0;i<binary.length();i++){
			if(binary.charAt(i)=='1'){
				counter++;
			}
		}
		return counter;
	}
	
	private static void getKey(){
		//Takes in user input for key
		//for testing purposes I have hard coded the key to ensure accuracy of testing
		System.out.print("\nPlease input key:\n->");
		String key = "";
		Scanner scanner2 = new Scanner(System.in);
		while(key.length()!=8){
			key= scanner2.next();
			if(key.length()!=8){
				System.err.println("The key has to eight characters. Please Try again.");
			}
		}
		Utilities.printAndDocument("Key: "+key+"\n", true, "Key");
		createBinaryKey(key);
	}
	
	private static void createBinaryKey(String stripped){
		Utilities.printAndDocument("----------Binary Key------------", true, "Key");
		//Declaration of variables 
		int num,count;
		int indexCounter = 0;
		String binaryStr;
		//Goes through ever character in key
		Utilities.printAndDocument("Character by Character Converting", true, "Key");
		for(int i=0;i<stripped.length();i++){
			//converts the character into a number
			num = (int) stripped.charAt(i);
			//Converts that number into a binary String
			binaryStr = Integer.toBinaryString(num);
			//Counts the number of 1's on the string
			count = countOnes(binaryStr);
			//If the number is even then it adds a one
			//If the number is odd then it pads it with a zero
			if(count %2 == 0){
				binaryStr += '1'; 
			}else{
				binaryStr += '0'; 
			}
			Utilities.printAndDocument("\tASCII Value: "+Integer.toString(num)+"      Binary: "+binaryStr, true, "Key");
			//Adds the binary string into the binaryKey array (where the entire key is stored)
			for(int z =0;z<8;z++){
				binaryKey[indexCounter] = Character.getNumericValue(binaryStr.charAt(z));
				indexCounter++;
			}
		}	
		//Prints the original key
		Utilities.printAndDocument("Original Key", true, "Key");
		for(int z =0;z<binaryKey.length;z++){
			Utilities.printAndDocument(Integer.toString(binaryKey[z]), false, "Key");
		}
		Utilities.printAndDocument("", true, "Key");
	}
	
	private static void leftShift(int numOfCyclescycle){
		//Declaration of variables 
		//shiftCycles is the number of shifts needed to in ever cycle
		int[] shiftCycles = {1,1,2,2,2,2,2,2,1,2,2,2,2,2,2,1};
		int cycle = 0;
		Utilities.printAndDocument("\r\nLeft Shift of "+Integer.toString(numOfCyclescycle) +" Key:" , true, "Key");
		//Loops until the number of shift cycles is completed
		do{
			//Shifts the left by one bit
			int tempLeft = permutatedKeyLeft[0];
			for(int i=0;i<permutatedKeyLeft.length-1;i++){
				permutatedKeyLeft[i] = permutatedKeyLeft[i + 1];
				
			}
			permutatedKeyLeft[permutatedKeyLeft.length - 1] = tempLeft;
			
			//Shifts the right by one bit
			int tempRight = permutatedKeyRight[0];
			for(int i=0;i<permutatedKeyRight.length-1;i++){
				permutatedKeyRight[i] = permutatedKeyRight[i + 1];
				Utilities.printAndDocument(Integer.toString(permutatedKeyRight[i]), false, "Key");
			}
			permutatedKeyRight[permutatedKeyRight.length - 1] = tempRight;
			cycle++;
		}while(shiftCycles[numOfCyclescycle]!=cycle);
		for(int i=0;i<permutatedKeyLeft.length;i++){
			Utilities.printAndDocument(Integer.toString(permutatedKeyLeft[i]), false, "Key");
		}
		for(int i=0;i<permutatedKeyRight.length;i++){
			Utilities.printAndDocument(Integer.toString(permutatedKeyRight[i]), false, "Key");
		}
		Utilities.printAndDocument("\r\n", false, "Key");
	}
	
	private static void generateKeys(){
		//Declaration of variables
		int[] PC2 = getPC2();
		int [] keyArray = new int[48];
		int [] LRConcat = new int[57];
		String key = "";
		
		//Generates 16 keys
		for(int i=0;i<16;i++){
			//Preforms the left shift of the left and right side
			leftShift(i);
			
			//Copies the left side to an array to a larger array
			for(int z=0;z<permutatedKeyLeft.length;z++){
				LRConcat[z] = permutatedKeyLeft[z];
			}
			//Concatenates the Right side to an array with the left side
			for(int w=28;w<56;w++){
				LRConcat[w] = permutatedKeyRight[w-28];
			}
			//Permutates the key with PC2
			Utilities.printAndDocument("\r\nPermutation of number "+Integer.toString(i)+" key with PC2:", true, "Key");
			for(int q=0;q<PC2.length;q++){
				keyArray[q] = LRConcat[PC2[q]-1];
				Utilities.printAndDocument(Integer.toString(keyArray[q]), false, "Key");
			}
			Utilities.printAndDocument("\r\n", false, "Key");
			//Converts the key to a String
			for(int x=0;x<keyArray.length;x++){
				key += Integer.toString(keyArray[x]);
			}
			//Adds the to the Keys arraylist to be viewed later
			Keys.add(key);
			//Sets the key back to null
			key = "";
		}
	}
	//Prints all permutations of the key
	private static void printKeys(){
		Utilities.printAndDocument("\r\n\r\nFinal Key Permuations", true, "Key");
		for(int i=0;i<Keys.size();i++){
			Utilities.printAndDocument("Key "+Integer.toString(i), true, "Key");
			Utilities.printAndDocument(Keys.get(i), true, "Key");
		}
	}
}
