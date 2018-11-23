import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.ArrayList;


public class Action implements Runnable
{
	private Socket socket = null;
	private BufferedReader in = null;
	private PrintWriter out = null;
	private Scanner sc = null;
	public String nom,dom,texte;
	public int num,prix;
	public Action(Socket s,String pseudo, int n)
  {
		socket=s;
		nom=pseudo;
		num=n;

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
			int deco=0;
			while(deco==0)
			{
				sc = new Scanner(System.in);
				System.out.print("Que voulez vous faire ?\n0 : Publier une annonce\n1 : Lire une annonce\n2 : Supprimer une annonce\n3 : Déconnexion\n");
				do{sc = new Scanner(System.in); rep =sc.nextInt();}
				while(rep<0||rep>3);
				if(rep==0) // publier annonce DOM = voiture, moto, musique, électroménager, téléphone, autre
				{
					System.out.print("Dans quel domaine ?\n0 : voiture\n1 : moto\n2 : musique\n3 : électroménager\n4 : télephone\n5 : autre\n");
					do{sc = new Scanner(System.in); rep =sc.nextInt();}
					while(rep<0||rep>5);
					if(rep==0) dom="VOI";else if(rep==1) dom="MOT";else if(rep==2) dom="MUS";
					else if(rep==3) dom="ELE";else if(rep==4) dom="TEL";else if(rep==5) dom="AUT";
					System.out.println("Quel prix ?");
					do{sc = new Scanner(System.in); prix =sc.nextInt();}
					while(prix<0);
					System.out.println("Saisissez le texte de l'annonce (pas de '[' ni de ']') :");
					sc = new Scanner(System.in); texte=sc.nextLine();
					out.print("PUB."+nom+"."+num+"."+dom+"."+prix+".["+texte+"]#");
					out.flush();
					ligne=lire(in);
					System.out.println("Le serveur répond : " +ligne);

				}
				else if(rep==1) // lire annonce
				{
					System.out.print("Dans quel domaine ?\n0 : voiture\n1 : moto\n2 : musique\n3 : électroménager\n4 : télephone\n5 : autre\n");
					do{sc = new Scanner(System.in); rep =sc.nextInt();}
					while(rep<0||rep>5);
					if(rep==0) dom="VOI";else if(rep==1) dom="MOT";else if(rep==2) dom="MUS";
					else if(rep==3) dom="ELE";else if(rep==4) dom="TEL";else if(rep==5) dom="AUT";
					out.print("REA."+nom+"."+num+"."+dom+"#");
					out.flush();
					ligne=lire(in);
					System.out.println("Le serveur répond : " +ligne);
					/*int nbAnnonces=(in.split("\\."))[1];


					System.out.print("---------------CATEGORIE : "+dom+"---------------\n|\n| ");
					System.out.print("Annonce i : "); */
				}
				else if(rep==2) // supprimer annonce
				{
					System.out.println("Quelle Annonce supprimer ? (idmessage)");
					sc = new Scanner(System.in);
					rep =sc.nextInt();
					out.print("DEL."+nom+"."+num+"."+rep+"#");
					out.flush();
					ligne=lire(in);
					System.out.println("Le serveur répond : " +ligne);
				}
				else if(rep==3) // deconnexion
				{
					out.print("BYE."+nom+"."+num+"#");
					out.flush();
					System.out.println("Aurevoir");
					deco=1;
				}
			}
		}
		catch (IOException e)
		{
	  	System.err.println("Aucun serveur à l'écoute du port "+socket.getLocalPort());
		}
  }
}
