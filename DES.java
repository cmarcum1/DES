import java.util.Scanner;

public class DES {
	
	public static void main(String args[]){
		int choice = 0; 
		System.out.println("Welcome to the DES Encryption Factory");
		Scanner in = new Scanner(System.in);
		do{
			if(choice != 0 ){
				System.out.println("\n\n");
			}
			System.out.println("Please Select what youn would like to do:");
			System.out.println("\t1. Encrypt and Decrypt");
			System.out.println("\t2. Exit with a lollipop");
			choice= in.nextInt();
			switch(choice){
			case 1:
				//Creates the permuation of keys
				Key.KeyProcess();
				//Encrypts and decrypts the message
				Message.EncyptAndDecrypt();
				//Saves all of the debugging information that the program has accumulated 
				Utilities.SaveDebugLog();
				break;
			case 2:
				System.out.println("Please Enjoy your lollipop :)");
				System.exit(0);
				break;
			}
		}while(choice ==1);
		in.close();
	}
}
