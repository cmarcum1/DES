import java.util.Arrays;

public class Message {

	private static int[] binaryMessage;
	private static int[] permutatedMessageLeft = new int[32];
	private static int[] permutatedMessageRight = new int[32];
	public static String encryptedMessage = "";
	
	public static void EncyptAndDecrypt(){
		//Gets message
		System.out.println("Encrypting now...");
		String stripped = getMessage();
		//Creates Binary representation of message
		createBinaryMessage(stripped);
		Utilities.printAndDocument("\r\n~#~#~#~#~#~#~#~#~#~##~#~#~#~~#~#~Encrypting Message~#~#~#~#~#~#~#~#~#~#~#~#~#~~#~#~", true, "Message");
		for(int i=0;i<binaryMessage.length/64;i++){
			//Encrypts each block
			permutateMessage(i+1);
			Encode(true);
		}
		//Prints the Encrypted
		System.out.println("Encrypted Message: " + encryptedMessage);
		Utilities.printAndDocument("\r\nEncrypted Message: " + encryptedMessage, true, "Message");
		Utilities.printAndDocument("\r\nEncrypted Message: " + encryptedMessage, true, "Final");
		//Then sets the Encrypted message to be decrypted 
		System.out.println("Decypting now...");
		Utilities.printAndDocument("\r\n~#~#~#~#~#~#~#~#~#~##~#~#~#~~#~#~Decrypting Message~#~#~#~#~#~#~#~#~#~#~#~#~#~#~#~#~\r\n", true, "Message");
		String decypt = encryptedMessage;
		clearMem();
		createBinaryMessage(decypt);
		for(int i=0;i<binaryMessage.length/64;i++){
			permutateMessage(i+1);
			Encode(false);
		}
		System.out.println("Decrypted Message: " + encryptedMessage);
		Utilities.printAndDocument("\r\nDecrypted Message: " + encryptedMessage, true, "Message");
		Utilities.printAndDocument("\r\nDecrypted Message: " + encryptedMessage, true, "Final");
		encryptedMessage = "";
		Key.Keys.clear();
	}
	
	private static String getMessage(){
		//Creates message, strips characters,
		String info = Utilities.readFile();
		System.out.println("Orginial Text: "+info);
		String stripped = stripChars(info);
		System.out.println("Stripped Text: "+stripped);
		int size= stripped.length();
		while(size % 8 != 0){
			size++;
		}
		Utilities.printAndDocument("Message to encrypt: "+info, true, "Message");
		Utilities.printAndDocument("Stripped (of all special characters) message: "+stripped, true, "Message");
		binaryMessage = new int[size*8];
		return stripped;
	}

	
	private static void permutateMessage(int num){
		//Decarlation of variables
		int [] IP =getIP();
		int start = (num -1)*64;
		Utilities.printAndDocument("\r\nPermutated Message #"+num+": ", true, "Message");
		//Permutate left side of message (and prints it)
			for(int i=0;i<permutatedMessageLeft.length;i++){
				permutatedMessageLeft[i] = binaryMessage[((IP[i]-1)+start)];
				Utilities.printAndDocument(Integer.toString(permutatedMessageLeft[i]), false, "Message");
			}
			//Permutate left side of message (and prints it)
			for(int i=permutatedMessageLeft.length;i<IP.length;i++){
				permutatedMessageRight[i-permutatedMessageLeft.length] = binaryMessage[((IP[i]-1)+start)];
				Utilities.printAndDocument(Integer.toString(permutatedMessageRight[i-permutatedMessageLeft.length]), false, "Message");
			}
			Utilities.printAndDocument("\r\nEnd of Permutated Message # "+Integer.toString(num), true, "Message");
	}
	
	private static String stripChars(String file){
		//Removes special characters and whitespaces from the original message
		String stripped = "";
		char[] invaildChars = {' ','!','@','#','$','%','^','&','*','(',')','-','_','+','=','{','}','[',']','\"','\'',';',',','<','>','?',':','|','.'};
		for(int i = 0;i<file.length();i++){
			boolean isSpecial = false;
			for(int z = 0;z<invaildChars.length;z++){
				if(file.charAt(i)==invaildChars[z]){
					isSpecial = true;
				}
			}
			if(!isSpecial){
				stripped += file.charAt(i);
			}
		}
		return stripped;
	}
	
