package com.pgx.java.socket;
import java.io.* ;
import java.net.* ;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.Thread ;

public class  ServeurTCP{
  private ServerSocket server_tcp ;
  public MyServerSocket(int port) throws Exception {
          this.server = new ServerSocket(port);
  }

  class MyThread extends Thread {
           int nb_connect;
           MyThread(int nb_connect) {
               this.nb_connect = nb_connect;
           }
           public void connection(){
             this.nb_connect++;
           }
   }

  private void listen() throws Exception {
        MyThread main_thread = new MyThread();
        while(true){
          String data = null;
          Socket client = this.server.accept();

          String clientAddress = client.getInetAddress().getHostAddress();
          System.out.println("\r\nNew connection from " + clientAddress);
          BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
          while ( (data = in.readLine()) != null ) {
              System.out.println("\r\nMessage from " + clientAddress + ": " + data);
            }

          }
}
