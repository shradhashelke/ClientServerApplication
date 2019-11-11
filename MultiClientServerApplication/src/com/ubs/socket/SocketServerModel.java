package com.ubs.socket;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map.Entry;
import java.util.TreeMap;

public class SocketServerModel {

	//ServerSocket and port for the communication between the client and the server
	private static ServerSocket server;    
	private static int port = 7077;

	public static void main(String[] args) throws IOException {

		Socket socket = null;
		DataInputStream dInputStream = null;
		TreeMap<Integer, String> result = new TreeMap<>();
		BufferedWriter out = new BufferedWriter(new FileWriter(new File("Result.txt")));

		//create the socket server object		
		server = new ServerSocket(port);	

		File file_clientModel_evenSeqNo = new File("Client1.txt");    	
		SocketClientModel clientModel_evenSeqNo = new SocketClientModel(file_clientModel_evenSeqNo);
		Thread client1 = new Thread(clientModel_evenSeqNo);
		client1.start();

		File file_clientModel_oddSeqNo = new File("Client2.txt");
		SocketClientModel clientModel_oddSeqNo = new SocketClientModel(file_clientModel_oddSeqNo);
		Thread client2 = new Thread(clientModel_oddSeqNo);
		client2.start();

		while(true){
			try {				
				//Establishes connection and waits for the client
				socket = server.accept();
				
				//read from socket to DataInputStream object
				dInputStream = new DataInputStream(socket.getInputStream());
				String message = dInputStream.readUTF();
				
				//terminate the server if client sends exit request
				if(message.startsWith("exit")){ 					
					break;
				}
				System.out.println(message);
				//Store result in sequence
				result.put(Integer.parseInt(message.split(":")[0]), message.split(":")[1]);				
				
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
		
		//Store result in file - Result.txt
		for (Entry<Integer, String> entry : result.entrySet())              
			out.write(entry.getKey() + " " + entry.getValue() + "\n");
		out.flush();

		//close resources		
		out.close();
		dInputStream.close();
		socket.close();
		server.close();

	}
}
