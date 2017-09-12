import java.security.MessageDigest;
import java.util.Random;

public class TextEncrypt 
{	
	
	Random textGenerator;
	
	public TextEncrypt(String key) 
	{
		try
		{
			MessageDigest md5Generator = MessageDigest.getInstance("MD5");				//Create a MD5 generator
			byte[] md5Result = md5Generator.digest(key.getBytes());						//Create the hash based on the key
		
			long randomSeed = md5ToSeed(md5Result);										//Convert the MD5 bytes to a long
			
			textGenerator = new Random();										//Create new random instance
			textGenerator.setSeed(randomSeed);										//Set the random seed to the MD5 long
		}
		catch(Exception e)
	    {
			Print.throwError("Something went wrong setting up the encryptor", e);
	    }
	}

	public String encrypt(String inputText) 
	{
			try
			{
				String encryptedStr = "";
				
	            for (int i = 0; i < inputText.length(); i++) 						//Loop through all pixel columns
	            {
	                    char chr = inputText.charAt(i);								//Get the current pixel color at this location
	                    char newChr = encryptChr(chr);								//Ecrypt the pixel
	                    encryptedStr += newChr;										//Change the input image pixel at this location
	                    Print.printPercent((int)Math.ceil(100 * (i+1)/inputText.length()));	//Print encryption progress
	            }
			
	            return encryptedStr;											//Return the new image
			}
		    catch(Exception e)
		    {
		    	Print.throwError("Something went wrong encrypting your text", e);
				return null;
		    }
	}
	
	private char encryptChr(char chr)
	{
		int randInt = textGenerator.nextInt(100);  
		return (char) ((char) ((int) chr)^(randInt));
	}
	
	private static long md5ToSeed(byte[] md5Result)		//Converts an MD5 hash into a seed
	{
		long a = md5Result[0] * 256 * md5Result[1] + 256 * 256 * md5Result[2] + 256 * 256 * 256 * md5Result[3];
		long b = md5Result[4] * 256 * md5Result[5] + 256 * 256 * md5Result[6] + 256 * 256 * 256 * md5Result[7];
		return a ^ b;
	}
}
