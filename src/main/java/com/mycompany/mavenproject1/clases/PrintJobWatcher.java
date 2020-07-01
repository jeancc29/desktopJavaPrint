/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1.clases;
import javax.print.DocPrintJob;
import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobEvent;

/**
 *
 * @author jean2
 */
public class PrintJobWatcher {
    boolean done = false;

  public PrintJobWatcher(DocPrintJob job) {
    job.addPrintJobListener(new PrintJobAdapter() {
      public void printJobCanceled(PrintJobEvent pje) {
        allDone();
      }
      public void printJobCompleted(PrintJobEvent pje) {
        allDone();
      }
      public void printJobFailed(PrintJobEvent pje) {
        allDone();
      }
      public void printJobNoMoreEvents(PrintJobEvent pje) {
        allDone();
      }
      void allDone() {
        synchronized (PrintJobWatcher.this) {
          done = true;
          System.out.println("Printing document is done ...");
          PrintJobWatcher.this.notify();
        }
      }
    });
  }
  public synchronized void waitForDone() {
    try {
      while (!done) {
          System.out.println("Waiting...!");
        wait();
      }
      System.out.println("Done true...!");
    } catch (InterruptedException e) {
    }
  }
  
//  public boolean waitForDone() {
//    
//    
//    boolean ret;
//    synchronized (this) {
//        try {
//        while (!done) {
//        System.out.println("Waiting !");
//          wait();
//        }
//        ret = done;
//        } catch (InterruptedException e) {
//            ret = done;
//        }
//    }
//    return ret;
//  }
  
//    public synchronized boolean waitForDone() {
//        boolean ret = false;
//    try {
//      while (!done) {
//        wait();
//      }
//      ret = true;
//    } catch (InterruptedException e) {
//    }
//    return ret;
//  }
}
