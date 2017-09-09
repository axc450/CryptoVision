import java.awt.Color;
import java.awt.image.BufferedImage;
import java.security.MessageDigest;
import java.util.Random;

public class ImageEncrypt 
{	
	
	Random pixelGenerator;
	
	public ImageEncrypt(String key) 
	{
		try
		{
			MessageDigest md5Generator = MessageDigest.getInstance("MD5");				//Create a MD5 generator
			byte[] md5Result = md5Generator.digest(key.getBytes());						//Create the hash based on the key
		
			long randomSeed = md5ToSeed(md5Result);										//Convert the MD5 bytes to a long
			
			pixelGenerator = new Random();										//Create new random instance
			pixelGenerator.setSeed(randomSeed);										//Set the random seed to the MD5 long
		}
		catch(Exception e)
	    {
	    	System.out.println("Something went wrong setting up the encryptor");		//Debug message
			System.out.println("Error: " + e.getMessage());							//Debug message
			System.exit(1);															//Exit program
	    }
	}

	public BufferedImage encrypt(BufferedImage inputImage) 
	{
			try
			{
				BufferedImage encryptedImage = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(), BufferedImage.TYPE_INT_RGB);
				
				for (int y = 0; y < inputImage.getHeight(); y++) 							//Loop through all pixel rows
	        	{
	                for (int x = 0; x < inputImage.getWidth(); x++) 						//Loop through all pixel columns
	                {
	                    Color color = new Color(inputImage.getRGB(x, y));					//Get the current pixel color at this location
	                    Color newColor = encryptColor(color);								//Ecrypt the pixel
	                    encryptedImage.setRGB(x, y, newColor.getRGB());						//Change the input image pixel at this location
	                }
	                
	                CryptoVision.printPercent((int)Math.ceil(100 * (y+1)/inputImage.getHeight()));		//Print encryption progress
	            }
			
			return encryptedImage;											//Return the new image
		}
	    catch(Exception e)
	    {
	    	System.out.println("Something went wrong encrypting your image");		//Debug message
			System.out.println("Error: " + e.getMessage());							//Debug message
			System.exit(1);															//Exit program
			return null;
	    }
	}
	
	private Color encryptColor(Color color)
	{
		int colorRED = color.getRed()^pixelGenerator.nextInt(255);			//Create a new RED value based off the XOR of the RED value and MD5 hash
        int colorGREEN = color.getGreen()^pixelGenerator.nextInt(255);		//Create a new GREEN value based off the XOR of the GREEN value and MD5 hash
        int colorBLUE = color.getBlue()^pixelGenerator.nextInt(255);		//Create a new BLUE value based off the XOR of the BLUE value and MD5 hash
        color = new Color(colorRED, colorGREEN, colorBLUE);					//Create the new color
		return color;
	}
	
	private static long md5ToSeed(byte[] md5Result)		//Converts an MD5 hash into a seed
	{
		long a = md5Result[0] * 256 * md5Result[1] + 256 * 256 * md5Result[2] + 256 * 256 * 256 * md5Result[3];
		long b = md5Result[4] * 256 * md5Result[5] + 256 * 256 * md5Result[6] + 256 * 256 * 256 * md5Result[7];
		return a ^ b;
	}
}
