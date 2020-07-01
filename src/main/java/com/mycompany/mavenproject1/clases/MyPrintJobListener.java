/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1.clases;

import javax.print.event.PrintJobEvent;
import javax.print.event.PrintJobListener;

/**
 *
 * @author jean2
 */
public class MyPrintJobListener implements PrintJobListener {
  public void printDataTransferCompleted(PrintJobEvent pje) {
    System.out.println("printDataTransferCompleted");
  }

  public void printJobCanceled(PrintJobEvent pje) {
    System.out.println("The print job was cancelled"); 
  }

  public void printJobCompleted(PrintJobEvent pje) {
    System.out.println("The print job was completed"); 
  }

  public void printJobFailed(PrintJobEvent pje) {
    System.out.println("The print job has failed");
  }

  public void printJobNoMoreEvents(PrintJobEvent pje) {
  }

  public void printJobRequiresAttention(PrintJobEvent pje) {
  }
}
