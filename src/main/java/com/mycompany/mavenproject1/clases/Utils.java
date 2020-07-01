/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1.clases;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.standard.PrinterName;

/**
 *
 * @author jean2
 */
public class Utils {
    
    public static String[] stringToArrayString(String string){
        if(string.isEmpty())
             return null;
        
        String[] array = string.split(",");
        
        return array;
    }
    
    public static Object[] stringToArrayOfObject(String string){
        if(string.isEmpty())
             return null;
        
        String[] array = string.split(",");
        Object[] arr = new Object[array.length];
        
        for(int c = 0; c < array.length ; c++){
            if(array[c].indexOf("{") != -1 && array[c].indexOf("}") != -1){
                arr[c] = stringToMap(array[c]);
            }
            else{
                arr[c] = array[c];
            }
        }
        for(Object object : arr){
                System.out.println(object);
            }
        return arr;
    }
    
    public static Map<String, String> stringToMap(String value){
        value = value.substring(1, value.length()-1);           //remove curly brackets
        String[] keyValuePairs = value.split(",");              //split the string to creat key-value pairs
        Map<String,String> map = new HashMap<>();               

        for(String pair : keyValuePairs)                        //iterate over the pairs
        {
            String[] entry = pair.split("=");                   //split the pairs to get key and value 
            map.put(entry[0].trim(), entry[1].trim());          //add them to the hashmap and trim whitespaces
        }
        return map;
    }
    
    public static boolean feedPrinter(String printer, byte[] b) {
    try {       
        AttributeSet attrSet = new HashPrintServiceAttributeSet(new PrinterName(printer, null)); //EPSON TM-U220 ReceiptE4
        DocPrintJob job = PrintServiceLookup.lookupPrintServices(null, attrSet)[0].createPrintJob();       
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
            Doc doc = new SimpleDoc(b, flavor, null);
            PrintJobWatcher pjDone = new PrintJobWatcher(job);
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
    
    //Aqui empieza la prueba
     public static Future<Boolean> feedPrinterFuture(String printer, byte[] b) {
         ExecutorService executor 
      = Executors.newSingleThreadExecutor();
    try {       
        AttributeSet attrSet = new HashPrintServiceAttributeSet(new PrinterName(printer, null)); //EPSON TM-U220 ReceiptE4
        DocPrintJob job = PrintServiceLookup.lookupPrintServices(null, attrSet)[0].createPrintJob();       
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
            Doc doc = new SimpleDoc(b, flavor, null);
            PrintJobWatcher pjDone = new PrintJobWatcher(job);
            job.print(doc, null);
            
            
       
            return executor.submit(() -> {
             pjDone.waitForDone();
             return true;
        });
        
    } catch (javax.print.PrintException pex) {
        System.out.println("Printer Error " + pex.getMessage());
//        return  Boolean.valueOf(false);
        return executor.submit(() -> {
             return false;
        });
    } catch(Exception e) {
        e.printStackTrace();
//        return false;
        return executor.submit(() -> {
             return false;
        });
    }
    
}
    
}
