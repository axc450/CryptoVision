import javax.imageio.ImageIO;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;

public class CryptoVision	//Main class
{
	private static final String[] exts = {"bmp", "png", "jpg", "jpeg", "gif", "txt"};	//List of valid file extensions
	private static String en_key = "e17ea208d03308b150e6dd9cf8a10387";					//Default XOR key to use
	
	public static void main(String[] args)	//Main method
	{
		if(args.length > 2)		//If too many arguments were given
		{
			Print.throwError("Too many arguments were given!");	//Throw error
		}
		
		if(args.length == 2)	//If custom encryption key was given 
		{
			en_key = args[1];	//Set key
		}
		
		File selectedFile;		//Set up selected file object
		
		if(args.length > 0)		//If file path was given
		{
			selectedFile = fileFromArg(args[0]);	//Load the file from the argument
		}
		else
		{
			selectedFile = chooseFile();			//Load the file from a File Chooser
		}
		
		String currentPath = selectedFile.getParent();	//Set the current path of the file
		String fileName = selectedFile.getName();		//Set the name of the file
		String currentExt = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());	//Set the extension of the file
		
		if(currentExt.equals("txt"))	//If the file is text
		{
			TextEncrypt en_text = new TextEncrypt(en_key);				//Create a text encryptor
			String inputText = importText(selectedFile);				//Import the text from the file
			String outputText = en_text.encrypt(inputText);				//Encrypt the text from the file
			exportText(outputText, currentPath);						//Export the encrypted text
			System.out.println("\nText Encryption Successful!");		//Debug message
		}
		else	//If the file is an image
		{
			ImageEncrypt en_image = new ImageEncrypt(en_key);			//Create an image encryptor
			BufferedImage inputImage = importImage(selectedFile);		//Import the image
			BufferedImage outputImage = en_image.encrypt(inputImage);	//Encrypt the image
			exportImage(outputImage, currentPath);						//Export the encrypted image
			System.out.println("\nImage Encryption Successful!");		//Debug message
		}
		
		System.exit(0);		//Exit program (no errors occurred)
	}
	
	private static File chooseFile()	//Provides file selection via a File Chooser 
	{
		JFileChooser fileChooser = new JFileChooser();								//Create new file chooser
		FileNameExtensionFilter fileExFilter = new FileNameExtensionFilter("Valid Input", exts);	//Set file filter to valid file types
		fileChooser.setAcceptAllFileFilterUsed(false);								//Set chooser to only allow valid file types
		fileChooser.setFileFilter(fileExFilter);									//Set the file filter
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);					//Set the file chooser to only accept files
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir"))); 	//Set the file chooser's default path
		if (!(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION))		//If a non valid option has been chosen
		{
			Print.throwError("A file was not selected!");		//Throw error
		}
		
		return fileChooser.getSelectedFile();	//Return selected file
	}

	private static File fileFromArg(String strPath)	//Provides file selection via a command line argument
	{
		try
		{
			File file = new File(strPath);	//Convert the file path given into a File object
			if(!file.isFile())	//If the file is invalid (a directory etc)
			{
				Print.throwError("The file \"" + strPath + "\" was not found!");	//Throw error
			}
			
			return file;		//Return the file
		}
		catch (Exception e)		//If something went wrong
		{
			Print.throwError("Something went wrong importing your file", e.getMessage());	//Throw error
			return null;
		}
	
	}

	private static BufferedImage importImage(File file)		//Import an image file
	{	
		try
		{
			BufferedImage imageData = ImageIO.read(file);	//Read the image into memory
			return imageData;								//Return the image data
		} 
		catch (Exception e)		//If something went wrong
		{
			Print.throwError("Something went wrong importing your image", e.getMessage());	//Throw error
			return null;
		}
	}
	
	private static String importText(File file)			//Import a text file
	{
		try
		{
			String textData = new String(Files.readAllBytes(file.toPath()));	//Read the text into memory
			return textData;													//Return the text data
		} 
		catch (Exception e)		//If something went wrong
		{
			Print.throwError("Something went wrong importing your text", e.getMessage());	//Throw error
			return null;
		}
	}
	
	private static void exportImage(BufferedImage outputImage, String currentPath)	//Export an image file
	{
		try
		{
			ImageIO.write(outputImage, "png", new File(currentPath + "/output.png"));	//Write out the new image to "output.png"
		}
		catch(Exception e)		//If something went wrong
		{
			Print.throwError("Something went wrong exporting your image", e.getMessage());	//Throw error
		}
	}
	
	private static void exportText(String outputText, String currentPath)	//Export a text file
	{
		try
		{
			Files.write(Paths.get(currentPath + "/output.txt"), outputText.getBytes());	//Write out the new text file to "output.txt"
		}
		catch(Exception e)		//If something went wrong
		{
			Print.throwError("Something went wrong exporting your text", e.getMessage());	//Throw error
		}
	}
}
