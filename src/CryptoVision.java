import javax.imageio.ImageIO;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.image.BufferedImage;
import java.util.Arrays;

public class CryptoVision
{
	private static final String[] exts = {"bmp", "png", "jpg", "gif"};
	
	private static String currentPath;				//Current path of selected file
	private static String currentExt;				//Current extension of selected file
	private static String fileName;
	private static ImageEncrypt en_image;
	private static String d_key = "e17ea208d03308b150e6dd9cf8a10387";			//XOR key to use
	
	public static void main(String[] args)
	{
		en_image = new ImageEncrypt(d_key);
		
		BufferedImage inputImage;
		if(args.length > 0)
		{
			inputImage = imageFromString(args[0]);
		}
		else
		{
			inputImage = importImage();
		}
		
		BufferedImage outputImage = en_image.encrypt(inputImage);		//Encrypt the image
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
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir"))); //Set the file chooser's default path
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
	
	private static void exportImage(BufferedImage outputImage)
	{
		try
		{
			ImageIO.write(outputImage, "png", new File(currentPath + "/output.png"));	//Write out the new image
		}
		catch(Exception e)
		{
			System.out.println("Something went wrong exporting your image");	//Debug message
			System.out.println("Error: " + e.getMessage());						//Debug message
			System.exit(1);														//Exit program
		}
	}
	
	public static void printPercent(int p)
	{
		System.out.print("\rEncrypting: " + p + "%");
		System.out.flush();
	}
}
