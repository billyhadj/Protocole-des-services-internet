import java.net.*;
import java.util.Scanner;
import java.io.*;

public class Connexion implements Runnable
{
	private Socket socket = null;
	public static Thread t2;
	public String nom;
	public int num;
	private PrintWriter out = null;
	private BufferedReader in = null;
	private Scanner sc = null;

	public Connexion(Socket s)
  {
		socket=s;
	}
	public String lire(BufferedReader in)
	{
		String s="";
		try
		{
			char c;
			do
			{
				c=(char) in.read();
				s+=c;
			}
			while(c!='#');
		}
		catch (IOException e)
		{
	  	System.err.println("erreur");
		}
		return s;
	}
	public void run()
  {
    try
    {
      out = new PrintWriter(socket.getOutputStream());
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String ligne;
			int rep;
			System.out.println("0 : Inscription ; 1 : Connexion");
			do{sc = new Scanner(System.in); rep =sc.nextInt();}
			while(rep!=0&&rep!=1);
			sc = new Scanner(System.in);
			if(rep==0) // si 1ere connexion
			{
				System.out.println("Entrez votre prenom :");
				nom= sc.nextLine();
				out.print("CO1."+nom+"."+socket.getLocalPort()+"#");
				out.flush();
				ligne=lire(in);
				System.out.println("Le serveur répond : " +ligne);
				nom=(ligne.split("\\."))[1];
				num=Character.getNumericValue(((((ligne.split("\\."))[2]).split("#"))[0]).charAt(0));
				System.out.println("Bienvenue, votre pseudo est : "+nom+num);
				t2 = new Thread(new Action(socket,nom,num));
				t2.start();
			}
			else if(rep==1) // si n_ième connexion
			{
				System.out.println("Entrez votre pseudo :");
				nom= sc.nextLine();
				num=Character.getNumericValue(nom.charAt(nom.length()-1));
				nom=(new StringBuilder(nom)).deleteCharAt(nom.length()-1).toString();
				out.print("CO2."+nom+"."+num+"."+socket.getLocalPort()+"#");
				out.flush();
				ligne=lire(in);
				System.out.println("Le serveur répond : " +ligne);
				if(ligne.equals("OUI#"))
				{
					t2 = new Thread(new Action(socket,nom,num));
					t2.start();
				}
			}
    }
    catch (IOException e)
		{
	  	System.err.println("Erreur Connexion");
		}

	}
}
