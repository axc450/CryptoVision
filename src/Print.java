public class Print	//Provides program output methods
{
	public static void printPercent(int p)		//Provides encryption progress output
	{
		System.out.print("\rEncrypting: " + p + "%");	//Output message
		System.out.flush();								//Flush output
	}

	public static void throwError(String msg, String err)	//Provides output when an error occurs (with exception message)
	{
		System.out.println(msg);							//Output message
		System.out.println("Error: " + err);				//Output message
		System.exit(1);										//Exit Program
	}

	public static void throwError(String msg)	//Provides output when an error occurs (without exception message)
	{
		System.out.println("Error: " + msg);	//Output message
		System.exit(1);							//Exit Program
	}
}
