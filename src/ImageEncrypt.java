import java.awt.Color;
import java.awt.image.BufferedImage;
import java.security.MessageDigest;
import java.util.Random;

public class ImageEncrypt	//Image Encryptor
{	
	
	Random pixelGenerator;	//Used to generate random (seed-based) pixels
	
	public ImageEncrypt(String key)		//Sets up encryptor
	{
		try
		{
			MessageDigest md5Generator = MessageDigest.getInstance("MD5");		//Create a MD5 generator
			byte[] md5Result = md5Generator.digest(key.getBytes());				//Create hash based on the key
		
			long randomSeed = md5ToSeed(md5Result);								//Convert the MD5 hash to a seed (for use in random generation)
			
			pixelGenerator = new Random();										//Create new random instance
			pixelGenerator.setSeed(randomSeed);									//Set the encryption seed
		}
		catch(Exception e)	//If something went wrong
	    {
			Print.throwError("Something went wrong setting up the encryptor", e.getMessage());	//Throw error
	    }
	}

	public BufferedImage encrypt(BufferedImage inputImage)		//Encrypt an image and return the result
	{
			try
			{
				//Create an empty image used for the encryption result
				BufferedImage encryptedImage = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(), BufferedImage.TYPE_INT_RGB);
				
				for (int y = 0; y < inputImage.getHeight(); y++) 							//Loop through all pixel rows
	        	{
	                for (int x = 0; x < inputImage.getWidth(); x++) 						//Loop through all pixel columns
	                {
	                    Color color = new Color(inputImage.getRGB(x, y));					//Get the current pixel color at this location
	                    Color newColor = encryptColor(color);								//Encrypt the pixel
	                    encryptedImage.setRGB(x, y, newColor.getRGB());						//Set the encrypted pixel in the result
	                }
	                
	                Print.printPercent((int)Math.ceil(100 * (y+1)/inputImage.getHeight()));		//Print encryption progress
	            }
			
			return encryptedImage;											//Return the new image
		}
	    catch(Exception e)	//If something went wrong
	    {
	    	Print.throwError("Something went wrong encrypting your image", e.getMessage());	//Throw error
			return null;
	    }
	}
	
	private Color encryptColor(Color color)		//Encrypt a pixel and return the result
	{
		int colorRED = color.getRed()^pixelGenerator.nextInt(255);			//Create a new RED value based on the XOR of the original RED value and encryption key
        int colorGREEN = color.getGreen()^pixelGenerator.nextInt(255);		//Create a new GREEN value based on the XOR of the original GREEN value and encryption key
        int colorBLUE = color.getBlue()^pixelGenerator.nextInt(255);		//Create a new BLUE value based on the XOR of the original BLUE value and encryption key
        color = new Color(colorRED, colorGREEN, colorBLUE);					//Convert new RGB values into Color object
		return color;
	}
	
	private static long md5ToSeed(byte[] md5Result)		//Converts an MD5 hash into a seed (for use in random generation)
	{
		long a = md5Result[0] * 256 * md5Result[1] + 256 * 256 * md5Result[2] + 256 * 256 * 256 * md5Result[3];
		long b = md5Result[4] * 256 * md5Result[5] + 256 * 256 * md5Result[6] + 256 * 256 * 256 * md5Result[7];
		return a ^ b;
	}
}
