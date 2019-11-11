package com.ubs.socket;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class SocketClientModel implements Runnable{
	
	private File inputFile = null;
	private static int port = 7077;
	
	public SocketClientModel(File inputFile) {
		this.inputFile = inputFile;		
	}

	@Override
	public void run() {
		DataOutputStream dOutputStream = null;
		Socket socket = null;
		Scanner sc = null;		
		//get the localhost IP address, if server is running on some other IP, you need to use that
		InetAddress host = null;
		
		try{			
			host = InetAddress.getLocalHost();
			sc = new Scanner(inputFile);
			
			while(sc.hasNextLine()){
				//establish socket connection to server
				socket = new Socket(host, port);				
				
				//Read Input from file and write to socket using DataOutputStream
				dOutputStream = new DataOutputStream(socket.getOutputStream());
				dOutputStream.writeUTF(sc.nextLine());
				
				Thread.sleep(300);
			}
			socket = new Socket(host, port);
			dOutputStream = new DataOutputStream(socket.getOutputStream());
			dOutputStream.writeUTF("exit");
			
			//close resources
			sc.close();
			socket.close();
			dOutputStream.close();
		}
		catch(UnknownHostException unknownHostException){
			unknownHostException.printStackTrace();
		}
		catch (IOException ioException) {
			ioException.printStackTrace();
		} catch (InterruptedException interruptedException) {			
			interruptedException.printStackTrace();
		}	
	}
}
