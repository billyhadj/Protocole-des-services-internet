import java.io.*;
import java.util.Scanner;
import java.net.*;

public class Client
{
	public static Socket socket=null;
	public static Thread t1;
	public static void main(String[] args)
	{
		try
		{
			System.out.println("Demande de connexion...");
			socket = new Socket("localhost",1027);
			t1 = new Thread(new Connexion(socket));
			t1.start();
		}
		catch (UnknownHostException e)
		{
	  	System.err.println("Impossible de se connecter à l'adresse "+socket.getLocalAddress());
		}
		catch (IOException e)
		{
	  	System.err.println("Aucun serveur à l'écoute du port "+socket.getLocalPort());
		}
	}
}
