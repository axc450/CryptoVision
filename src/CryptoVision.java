import javax.imageio.ImageIO;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;

public class CryptoVision
{
	private static final String[] exts = {"bmp", "png", "jpg", "jpeg", "gif", "txt"};
	private static String en_key = "e17ea208d03308b150e6dd9cf8a10387";			//XOR key to use
	
	public static void main(String[] args)
	{
		if(args.length > 2)
		{
			throwError("Too many arguments were given!");
		}
		
		if(args.length == 2)
		{
			en_key = args[1];
		}
		
		File selectedFile;
		
		if(args.length > 0)
		{
			selectedFile = fileFromArg(args[0]);
		}
		else
		{
			selectedFile = chooseFile();
		}
		
		String currentPath = selectedFile.getParent();
		String fileName = selectedFile.getName();
		String currentExt = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
		
		if(currentExt.equals("txt"))
		{
			TextEncrypt en_text = new TextEncrypt(en_key);
			String inputText = importText(selectedFile);
			String outputText = en_text.encrypt(inputText);
			exportText(outputText, currentPath);
			System.out.println("\nText Encryption Successful!");			//Debug message
			System.exit(0);												//Exit program
		}
		else
		{
			ImageEncrypt en_image = new ImageEncrypt(en_key);
			BufferedImage inputImage = importImage(selectedFile);
			BufferedImage outputImage = en_image.encrypt(inputImage);		//Encrypt the image
			exportImage(outputImage, currentPath);
			System.out.println("\nImage Encryption Successful!");			//Debug message
			System.exit(0);												//Exit program
		}
	}
	
	private static File chooseFile()
	{
		JFileChooser fileChooser = new JFileChooser();				//Create new file chooser
		FileNameExtensionFilter fileExFilter = new FileNameExtensionFilter("Valid Input", exts);		//Set file filter to valid file types
		fileChooser.setAcceptAllFileFilterUsed(false);				//Set chooser to only allow valid file types
		fileChooser.setFileFilter(fileExFilter);					//Set the file filter
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);	//Set the file chooser to only accept files
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir"))); //Set the file chooser's default path
		if (!(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION))	//If a non valid option has been chosen
		{
			System.exit(0);											//Exit program
		}
		return fileChooser.getSelectedFile();
	}

	private static File fileFromArg(String strPath) 
	{
		try
		{
			File file = new File(strPath);
			if(!file.isFile())
			{
				throwError("The file \"" + strPath + "\" was not found!");
			}
			
			return new File(strPath);
		}
		catch (Exception e)
		{
			throwError("Something went wrong importing your file", e);
			return null;
		}
	
	}

	private static BufferedImage importImage(File file)
	{	
		try
		{
			BufferedImage imageData = ImageIO.read(file);		//Read the image into memory
			return imageData;															//Return this data
		} 
		catch (Exception e)
		{
			throwError("Something went wrong importing your image", e);
			return null;
		}
	}
	
	private static String importText(File file)
	{
		try
		{
			String textData = new String(Files.readAllBytes(file.toPath()));		//Read the image into memory
			return textData;															//Return this data
		} 
		catch (Exception e)
		{
			throwError("Something went wrong importing your text", e);
			return null;
		}
	}
	
	private static void exportImage(BufferedImage outputImage, String currentPath)
	{
		try
		{
			ImageIO.write(outputImage, "png", new File(currentPath + "/output.png"));	//Write out the new image
		}
		catch(Exception e)
		{
			throwError("Something went wrong exporting your image", e);
		}
	}
	
	private static void exportText(String outputText, String currentPath)
	{
		try
		{
			Files.write(Paths.get(currentPath + "/output.txt"), outputText.getBytes());	//Write out the new image
		}
		catch(Exception e)
		{
			throwError("Something went wrong exporting your text", e);
		}
	}
	
	public static void printPercent(int p)
	{
		System.out.print("\rEncrypting: " + p + "%");
		System.out.flush();
	}
	
	public static void throwError(String msg, Exception e)
	{
		System.out.println(msg);											//Debug message
		System.out.println("Error: " + e.getMessage());						//Debug message
		System.exit(1);														//Exit Program
	}
	
	public static void throwError(String msg)
	{
		System.out.println("Error: " + msg);											//Debug message
		System.exit(1);														//Exit Program
	}
}
