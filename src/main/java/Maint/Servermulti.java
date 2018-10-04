/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Maint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.simple.JSONArray;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;

















public class Servermulti {

    int port;
    ServerSocket server=null;
    Socket client=null;
    ExecutorService pool = null;
    int clientcount=0;
    
    public static void main(String[] args) throws IOException {
        Servermulti serverobj=new Servermulti(55000);
        serverobj.startServer();
    }
    
    Servermulti(int port){
        this.port=port;
        pool = Executors.newFixedThreadPool(5);
    }

    public void startServer() throws IOException {
        
        server=new ServerSocket(3000);
        System.out.println("Server Booted");
        
		
        while(true)
        { 
            client=server.accept();
            clientcount++;
            System.out.println("client "+client.toString()+clientcount+" has connected \n obtaining name");
            try {
            
            	ServerThread runnable= new ServerThread(client,clientcount,this);
            	
            	Send send= new Send(client,clientcount,this);
                pool.execute(send);
                pool.execute(runnable);
                
            }catch (Exception e) {
				// TODO: handle exception

            	System.out.println("there is a problem");
            	System.out.println(e.getMessage());
			}
            
            
            
        }
        
    }
    
    private static class Send implements Runnable {
    	ConnectionDb c = new ConnectionDb();
    	Connection conn = c.getConnection();
    	 PrintStream cout;
    	RethinkDB r = c.getR();
    	 Servermulti serverp=null;
         Socket client=null;
         BufferedReader cin;
        
       
         int id;
    	
    	
Send(Socket client, int count ,Servermulti server ) throws IOException {
            
            this.client=client;
            this.serverp=server;
            this.id=count;
           
            cout=new PrintStream(client.getOutputStream());
             }
      
        
        
    	
    	
		//@Override
		public void run() {
			while (true) {
				
			
			System.out.println("snd is running");
		
			// TODO Auto-generated method stub
			Cursor<Object> cursor =r.db("maintennance").table("instruction").changes().run(conn);
			System.out.println(cursor);
			//if(!cursor.toList().isEmpty()) {
				
			
			for (Object inst : cursor) {
				System.out.print("Server : ");
				Isntruction instructio = new Isntruction();
				int firstid= inst.toString().indexOf("id=");
				String blah = inst.toString().substring(firstid+3, firstid+39);
				System.out.println(blah);
				instructio=instructio.findInstructionByID(conn, r, blah);
				System.out.println(instructio.toString());
				
				cout.println(inst.toString());	
				
			//}
			}
			
		}}
    	
    }

    private static class ServerThread implements Runnable {
        String usernames;
        Servermulti serverp=null;
        Socket client=null;
        BufferedReader cin;
        PrintStream cout;
       // Scanner sc=new Scanner(System.in);
        int id;
        String s;
        List<UserClass> list = new ArrayList<UserClass>();
        ConnectionDb c = new ConnectionDb();
     	  Connection conn = c.getConnection();

      	RethinkDB r = c.getR();
        UserClass hih = new UserClass();
        Mac mac=new Mac() ;
        
        ServerThread(Socket client, int count ,Servermulti server ) throws IOException {
            
            this.client=client;
            this.serverp=server;
            this.id=count;
            
            System.out.println("thread has started");
            cin=new BufferedReader(new InputStreamReader(client.getInputStream(),"Cp1252"));
            cout=new PrintStream(client.getOutputStream());
            InputStreamReader blah = new InputStreamReader(client.getInputStream());
            //System.out.println("reading line");
            s=cin.readLine();
           // System.out.println("finding by name");
            System.out.println(s.toString());
            if(s.contains("requested")) {
            		int first =s.indexOf("requested:");
            		int last=s.indexOf(",", first);
            		String idi = s.substring(first+10,last );
            		String results=s.substring(last+1);
            		r.table("instruction").get(idi).update(r.hashMap("result",results)).run(conn);
            }
            if(s.contains("usrname")) {
            	System.out.println("searching for user");
            String username= s.substring(s.indexOf("username:")+9,s.length());
            usernames=username;
            System.out.println("names "+usernames);
            list = hih.findByName(conn, r, username);
            UserClass us = new UserClass();
            //System.out.println("test on list");
            if (list.isEmpty()) {
            	System.out.println("user doesnt't exist \n adding user ");
            	
            	hih.setName(username);
            	 us=hih.addusert(conn, r, hih); 
            	//System.out.println("this is us :"+us.toString());
            	
            }
            else {
            	//System.out.println("else statmnt");
            	hih.setName(username);
            	
            	System.out.println(list);
            	cout.println(list.get(1).getName()+","+list.get(1).getInitialAddresse());
            	
            	
            }
            }
            if(s.substring(0, 3).equals("mac:")) {
            	
            
            //System.out.println("qdding qmc");
           // mac.addMac(conn, r, s);
            System.out.println("getting mac");
            Object sisi =mac.getmac(conn, r, s);
            //System.out.println("sisi"+sisi);
            //list=hih.findByMac(conn, r, s);
           // if (list.isEmpty()) {
            	//System.out.println("user doesnt't exist \n adding user ");
            	//List<String> macs = new ArrayList<String>();
            	//macs.add(s);
            	//hih.setMacs(s);
            	
            	//Object j = hih.adduser(conn, r, hih);
            	//System.out.println(j.toString());
            	//JSONArray tlala = (JSONArray)j;
            	//System.out.println(tlala.get(3));
           // }
            }
            System.out.println("Connection "+s+" established with client "+client);
            
            //System.out.println(blah.getEncoding());
            //System.out.println(blah.read());
        }
      
        
        
        public void run() {
        	
         // int x=1;
         try{
         while(true){
        	 String line ;
        	 StringBuilder sb = new StringBuilder();
        	 s=cin.readLine();
        	 System. out.print("Client("+id+") :"+s+"\n");
        	 PingRes ping = new PingRes();
        	 UserClass user = new UserClass();
        	 ping=ping.toping(s, user);
        	 System.out.println(ping.toString());
       	  	System.out.println("usernames"+usernames);
        	 ping.addping(usernames, ping.getAverage(), ping.getLoss(), ping.getType(), conn, r);
          	  
       
       	System.out.println("connection has been made");

        
            BufferedReader stdin=new BufferedReader(new InputStreamReader(System.in));
			
			//int i = 0;
			
		
			//s=stdin.readLine();
		//stdin.readLine().notifyAll();
			
		//	System.out.println(s);
                     /*       s=sc.nextLine();
                        if (s.equalsIgnoreCase("bye"))
                        {
                            cout.println("BYE");
                            x=0;
                            System.out.println("Connection ended by server");
                            break;
                        }*/
			
			
			
		}
		
            
         //       cin.close();
          //      client.close();
		//cout.close();
         //       if(x==0) {
		//	System.out.println( "Server cleaning up." );
		//	System.exit(0); 
            
      //   }
         } 
         catch(IOException ex){
             System.out.println("Error : "+ex);
         }
            
 		
        }
public void sendit (String ipaddrese,Socket client ) throws IOException {
	PrintStream sout=new PrintStream(client.getOutputStream());
	BufferedReader stdin=new BufferedReader(new InputStreamReader(System.in));
	System.out.print("Server : ");
	s=stdin.readLine();
	sout.println(s);
	
	
}

   
    }
    
}
