package hangmangames;

import java.io.*;
import java.net.*;
import java.util.*;
public class Sever
{
	private static ServerSocket serverSocket;
	private static final int PORT = 1234;
	public static void main(String[] args)throws IOException
	{
		System.out.println("Opening port¡­\n");
		try
		{
			serverSocket = new ServerSocket(PORT); 
		}
		catch(IOException ioEx)
		{
			System.out.println(
			"Unable to attach to port!");
			System.exit(1);
		}
		do
		{
			Socket link = serverSocket.accept(); //Waiting for client to connect.
			System.out.println("\nNew client accepted.\n"); //Get a new player.
			HandlerClient handler=new HandlerClient(link);//Create a thread to handle communication with
			//this client and pass the constructor for this thread a reference to the relevant socket¡­
			handler.start();   //method calls run.
		}while(true);
	}
}
	class HandlerClient extends Thread
	{
		private Socket client;
		private Scanner in;
		private PrintWriter output;
		String input;
		
		public HandlerClient(Socket socket)
		{
			try
			{	
				client=socket;
			
				in =
				new Scanner(client.getInputStream()); 
				output =
				new PrintWriter(
				client.getOutputStream(),true);  //Set up input and output stream.
			}
			catch(IOException ioEx)
			{
				ioEx.printStackTrace();
			}
		}
		
		public void run()
		{
			String[]words= new String[]{"dog","cat","cool","pink","apple","orange","special",
					"network","programme","never","sense","double","differ","bedbug","hangman"}; 
			// This is the  list of words that server will pick from.
			int point=0;// Initialize player's grades.
				
		do {
			
		
		int picknumber;
		picknumber=(int)(Math.random()*14);
		String pick;
		pick=words[picknumber]; //Pick a random word from list.
		System.out.println(pick); //Show chosen word on the server side.
		int p,q; // The place that input letter in the chosen word. "p" is that letter first appear place
		//and "q" is second appear place.
		String slash=""; //Used to show which letters client hasn't guessed correctly.
		for(int i=0;i<pick.length();i++) 
		{
			slash=slash+"_ ";
		}// Ensure that the number of slashes equals to the number of chosen word's letters
		
		labelb:
		for(int m=0;m<pick.length();m++)
		{	
			output.println("this word has "+pick.length()+" letters  "+slash);
			input=in.nextLine();
			if(input.length()==1) //Make sure that whether client is guessing some specific letter
				//in the chosen word or the whole word.
			{
				char attempt=input.charAt(0); //For there is only one letter in string "input", convert 
				// it into type char as "attempt" which is easy to operate.
				p=pick.indexOf(attempt);
				if(p==-1)
				{
					continue;
					// Chosen word doesn't contain the letter client provide. Jump to another attempt.
				}
				else
				{
					m=m-1; 
					//Chosen word contains the letter client provide. It is a successful attempt, and it
					//doesn't consume allowed failed chance.
					q=pick.indexOf(attempt,p+1);
					//Check is there more than one client provided letter in chosen word. Because there are 
					//only 15 words in list and none of them contains 3 or more same letters, I just need
					//set "p" and "q" to represent place of client provided letter.
					if(q==-1)
					{
						String z="";
						for(int k=0;k<slash.length();k++)
						{	
							if(k!=2*p)
							{
								char l=slash.charAt(k);
								z=z+l;
							}
							else
							{
								z=z+attempt;
							}
							
						}
						slash=z; //Use correct letter to replace slash.
						continue;
					}
					else
					{
						String z="";
						for(int k=0;k<slash.length();k++)
						{	
							if((k!=2*p)&&(k!=2*q))
							{
								char l=slash.charAt(k);
								z=z+l;
							}
							else
							{
								z=z+attempt;
							}
							
						}
						slash=z;//Use 2 correct letters to replace slash.
					}

				}
				
			}
			
			else //This is the situation that client guess total word.
			{
				int f=input.compareTo(pick);
				if(f==0)
				{
					output.println("that's right");
					point=point+2;// As shown below, after every round game, points will decrease by 1.
					//Consequently, when clients get right word, clients will get 2 points here and lose 1 point afterward.
					break labelb;
				}
				else
				{
					continue;
					
				}
			}
		}
		point=point-1; //When clients run out of chance, they lose 1 point.
		output.println("You get "+point+" points now");
		
		}while(!input.equals("QUIT")); // Whenever clients input "QUIT", connection closes.
		try
		{
		if (client!=null)
		{
		System.out.println(
		"Closing down connection¡­");
		client.close();
		}
		}
		catch(IOException ioEx)
		{
		System.out.println("Unable to disconnect!");
		}
			
		}
		
	
	}
