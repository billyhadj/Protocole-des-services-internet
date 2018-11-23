import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Serveur
{
 public static ServerSocket ss = null;
 public static Thread t;
 public static ArrayList bddnom = new ArrayList();
 public static ArrayList bddnum = new ArrayList();
 public static ArrayList bddco = new ArrayList();
 public static ArrayList<Annonce> bddann = new ArrayList<Annonce>();
 public static int NBA=0;
 public static int getNBA()
 {
   return NBA;
 }
 public static void incrNBA()
 {
   NBA++;
 }
 public static ArrayList getBDDNom()
 {
   return bddnom;
 }
 public static ArrayList getBDDNum()
 {
   return bddnum;
 }
 public static ArrayList getBDDCo()
 {
   return bddco;
 }
 public static ArrayList getBDDAnn()
 {
   return bddann;
 }
 public static void main(String[] args)
 {
		try
		{
			ss=new ServerSocket(1027);
			System.out.println("Le serveur est à l'écoute du port "+ss.getLocalPort());
			t=new Thread(new Accepter_connexion(ss));
			t.start();
		}
		catch (IOException e)
		{
			System.err.println("Le port "+ss.getLocalPort()+" est déjà utilisé !");
		}
	}
}
