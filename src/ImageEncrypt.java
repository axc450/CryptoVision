import javax.imageio.ImageIO;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Random;

public class ImageEncrypt
{
	private static final String[] exts = {"bmp", "png", "jpg", "gif"};
	
	private static String currentPath;				//Current path of selected file
	private static String currentExt;				//Current extension of selected file
	private static String fileName;
	
	private static String key = "e17ea208d03308b150e6dd9cf8a10387";			//XOR key to use
	
	public static void main(String[] args)
	{
		BufferedImage inputImage;
		if(args.length > 0)
		{
			inputImage = imageFromString(args[0]);
		}
		else
		{
			inputImage = importImage();
		}
		BufferedImage outputImage = encryptXOR(inputImage,key);		//Encrypt the image
		exportImage(outputImage);									//Export the image
		System.out.println("\nImage Encryption Successful!");			//Debug message
		System.exit(0);												//Exit program
	}

	private static BufferedImage imageFromString(String strPath) 
	{
		Path jPath = Paths.get(strPath);
		currentPath = jPath.toFile().getParent();
		fileName = jPath.getFileName().toString();
		currentExt = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
		if(!Arrays.asList(exts).contains(currentExt))
		{
			System.out.println("The selected file was not an image!");
			System.exit(1);
		}
		try
		{
			BufferedImage imageData = ImageIO.read(jPath.toFile());		//Read the image into memory
			return imageData;															//Return this data
		} 
		catch (Exception e)
		{
			System.out.println("Something went wrong importing your image");			//Debug message
			System.out.println("Error: " + e.getMessage());								//Debug message
			System.exit(1);																//Exit program
			return null;
		}
	}

	private static BufferedImage importImage()
	{	
		JFileChooser fileChooser = new JFileChooser();				//Create new file chooser
		FileNameExtensionFilter fileExFilter = new FileNameExtensionFilter("Images", exts);		//Set file filter to images only
		fileChooser.setAcceptAllFileFilterUsed(false);				//Set chooser to only allow images
		fileChooser.setFileFilter(fileExFilter);					//Set the file filter
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);	//Set the file chooser to only accept files
		if (!(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION))	//If a non valid option has been chosen
		{
			System.exit(0);											//Exit program
		}
		
		currentPath = fileChooser.getSelectedFile().getParent();						//Set current path to the folder where the image is stored
		fileName = fileChooser.getSelectedFile().getName();							//Set the filename of the selected file
		currentExt = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());	//Set the extension of the file type

		try
		{
			BufferedImage imageData = ImageIO.read(fileChooser.getSelectedFile());		//Read the image into memory
			return imageData;															//Return this data
		} 
		catch (Exception e)
		{
			System.out.println("Something went wrong importing your image");			//Debug message
			System.out.println("Error: " + e.getMessage());								//Debug message
			System.exit(1);																//Exit program
			return null;
		}
	}
	
	private static BufferedImage encryptXOR(BufferedImage inputImage, String key)
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
	                
	                printPercent((int)Math.ceil(100 * (y+1)/inputImage.getHeight()));
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
	
	private static void exportImage(BufferedImage outputImage)
	{
		try
		{
			ImageIO.write(outputImage, currentExt, new File(currentPath + "/output." + currentExt));	//Write out the new image
		}
		catch(Exception e)
		{
			System.out.println("Something went wrong exporting your image");	//Debug message
			System.out.println("Error: " + e.getMessage());						//Debug message
			System.exit(1);														//Exit program
		}
	}

	private static long md5ToSeed(byte[] md5Result)
	{
		long a = md5Result[0] * 256 * md5Result[1] + 256 * 256 * md5Result[2] + 256 * 256 * 256 * md5Result[3];
		long b = md5Result[4] * 256 * md5Result[5] + 256 * 256 * md5Result[6] + 256 * 256 * 256 * md5Result[7];
		return a ^ b;
	}
	
	private static void printPercent(int p)
	{
		System.out.print("\rEncrypting: " + p + "%");
		System.out.flush();
	}
}
