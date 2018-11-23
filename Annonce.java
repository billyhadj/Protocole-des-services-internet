import java.util.ArrayList;
public class Annonce
{
		public String nom;
    public int num;
    public String dom;
    public int prix;
    public String texte;
		public String ip;
		public String port;

    public int idmessage;

		public Annonce(String nm,int n,String d,int p,String t,int i, String iip,String prt)
    {
			nom=nm;num=n;dom=d;prix=p;texte=t;idmessage=i;ip=iip;port=prt;
		}
    public String getnom(){return nom;}
    public String getdom(){return dom;}
    public String gettexte(){return texte;}
		public String getIP(){return ip;}
    public int getnum(){return num;}
    public int getprix(){return prix;}
		public String getport(){return port;}
    public int getidmessage(){return idmessage;}

}
