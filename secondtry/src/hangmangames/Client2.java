package hangmangames;

import java.io.*;
import java.net.*;
import java.util.*;
public class Client2
{
	private static InetAddress host;
	private static final int PORT = 1234;
	public static void main(String[] args)
	{
		try
		{
		host = InetAddress.getLocalHost(); //Get client's own address.
		}
		catch(UnknownHostException uhEx)
		{
		System.out.println("Host ID not found!");
		System.exit(1);
		}
		accessServer();
	}
	private static void accessServer()
	{
		Socket link = null; //Set up a socket for TCP connection with server.
		try
		{
			link = new Socket(host,PORT); //Step 1.
			Scanner input =
			new Scanner(link.getInputStream());//Set up stream for server input.
			PrintWriter output =
			new PrintWriter(
			link.getOutputStream(),true); //Set up stream for keyboard entry¡­
			Scanner userEntry = new Scanner(System.in);
			String message = null, response;
			do
			{
			response = input.nextLine(); 
			System.out.println(response); //Print out message received from server.
			if((response.contentEquals("that's right"))||(response.charAt(0)=='Y'))
			{
				continue; //Win the game and begin a new round.
			}
			System.out.print("Enter message: ");
			message = userEntry.nextLine(); // Send message to server.
			output.println(message); 
		
			}while (!message.equals("QUIT"));//Whenever clients input "QUIT", connection closes.
		}
		catch(IOException ioEx)
		{
			ioEx.printStackTrace();
		}
		finally
		{
			try
			{
				System.out.println(
				"\n* Closing connection¡­ *");
				link.close(); //Step 4.
			}
			catch(IOException ioEx)
			{
				System.out.println(
				"Unable to disconnect!");
				System.exit(1);
			}
		}
	}
}