	private static void createBinaryMessage(String stripped){
		Utilities.printAndDocument("\r\n----------Binary Message------------\n", true, "Message");
		//Declaration of variables
		int num;
		Integer[] binary = new Integer[8];
		String binaryStr ="";
		int iHolder=0;
		int indexCounter = 0;
		int [] padding = {0,0,0,0,0,0,0,0};
		Utilities.printAndDocument("\tCharacter by Character Converting\r\n", true, "Message");
		for(int i=0;i<stripped.length();i++){
			//Takes each character and takes to binary
			num = (int) stripped.charAt(i);
			binaryStr = Integer.toBinaryString(num);
			//Pads binary to 8 bits
			while(binaryStr.length() != 8 ){
				binaryStr = "0"+binaryStr;
			}
			//Changing binaryStr to int []
			for(int z=0;z<binaryStr.length();z++){
				binary[z] = Character.getNumericValue(binaryStr.charAt(z));
			}
			//Add 8 bit binary to binary message
			for(int d = 0;d<8;d++){
				binaryMessage[indexCounter] = binary[d];
				indexCounter++;
			}
			Utilities.printAndDocument("\tASCII Value: "+Integer.toString(num)+"      Binary: "+binaryStr, true, "Message");
			iHolder = i;
		}
		iHolder++;
		//Adds Padding to end of message if the size is not a factor of 8
		
		while(iHolder %8!=0){
			for(int d = 0;d<8;d++){
				binaryMessage[indexCounter] = padding[d];
				indexCounter++;
			}
			Utilities.printAndDocument("\tAdding Padding", true, "Message");
			iHolder++;
		}
		//Prints the original message
		Utilities.printAndDocument("\r\nMessage: ", true, "Message");
		for(int z =0;z<binaryMessage.length;z++){
			Utilities.printAndDocument(Integer.toString(binaryMessage[z]), false, "Message");
		}
		Utilities.printAndDocument("\r\n", false, "Message");
		
	}
	private static void EncodeCycle(String Key,int cycle){
		//Declaration of int arrays
		int [] ETable = getETable();
		int [] tempLeft = new int[32];
		int [] functionAnswer = new int[48];
		int [] sBoxPermutated;
		int [] expandedRigth = new int[48];
		
		//Moving the left side into a temp array
		for(int i = 0;i<permutatedMessageLeft.length;i++){
			tempLeft[i] = permutatedMessageLeft[i];
		}
		//Moving the Right side into the left side
		Utilities.printAndDocument("\r\nMoving Right side into Left side: (Left side displaying):", true, "Message");
		for(int i = 0;i<permutatedMessageLeft.length;i++){
			permutatedMessageLeft[i] = permutatedMessageRight[i];
			Utilities.printAndDocument(Integer.toString(permutatedMessageLeft[i]), false, "Message");
		}
		Utilities.printAndDocument("\r\nExpanding right side with E Bit Selection Table:", true, "Message");
		//Expands with E Bit Selection Table
		for(int i = 0;i<expandedRigth.length;i++){
			int a= permutatedMessageRight[ETable[i]-1];
			expandedRigth[i] = a;
			Utilities.printAndDocument(Integer.toString(expandedRigth[i]), false, "Message");
		}
		
		Utilities.printAndDocument("\r\nXORing Expansion & Key:", true, "Message");
		//XOR Expansion & Key 
		for(int i = 0;i<expandedRigth.length;i++){
			functionAnswer[i] =  Integer.parseInt(Character.toString(Key.charAt(i))) ^ expandedRigth[i];
			Utilities.printAndDocument(Integer.toString(functionAnswer[i]), false, "Message");
		}
		Utilities.printAndDocument("\r\n", false, "Message");
		
		//SBox Look up and permuation 
		sBoxPermutated = SBoxes(functionAnswer);
		
		Utilities.printAndDocument("\r\nXORing Left side and sBox Permutated:", true, "Message");
		//Sets the Right side to XOR of Left side and sBox Permutated
		for(int i = 0;i<sBoxPermutated.length;i++){
			permutatedMessageRight[i] = tempLeft[i] ^ sBoxPermutated[i];
			Utilities.printAndDocument(Integer.toString(permutatedMessageRight[i]), false, "Message");
		}
		Utilities.printAndDocument("\r\nEnd of Cycle", true, "Message");
	}
	
