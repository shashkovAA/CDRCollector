package tests;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.file.Files;

public class GeneratorCDR 
{
  
	public static void main(String[] args) throws IOException 
{

    System.out.println("Welcome to Client side");

    Socket fromserver = null;

    

    fromserver = new Socket("localhost",9009);
    

    PrintWriter    out = new PrintWriter(fromserver.getOutputStream(),true);

/*    try (FileInputStream myFile =new FileInputStream("Test_unformatted.log");
	    InputStreamReader inputStreamReader = new InputStreamReader(myFile, "UTF8");
		    BufferedReader reader = new BufferedReader(inputStreamReader,3000);)
    {
		    String line;
		    while ( (line = reader.readLine()) != null )
		    {
			    out.println(line);
			  
		    }
    }
    catch (IOException ioe)
    {
		    ioe.getMessage();
    }*/
 
 out.println("Hello");

 //   out.close();

   // fromserver.close();
} 
}
