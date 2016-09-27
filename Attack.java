/*
 * Minh Doan
 * Jamal Scott
 */
import java.io.*; //for reading and writing file
import java.util.*; //for other stuff

/*
 * This Class reads in an encrypted text file, then analyze it, find the key, and then decrypted the file based on the key.
 */
public class Attack 
{
	public static void main(String[] args) throws IOException 
	{
		Scanner input = new Scanner(System.in);
		double[] allic = new double[15];
		String key = "";
		String filename;
		String fileout;
		char c;
		int keylength=1;
		System.out.print("Please enter file to analyze: ");
		filename = input.nextLine();
		
		ArrayList<Character> list = new ArrayList<Character>();
		BufferedReader in = new BufferedReader(new FileReader(filename));
		
		//Reads in all characters except for spaces, numbers, and special characters.
		while( ( c = (char)in.read() ) != (char)-1 )
		{
				//appends to the end of an array list.
				if (c != '\r' && c != '\n') list.add(c);
				
		}
		
		//Close the I/O stream and appends the appropriate file extension
		in.close();
		fileout = filename +".cracked";
		
		//Calculating and storing IC + finding keylength
		for(int i =0;i<15;i++)
		{
			//Store each of the index of coincidence values
			allic[i] = ICCalculator(list, i+1);
			
			//Calculates and prints the length of the key which is closest to an IC value of 1.73
			if (Math.abs(allic[i]-1.73) < Math.abs(allic[keylength-1] - 1.73)) keylength = i+1;
			System.out.printf("Length: %-7d IC: %.2f\n",i+1,allic[i]);
		}
		System.out.println("The length of the key is: "+keylength);
		
		//Finding key letters
		for(int i =0;i<keylength;i++)
		{
			key += KeyFinder(list,keylength,i);
		}
		
		//Prints the key
		System.out.println("Recovered key:	"+key);
		decrypt(filename,key,fileout);
		System.out.println("Decrypted content written to "+fileout);
		
	}
	/*
	 * This method decrypts a file given the it's name, the key, and the output file and saved the decrypted text in the output file.
	 * 
	 */
	public static void decrypt(String filename, String keyphrase ,String fileout) throws IOException
	{
		char c;
		int counter = 0;
		BufferedReader in = new BufferedReader(new FileReader(filename));
		PrintWriter out = new PrintWriter(new FileWriter(fileout));
		while( ( c = (char)in.read() ) != (char)-1 )
		{
					//Decrypts and writes to a file
					if( c <= 'Z' && c >= 'A')
						{
							c = (char)( (c - keyphrase.charAt(counter++) + 26 )%26 + 'A');
							
							//Tiles the key phrase as necessary
							counter %= keyphrase.length();
						}
					//Writes to the file
					out.print(c);
		}
		//Close I/o Stream
		in.close();
		out.close();
	}
	/*
	 * This method finds the letter at position i of the key given the key length and the sample text
	 * then returns said letter.
	 */
	public static char KeyFinder(ArrayList<Character> list,int keylength,int i){
		char c = ' ';
		double N= 0;
		double smallestx2 = Double.MAX_VALUE;
		
		//The expected freq. of English alphabetic characters
		double[] expectedFreq = {
				0.08167, 0.01492,0.02782,0.04253,0.12702,0.02228,0.02015
				,0.06094,0.06966,0.00153,0.00772,0.04025,0.02406,0.06749,0.07507,0.01929,
				0.00095,0.05987,0.06327,0.09056,0.02758,0.00978,0.0236,0.0015,0.01974,0.00074
				};
		double[] observedFreq = new double[26];
		long[] freq = new long[26];
		
		//count the frequency
		for(int j = i; j < list.size(); j+= keylength)
			{
						freq[(c=list.get(j))-'A']++;
			}
		
		//Determine key length
		for(int j =0; j< 26;j++)
		{
			N += freq[j];
		}
		
		for(int j =0; j< 26;j++)
		{
			observedFreq[j] = freq[j]/N;
		}
		
		for(int j =0; j< 26; j++)
		{
			double x2 = 0;
			for(int k =0;k<26;k++)
			{
				//Calculating chi-squared
				x2 += Math.pow((observedFreq[(j+k)%26] - expectedFreq[k]),2) / expectedFreq[k] ;
			}
			if (x2 <  smallestx2)
			{
				smallestx2 = x2;
				c = (char)('A'+ j );
			}
			
		}
		
		return c;
	}
	
	/*
	 * This method calculates the IC value of a particular key length given the sample text
	 * and returns said IC value.
	 */
	public static double ICCalculator(ArrayList<Character> list,int length)
	{
		long[] letterCount = new long[26];
		double ic = 0;
		char c;
		for(int count = 1; count <= length;count++)
			{
			for(int i = count-1; i < list.size(); i+=length)
				{
							//Totals up the occurrence of each letter in the document
							letterCount[(c=list.get(i))-'A']++;
				}
			//Calls the method which calculates the IC per character
			ic+= ICFormula(letterCount);
			letterCount = new long[26];
			}
		
		return ic/length;
	}
	/*
	 * This method takes in the letter count array then use that to calculate the IC number and returns it.
	 */
	public static double ICFormula(long[] letterCount)
	{	
		double numerator=0;
		double N = 0; 
		double denominator,result;
		for(int i =0; i< 26;i++)
		{
			N += letterCount[i];
		}
		denominator = (N-1)*N/26;
			
			//Calculates the index of coincidence 
			for(int i =0; i< 26;i++)
				{
					 numerator += letterCount[i]*(letterCount[i]-1);
				}
		result = numerator/denominator;
		return result;
	}
}