	private static void Encode(boolean encypt){
		//Initailation of variables
		int [] encrypted = new int[64];
		int [] IPInverse = getIPInverse();
		int [] RightLeft  = new int [64];
		int asciiValue;
		//Sends block thru EncodeCycle 16 times
		
		if(encypt){
			for(int i = 0;i<16;i++){
				Utilities.printAndDocument("\r\nEncoding Cycle: "+Integer.toString(i), true, "Message");
				EncodeCycle(Key.Keys.get(i),i);
			}
		}else{
			for(int i = 15;i>-1;i--){
				Utilities.printAndDocument("\r\nDencoding Cycle: "+Integer.toString(15-i), true, "Message");
				EncodeCycle(Key.Keys.get(i),i);
			}
		}
		//Dumps the Right side of the permutated message into RightLeft
		for(int i = 0;i<permutatedMessageRight.length;i++){
			RightLeft[i] = permutatedMessageRight[i];
		}
		//Dumps the left side of the permutated message into RightLeft
		for(int i=0;i<permutatedMessageLeft.length;i++){
			RightLeft[i+permutatedMessageRight.length] = permutatedMessageLeft[i];
		}
		//Permutates RightLeft with the IPInverse array
		Utilities.printAndDocument("\r\nStart Permutates of RightLeft with the IPInverse array:", true, "Message");
		for(int i=0;i<encrypted.length;i++){
			encrypted[i] =  RightLeft[IPInverse[i]-1];
			Utilities.printAndDocument(Integer.toString(encrypted[i]), false, "Message");
		}
		Utilities.printAndDocument("\r\nEnd Permutates of RightLeft with the IPInverse array\r\n", true, "Message");
		//Converts binary value of encrypted array to ascii (characters)
		//Encryption is finished
		int counter = 0;
		String binaryHolder = "";
		for(int i = 0;i<8;i++){
			for(int z = 0;z<8;z++){
				binaryHolder += Integer.toString(encrypted[counter]);
				counter++;
			}
			asciiValue = Integer.parseInt(binaryHolder, 2);
			Utilities.printAndDocument("\tASCII value: "+Integer.toString(asciiValue) +"\tCharacter: "+ String.valueOf((char)asciiValue)+ "\tBinary: "+binaryHolder, true, "Message");
			encryptedMessage +=String.valueOf((char)asciiValue);
			binaryHolder = "";
		}
		
		//Encryption is finished
	}

	private static int[] findRowsAndCol(int[] sixBit){
		//Grabs the first and last bit for row, then grabs the middle 4 numbers for col
		//returns those values for SBox look up
		int [] rc = new int[2];
		String row = Integer.toString(sixBit[0])+Integer.toString(sixBit[5]);
		String col = Integer.toString(sixBit[1])+Integer.toString(sixBit[2]) +Integer.toString(sixBit[3])+Integer.toString(sixBit[4]);
		rc[0] = Integer.parseInt(row, 2);
		rc[1] = Integer.parseInt(col, 2);
		return rc;
	}
	
	private static int[] SBoxes(int [] function){
		//Declaration of int arrays
		int [] sixbit = new int[6];
		int [] rc;
		int[][] SBox;
		int S1Value;
		int [] sBoxForEncryption = new int[32];
		int [] sBoxPermutated = new int[32];
		int [] P = getP();
		String bin;
		int counter = 0;
		//Looks up each index of XOR Expansion & Key and finds the variable of the SBOX
		Utilities.printAndDocument("\r\nStarting S Box Permuation", true, "Message");
		for(int i = 0;i<8;i++){
			//Grabs the appropriate SBOX
			SBox = getSBox(i+1);
			//Copies the next 6 bits form XOR Expansion & Key into sixbit array
			System.arraycopy(function, i*6, sixbit, 0, sixbit.length);
			//finds rows and cols for the Sbox
			rc = findRowsAndCol(sixbit);
			Utilities.printAndDocument("\tS Box Row and Column values: "+Integer.toString(rc[0]) +" & "+Integer.toString(rc[1]), true, "Message");
			//Grabs the SBox Value
			S1Value = SBox [rc[0]][rc[1]];
			//Converts the SBox Value into binary
			bin = Integer.toBinaryString(S1Value);
			//Pads to 4 bits
			while(bin.length() != 4){
				bin = "0"+bin;
			}
			Utilities.printAndDocument("\t\tS Box value: "+ Integer.toString(S1Value) +"      Binary: "+bin, true, "Message");
			//Adds the binary version of SBox into sBoxForEncryption
			for(int z = 0;z<bin.length();z++){
				sBoxForEncryption[counter] = Integer.parseInt(Character.toString(bin.charAt(z)));
				counter++;
			}
		}
		Utilities.printAndDocument("\r\nPermuating sBoxForEncryption (binary values) with the P array:", true, "Message");
		//Permuates sBoxForEncryption (binary values) with the P array 
		for(int z = 0;z<P.length;z++){
			int b = sBoxForEncryption[P[z]-1];
			sBoxPermutated[z] = b;
			Utilities.printAndDocument(Integer.toString(sBoxPermutated[z]),false,"Message");
		}
		Utilities.printAndDocument("", true, "Message");
		return sBoxPermutated;	
	}
	private static void clearMem(){
		//Sets all global back to null
		Arrays.fill(binaryMessage, 0);
		Arrays.fill(permutatedMessageLeft,0);
		Arrays.fill(permutatedMessageRight,0);
		
		encryptedMessage = "";
	}
	
	private static int[] getIPInverse(){
		//Gets IPInverse array (for use after Right and Left are put together (in very end))
		int [] IPInverse = {40,8,48,16,56,24,64,32,39,7,47,15,55,23,63,31,38,6,46,14,54,22,62,30,37,5,45,13,53,21,61,29,
				36,4,44,12,52,20,60,28,35,3,43,11,51,19,59,27,34,2,42,10,50,18,58,26,33,1,41,9,49,17,57,25};
		return IPInverse;
	}
	
