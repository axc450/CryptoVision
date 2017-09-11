public class Print 
{
	public static void printPercent(int p)		//Provides encryption progress output
	{
		System.out.print("\rEncrypting: " + p + "%");	//Output message
		System.out.flush();								//Flush output
	}

	public static void throwError(String msg, Exception e)	//Provides output when an error occurs (with exception message)
	{
		System.out.println(msg);							//Output message
		System.out.println("Error: " + e.getMessage());		//Output message
		System.exit(1);										//Exit Program
	}

	public static void throwError(String msg)	//Provides output when an error occurs (without exception message)
	{
		System.out.println("Error: " + msg);	//Ouput message
		System.exit(1);							//Exit Program
	}
}
