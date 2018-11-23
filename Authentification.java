import java.net.*;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;

public class Authentification implements Runnable
{
	private Socket socket;
	private PrintWriter out = null;
	private BufferedReader in = null;
	public String ligne;
	public String nom;
	public String port;
	public int num;
	public static Thread t;

	public Authentification(Socket s)
  {
		 socket = s;
	}
	public int AjouterClientBDD(String nom,ArrayList bddnom,ArrayList bddnum,ArrayList bddco)
  {
    int cpt=0;
    int i;
    int taille=bddnum.size();
    for(i=0;i<taille;i++)
    {

      if(nom.equals(bddnom.get(i))) cpt++;
    }
    bddnom.add(nom);
    bddnum.add(cpt);
		bddco.add(0);
    return cpt;
  }
	public static boolean ClientExiste(String nom,int num,ArrayList bddnom,ArrayList bddnum)
	{
		int i;
		for(i=0;i<bddnum.size();i++)
		{
			if(nom.equals(bddnom.get(i))&&num==(int)bddnum.get(i)) return true;
		}
		return false;
	}
	public void SeConnecte(String nom,int num,ArrayList bddnom,ArrayList bddnum,ArrayList bddco)
	{
		int i;
		for(i=0;i<bddnum.size();i++)
		{
			if(nom.equals(bddnom.get(i))&&num==(int)bddnum.get(i)) bddco.set(i,1);
		}
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
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());
			ligne=lire(in);
			System.out.println("Le client dit : "+ligne);
			String[] split=ligne.split("\\.");
			if(split[0].equals("CO1"))
			{
					nom=split[1];
					port=(split[2].split("#"))[0];
					num=AjouterClientBDD(nom,Serveur.getBDDNom(),Serveur.getBDDNum(),Serveur.getBDDCo());
					out.print("RE1."+nom+"."+num+"#");
					out.flush();
					System.out.println("Nouveau client dans la BDD : "+nom+num);
					System.out.println(nom+num+" vient de se connecter");
					SeConnecte(nom,num,Serveur.getBDDNom(),Serveur.getBDDNum(),Serveur.getBDDCo());
					t = new Thread(new ActionS(socket,nom,num,port));
					t.start();
			}
			else if(split[0].equals("CO2"))
			{
					nom=split[1];
					num=Integer.parseInt(String.valueOf(split[2]));
					port=(split[3].split("#"))[0];
					if(ClientExiste(nom,num,Serveur.getBDDNom(),Serveur.getBDDNum())==true)
					{
						out.print("OUI#");
						out.flush();
						System.out.println(nom+num+" vient de se connecter");
						SeConnecte(nom,num,Serveur.getBDDNom(),Serveur.getBDDNum(),Serveur.getBDDCo());
					  t = new Thread(new ActionS(socket,nom,num,port));
						t.start();
					}
					else
					{
						out.print("NON.1#");
						out.flush();
					}
			}
		}
    catch(IOException e)
    {
			System.err.println("ERREUR");
		}
	}
}