	private static int[][] getSBox(int index){
		//Gets each SBox 
		if(index == 1){
			int[][] S1= new int[][]{{14,4,13,1,2,15,11,8,3,10,6,12,5,9,0,7},
					{0,15,7,4,14,2,13,1,10,6,12,11,9,5,3,8},
					{4,1,14,8,13,6,2,11,15,12,9,7,3,10,5,0},
					{15,12,8,2,4,9,1,7,5,11,3,14,10,0,6,13}};
			return S1;
		}else if(index == 2){
			int[][] S2= new int[][]{{15,1,8,14,6,11,3,4,9,7,2,13,12,0,5,10},
					{3,13,4,7,15,2,8,14,12,0,1,10,6,9,11,5},
					{0,14,7,11,10,4,13,1,5,8,12,6,9,3,2,15},
					{13,8,10,1,3,15,4,2,11,6,7,12,0,5,14,9}};
			return S2;
		}else if(index == 3){
			int[][] S3= new int[][]{{10,0,9,14,6,3,15,5,1,13,12,7,11,4,2,8},
					{13,7,0,9,3,4,6,10,2,8,5,14,12,11,15,1},
					{13,6,4,9,8,15,3,0,11,1,2,12,5,10,14,7},
					{1,10,13,0,6,9,8,7,4,15,14,3,11,5,2, 12}};
			return S3;
		}else if(index == 4){
			int[][] S4= new int[][]{{7,13,14,3,0,6,9,10,1,2,8,5,11,12,4,15},
					{13,8,11,5,6,15,0,3,4,7,2,12,1,10,14,9},
					{10,6,9,0,12,11,7,13,15,1,3,14,5,2,8,4},
					{3,15,0,6,10,1,13,8,9,4,5,11,12,7,2,14}};
			return S4;
		}else if(index == 5){
			int[][] S5= new int[][]{{2,12,4,1,7,10,11,6,8,5,3,15,13,0,14,9},
					{14,11,2,12,4,7,13,1,5,0,15,10,3,9,8,6},
					{4,2,1,11,10,13,7,8,15,9,12,5,6,3,0,14},
					{11,8,12,7,1,14,2,13,6,15,0,9,10,4,5,3}};
			return S5;
		}else if(index == 6){
			int[][] S6= new int[][]{{12,1,10,15,9,2,6,8,0,13,3,4,14,7,5,11},
					{10,15,4,2,7,12,9,5,6,1,13,14,0,11,3,8},
					{9,14,15,5,2,8,12,3,7,0,4,10,1,13,11,6},
					{4,3,2,12,9,5,15,10,11,14,1,7,6,0,8,13}};
			return S6;
		}else if(index == 7){
			int[][] S7= new int[][]{{4,11,2,14,15,0,8,13,3,12,9,7,5,10,6,1},
					{13,0,11,7,4,9,1,10,14,3,5,12,2,15,8,6},
					{1,4,11,13,12,3,7,14,10,15,6,8,0,5,9,2},
					{6,11,13,8,1,4,10,7,9,5,0,15,14,2,3,12}};
			return S7;
		}else if(index == 8){
			int[][] S8= new int[][]{{13,2,8,4,6,15,11,1,10,9,3,14,5,0,12,7},
					{1,15,13,8,10,3,7,4,12,5,6,11,0,14,9,2},
					{7,11,4,1,9,12,14,2,0,6,10,13,15,3,5,8},
					{2,1,14,7,4,10,8,13,15,12,9,0,3,5,6,11}};
			return S8;
		}else{
			return null;
		}
	}
	
	private static int [] getETable(){
		//Gets E Table
		int [] ETable = {32,1,2,3,4,5, 4,5,6,7,8,9,8,9,10,11,12,13,12,13,14,15,16,17,16,
				17,18,19,20,21,20,21,22,23,24,25,24,25,26,27,28,29,28,29,30,31,32,1};
		return ETable;
	}
	 
	private static int[] getP(){
		//Gets P array (for use after S Box)
		int [] P = {16,7,20,21,29,12,28,17,1,15,23,26,5,18,31,10,2,8,24,14,32,27,3,9,19,13,30,6,22,11,4,25};
		return P;
	}
	
	private static int[] getIP(){
		//Gets IP value array (for initial permutation of the message)
		int[] IP={58,50,42,34,26,18,10,2,60,52,44,36,28,20,12,4,62,54,46,38,30,22,14,6,
				64,56,48,40,32,24,16,8,57,49,41,33,25,17,9,1,59,51,43,35,27,19,11,3,
				61,53,45,37,29,21,13,5,63,55,47,39,31,23,15,7};
		return IP;
	}
}
