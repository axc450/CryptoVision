import java.awt.Color;
import java.awt.image.BufferedImage;
import java.security.MessageDigest;
import java.util.Random;

public class ImageEncrypt 
{
	private String key;
	
	public ImageEncrypt(String key) 
	{
		this.key = key;
	}

	public BufferedImage encrypt(BufferedImage inputImage) 
	{
			try
			{
				MessageDigest md5Generator = MessageDigest.getInstance("MD5");				//Create a MD5 generator
				byte[] md5Result = md5Generator.digest(key.getBytes());						//Create the hash based on the key
			
				long randomSeed = md5ToSeed(md5Result);										//Convert the MD5 bytes to a long
				
				Random randomGenerator = new Random();										//Create new random instance
				randomGenerator.setSeed(randomSeed);										//Set the random seed to the MD5 long
				
				for (int y = 0; y < inputImage.getHeight(); y++) 							//Loop through all pixel rows
	        	{
	                for (int x = 0; x < inputImage.getWidth(); x++) 						//Loop through all pixel columns
	                {
	                    Color color = new Color(inputImage.getRGB(x, y));					//Get the current pixel color at this location
	                    int colorRED = color.getRed()^randomGenerator.nextInt(255);			//Create a new RED value based off the XOR of the RED value and MD5 hash
	                    int colorGREEN = color.getGreen()^randomGenerator.nextInt(255);		//Create a new GREEN value based off the XOR of the GREEN value and MD5 hash
	                    int colorBLUE = color.getBlue()^randomGenerator.nextInt(255);		//Create a new BLUE value based off the XOR of the BLUE value and MD5 hash
	                    Color newColor = new Color(colorRED, colorGREEN, colorBLUE);		//Create the new color
	                    inputImage.setRGB(x, y, newColor.getRGB());							//Change the input image pixel at this location
	                }
	                
	                CryptoVision.printPercent((int)Math.ceil(100 * (y+1)/inputImage.getHeight()));
	            }
			
			return inputImage;														//Return the new image
		}
	    catch(Exception e)
	    {
	    	System.out.println("Something went wrong encrypting your image");		//Debug message
			System.out.println("Error: " + e.getMessage());							//Debug message
			System.exit(1);															//Exit program
			return null;
	    }
	}
	
	private static long md5ToSeed(byte[] md5Result)
	{
		long a = md5Result[0] * 256 * md5Result[1] + 256 * 256 * md5Result[2] + 256 * 256 * 256 * md5Result[3];
		long b = md5Result[4] * 256 * md5Result[5] + 256 * 256 * md5Result[6] + 256 * 256 * 256 * md5Result[7];
		return a ^ b;
	}
}
