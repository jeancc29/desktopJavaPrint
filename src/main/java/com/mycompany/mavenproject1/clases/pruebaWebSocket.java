/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1.clases;
import com.mycompany.mavenproject1.Main;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.concurrent.Future;

import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

/**
 *
 * @author jean2
 */
public class pruebaWebSocket extends WebSocketServer {

	public pruebaWebSocket( int port ) throws UnknownHostException {
		super( new InetSocketAddress( port ) );
	}

	public pruebaWebSocket( InetSocketAddress address ) {
		super( address );
	}

	@Override
	public void onOpen( WebSocket conn, ClientHandshake handshake ) {
		conn.send("Welcome to the server!"); //This method sends a message to the new client
		broadcast( "new connection: " + handshake.getResourceDescriptor() ); //This method sends a message to all clients connected
		System.out.println( conn.getRemoteSocketAddress().getAddress().getHostAddress() + " entered the room!" );
	}

	@Override
	public void onClose( WebSocket conn, int code, String reason, boolean remote ) {
		broadcast( conn + " has left the room!" );
		System.out.println( conn + " has left the room!" );
	}

	@Override
	public void onMessage( WebSocket conn, String message ) {
		broadcast( message );
//                System.out.println("Palomooo: " + message);
		System.out.println( conn + ": " + message );
            //Utils.stringToArrayOfObject(message);
            try {
                String[] arreglo = Utils.stringToArrayString(message);
                String data = "";
                boolean cutPaper = false;
                boolean closeApp = false;
                for (int c = 1; c < arreglo.length; c++) {
                    //Utils.feedPrinter(arreglo[0], arreglo[c].getBytes());
                    if(arreglo[c].equals("CUT_PAPER"))
                        cutPaper = true;
                    else if(arreglo[c].equals("CLOSE_APP"))
                        closeApp = true;
                    else
                        data+= arreglo[c];
                    
                }
                System.out.println("Cut paper: " + cutPaper);
                System.out.println("Close app: " + closeApp);
                if(data.isEmpty() == false){
                    Future<Boolean> future = Utils.feedPrinterFuture(arreglo[0], data.getBytes());
                    while(!future.isDone()) {
                        System.out.println("Waite while printing...");
                        Thread.sleep(300);
                    }
                    Boolean result = future.get();
                    //Utils.feedPrinter(arreglo[0], data.getBytes());
                }
                if(cutPaper){
//                    String cut = "\\x1d\\x56\\x00";
//                    cut+= "\\x1b\\x69";
                    byte[] cut1 = {(byte)0x1d, (byte)0x56, (byte)0x00};
                    Future<Boolean> future = Utils.feedPrinterFuture(arreglo[0], cut1);
                    while(!future.isDone()) {
                        System.out.println("Waite while printing cut...");
                        Thread.sleep(300);
                    }
                    Boolean result = future.get();
                    
                    //byte[] cut1 = {(byte)0x1d, (byte)0x56, (byte)0x00};
                    //Utils.feedPrinter(arreglo[0], cut1);
                }
                broadcast("true");
                
                if(closeApp){
                    System.exit(0);
                }
//                    Main.close = true;
            } catch (Exception e) {
                broadcast("false");
            }
	}
	@Override
	public void onMessage( WebSocket conn, ByteBuffer message ) {
		broadcast( message.array() );
		System.out.println( conn + ": " + message );
	}


//	public static void main( String[] args ) throws InterruptedException , IOException {
//		int port = 8887; // 843 flash policy port
//		try {
//			port = Integer.parseInt( args[ 0 ] );
//		} catch ( Exception ex ) {
//		}
//		ChatServer s = new ChatServer( port );
//		s.start();
//		System.out.println( "ChatServer started on port: " + s.getPort() );
//
//		BufferedReader sysin = new BufferedReader( new InputStreamReader( System.in ) );
//		while ( true ) {
//			String in = sysin.readLine();
//			s.broadcast( in );
//			if( in.equals( "exit" ) ) {
//				s.stop(1000);
//				break;
//			}
//		}
//	}
	@Override
	public void onError( WebSocket conn, Exception ex ) {
		ex.printStackTrace();
		if( conn != null ) {
			// some errors like port binding failed may not be assignable to a specific websocket
		}
	}

	@Override
	public void onStart() {
		System.out.println("Server started!");
		setConnectionLostTimeout(0);
		setConnectionLostTimeout(100);
	}
}