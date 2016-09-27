/*
 * Minh Doan
 * Jamal Scott
 */
import java.io.*;
import java.util.*;

public class Vigenere
{

	public static void main(String[] args) throws IOException 
	{
		// TODO Auto-generated method stub
		Scanner input = new Scanner(System.in);
		char c;
		String filename;
		String fileout;
		String keyphrase;
		System.out.print("Encrypt or decrypt a file (e/d): ");
		c = input.nextLine().charAt(0);
		
		//Interpret user input
		if (c == 'e' || c =='E')
			{
			System.out.print("Please enter file to encrypt: ");
			filename = input.nextLine();
			System.out.print("Enter key phrase (no spaces): ");
			keyphrase = input.nextLine();
			System.out.print("Enter output file: ");
			fileout = input.nextLine();
			encrypt(filename,keyphrase,fileout);
			}
		else if (c == 'd' || c == 'D')
			{
			System.out.print("Please enter file to decrypt: ");
			filename = input.nextLine();
			System.out.print("Enter key phrase (no spaces): ");
			keyphrase = input.nextLine();
			System.out.print("Enter output file: ");
			fileout = input.nextLine();
			decrypt(filename,keyphrase,fileout);
			}
		else 
			{
			System.out.println("There's no such option !");
			System.exit(0);
			}

		
	}
	/*
	 * This method encrypts a file given the name, the key, the output file name
	 * and then saved the decrypted text in a file with the given name
	 */
	public static void encrypt(String filename, String keyphrase ,String fileout) throws IOException
	{
		char c;
		int counter = 0;
		BufferedReader in = new BufferedReader(new FileReader(filename));
		PrintWriter out = new PrintWriter(new FileWriter(fileout));
		while( ( c = (char)in.read() ) != (char)-1 )
		{
				//Convert all characters to upper case
				c = Character.toUpperCase(c);
				
				//Determine if the characters read are letters, also keeps new line chars.
				if( c <= 'Z' && c >= 'A' || c == '\r' || c == '\n' )
					{	
					if( c <= 'Z' && c >= 'A')
						{
							//Shifts by the letters of the key
							c = (char)( (c- 'A' + keyphrase.charAt(counter++)- 'A')%26 + 'A');
							
							//Tiles the key
							counter %= keyphrase.length();
						}
					out.print(c);
					}
		}
		//Close file I/O
		in.close();
		out.close();
		
	}
	/*
	 * This method decrypts a file given the it's name, the key, and the output file  name
	 * and saved the decrypted text in the output file.
	 * 
	 */
	public static void decrypt(String filename, String keyphrase ,String fileout) throws IOException
	{
		char c;
		int counter = 0;
		BufferedReader in = new BufferedReader(new FileReader(filename));
		PrintWriter out = new PrintWriter(new FileWriter(fileout));
		
		//Reads in the file
		while( ( c = (char)in.read() ) != (char)-1 )
		{
					if( c <= 'Z' && c >= 'A')
						{
							c = (char)( (c - keyphrase.charAt(counter++) + 26 )%26 + 'A');
							//Tiles the key
							counter %= keyphrase.length();
						}
					out.print(c);
		}
		in.close();
		out.close();
	}

}
