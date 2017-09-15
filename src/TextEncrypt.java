import java.security.MessageDigest;
import java.util.Random;

public class TextEncrypt 	//Text Encryptor
{	
	
	Random textGenerator;	//Used to generate random (seed-based) ASCII characters
	
	public TextEncrypt(String key) 		//Sets up encryptor
	{
		try
		{
			MessageDigest md5Generator = MessageDigest.getInstance("MD5");		//Create a MD5 generator
			byte[] md5Result = md5Generator.digest(key.getBytes());				//Create hash based on the key
		
			long randomSeed = md5ToSeed(md5Result);								//Convert the MD5 hash to a seed (for use in random generation)
			
			textGenerator = new Random();										//Create new random instance
			textGenerator.setSeed(randomSeed);									//Set the encryption seed
		}
		catch(Exception e)	//If something went wrong
	    {
			Print.throwError("Something went wrong setting up the encryptor", e.getMessage());	//Throw error
	    }
	}

	public String encrypt(String inputText) 		//Encrypt a string and return the result
	{
			try
			{
				String encryptedStr = "";		//Create an empty string used for the encryption result
				
	            for (int i = 0; i < inputText.length(); i++) 						//Loop through all characters
	            {
	                    char chr = inputText.charAt(i);								//Get the current character at this location
	                    char newChr = encryptChr(chr);								//Encrypt the character
	                    encryptedStr += newChr;										//Set the encrypted pixel in the result
	                    
	                    Print.printPercent((int)Math.ceil(100 * (i+1)/inputText.length()));		//Print encryption progress
	            }
			
	            return encryptedStr;											//Return the new string
			}
		    catch(Exception e)	//If something went wrong
		    {
		    	Print.throwError("Something went wrong encrypting your text", e.getMessage());	//Throw error
				return null;
		    }
	}
	
	private char encryptChr(char chr)					//Encrypt an ASCII character and return the result
	{
		int randInt = textGenerator.nextInt(100);		//Generate an integer based on the encryption key
		return (char) ((char) ((int) chr)^(randInt));	//Return a new character based on the XOR of the original ASCII value and generated integer
	}
	
	private static long md5ToSeed(byte[] md5Result)		//Converts an MD5 hash into a seed
	{
		long a = md5Result[0] * 256 * md5Result[1] + 256 * 256 * md5Result[2] + 256 * 256 * 256 * md5Result[3];
		long b = md5Result[4] * 256 * md5Result[5] + 256 * 256 * md5Result[6] + 256 * 256 * 256 * md5Result[7];
		return a ^ b;
	}
}
