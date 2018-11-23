import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.net.InetAddress;

public class ActionS implements Runnable
{
	private Socket socket = null;
	private BufferedReader in = null;
	private PrintWriter out = null;
	public String nom,dom,texte,ip,port;
	public int num,prix,idmessage;
  private int deco=0;
	public ActionS(Socket s,String pseudo, int n,String p)
  {
		socket=s;
		nom=pseudo;
		num=n;
		port=p;
	}
	public int AjouterAnnonce(ArrayList bdd,String nom,int num,String dom, int prix,String texte,String ip,String port)
	{
		int idmessage=Serveur.getNBA();
		Serveur.incrNBA();
		Annonce a = new Annonce(nom,num,dom,prix,texte,idmessage,ip,port);
		bdd.add(a);
		return idmessage;
	}
	public boolean SupprimerAnnonce(ArrayList bdd,String nom,int num,int id)
	{
		Annonce a;
		int i;
		for(i=0;i<bdd.size();i++)
		{
			a=(Annonce) bdd.get(i);
			if(a.getnom().equals(nom)&&a.getnum()==num&&a.getidmessage()==id)
			{
				bdd.remove(i);
				return true;
			}
		}
		return false;
	}
	public int NbAnnonces(String dom,ArrayList bddann,ArrayList tab)
	{
		int cpt=0,i;
		Annonce a;
		for(i=0;i<bddann.size();i++)
		{
			a=(Annonce) bddann.get(i);
			if(a.getdom().equals(dom))
			{
				cpt++;
				tab.add(i);
			}
		}
		return cpt;
	}
	/*public boolean AnnonceAppartient(String nom,int num,int id,ArrayList bddann)
	{
		int i;
		Annonce a;
		for(i=0;i<bddann.size();i++)
		{
			a=(Annonce) bddann.get(i);
			if(a.getnom().equals(nom)&&a.getnum()==num&&a.getidmessage()==id) return true;
		}
		return false;
	}*/
	public void SeDeconnecte(String nom,int num,ArrayList bddnom,ArrayList bddnum,ArrayList bddco)
	{
		int i;
		for(i=0;i<bddnum.size();i++)
		{
			if(nom.equals(bddnom.get(i))&&num==(int)bddnum.get(i)) bddco.set(i,0);
		}
	}
	public boolean IsCo(String nom,int num,ArrayList bddco,ArrayList bddnom,ArrayList bddnum)
	{
		int i;
		for(i=0;i<bddnum.size();i++)
		{
			if(nom.equals(bddnom.get(i))&&num==(int)bddnum.get(i)&&(int)bddco.get(i)==1) return true;
		}
		return false;
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
      while(deco==0)
      {
        out = new PrintWriter(socket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String ligne;
        ligne=lire(in);
        System.out.println("Le client dit : "+ligne);
        if(((ligne.split("\\."))[0]).equals("BYE")) // si deconnexion
        {
          deco=1;
          System.out.println(nom+num+" vient de se déconnecter");
					SeDeconnecte(nom,num,Serveur.getBDDNom(),Serveur.getBDDNum(),Serveur.getBDDCo());
        }
				else if(((ligne.split("\\."))[0]).equals("PUB")) // si publication
				{
					dom=(ligne.split("\\."))[3];
					prix=Integer.parseInt(String.valueOf((ligne.split("\\."))[4]));
					texte=(((ligne.split("\\["))[1]).split("\\]"))[0];
					if(Authentification.ClientExiste(nom,num,Serveur.getBDDNom(),Serveur.getBDDNum())==false)
					{
						out.print("NON.1#");
						out.flush();
					}
					else if(!(dom.equals("VOI"))&&!(dom.equals("MOT"))&&!(dom.equals("MUS"))&&!(dom.equals("ELE"))&&!(dom.equals("TEL"))&&!(dom.equals("AUT")))
					{
						out.print("NON.2#");
						out.flush();
					}
					else if(prix<0)
					{
						out.print("NON.3#");
						out.flush();
					}
					else
					{
						InetAddress inetadr = InetAddress.getLocalHost();
						ip= (String) inetadr.getHostAddress();
						idmessage=AjouterAnnonce(Serveur.getBDDAnn(),nom,num,dom,prix,texte,ip,port);
						out.print("OUI."+idmessage+"#");
						out.flush();
					}
				}
				else if(((ligne.split("\\."))[0]).equals("REA")) // si lecture Annonce
				{
					int nbAnnonces=0,i,index,con;
					dom=(((ligne.split("\\."))[3]).split("#"))[0];
					ArrayList tab = new ArrayList();
					nbAnnonces=NbAnnonces(dom,Serveur.getBDDAnn(),tab);
					out.print("OUI."+nbAnnonces);
					Annonce a;
					for(i=0;i<nbAnnonces;i++)
					{
						index= (int) tab.get(i);
						a= (Annonce) (Serveur.getBDDAnn()).get(index);
						out.print("."+a.getidmessage()+"."+a.getnom()+"."+a.getnum()+".");
						if(IsCo(a.getnom(),+a.getnum(),Serveur.getBDDCo(),Serveur.getBDDNom(),Serveur.getBDDNum())==true)
						{
							out.print("CON.");
						}
						else out.print("DEC.");
						out.print(a.getIP()+"."+a.getport()+"."+a.getprix()+"."+a.gettexte());
					}
					out.print("#");
					out.flush();
				}
				else if(((ligne.split("\\."))[0]).equals("DEL")) // si suppression Annonce
				{
					int id=Integer.parseInt(String.valueOf((((ligne.split("\\."))[3]).split("#"))[0]));
					boolean app = SupprimerAnnonce(Serveur.getBDDAnn(),nom,num,id);
					if(app)
					{
						out.print("OUI#");
						out.flush();
					}
					else
					{
						out.print("NON.5#");
						out.flush();
					}
				}
      }
    }
    catch (IOException e)
		{
	  	System.err.println("Aucun serveur à l'écoute du port "+socket.getLocalPort());
		}
  }
}
