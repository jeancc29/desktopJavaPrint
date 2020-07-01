/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;

import com.mycompany.mavenproject1.clases.MyPrintJobListener;
import com.mycompany.mavenproject1.clases.PrintJobWatcher;
import com.mycompany.mavenproject1.clases.pruebaWebSocket;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.standard.PrinterName;
import javax.print.event.PrintJobListener;

/**
 *
 * @author jean2
 */
public class Main {
    //I believe that by most standards, System.exit() is a not very OOP way of closing 
    //applications, I've always been told that the proper way is to return from main. 
    //This is somewhat a bit of a pain and requires a good design but I do believe 
    //its the "proper" way to exit
    public static boolean close = false;
    public static void print(){
        String code2 = "1B700096FA"; // my code in hex
    FileOutputStream os = null;
    try {
//        os = new FileOutputStream("LPT1:POS-58");
        os = new FileOutputStream("LPPOS58 Printer");
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }
      PrintStream ps = new PrintStream(os);
    ps.print("hola");
      ps.close();
    }
    
    private static boolean feedPrinter(byte[] b) {
    try {       
        AttributeSet attrSet = new HashPrintServiceAttributeSet(new PrinterName("POS58 Printer", null)); //EPSON TM-U220 ReceiptE4

        DocPrintJob job = PrintServiceLookup.lookupPrintServices(null, attrSet)[0].createPrintJob();       
        //PrintServiceLookup.lookupDefaultPrintService().createPrintJob();  

        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        Doc doc = new SimpleDoc(b, flavor, null);
        PrintJobWatcher pjDone = new PrintJobWatcher(job);
//        job.addPrintJobListener(new MyPrintJobListener());

        job.print(doc, null);
        pjDone.waitForDone();
        System.out.println("Done !");
    } catch (javax.print.PrintException pex) {
        System.out.println("Printer Error " + pex.getMessage());
        return false;
    } catch(Exception e) {
        e.printStackTrace();
        return false;
    }
    return true;
}

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException , IOException {
		int port = 8999; // 843 flash policy port
		try {
			port = Integer.parseInt( args[ 0 ] );
		} catch ( Exception ex ) {
		}
		      pruebaWebSocket s = new pruebaWebSocket( port );
		s.start();
		System.out.println( "ChatServer started on port: " + s.getPort() );
                
                
//                print();
//feedPrinter("holaaaa\n\npalomo\n\nesjean\n\n".getBytes());
		BufferedReader sysin = new BufferedReader( new InputStreamReader( System.in ) );
		while ( true ) {
			String in = sysin.readLine();
			s.broadcast( in );
			if( in.equals( "exit" ) ) {
				s.stop(1000);
				break;
			}
                        
                        System.out.println("Main: " + close);
                        if( close == true ) {
				s.stop(1000);
				break;
			}
		}
                
                
                
	}
    
}
