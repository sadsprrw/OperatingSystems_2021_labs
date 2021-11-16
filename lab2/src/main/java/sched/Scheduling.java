package sched;
// This file contains the main() function for the Scheduling
// simulation.  Init() initializes most of the variables by
// reading from a provided file.  SchedulingAlgorithm.Run() is
// called from main() to run the simulation.  Summary-Results
// is where the summary results are written, and Summary-Processes
// is where the process scheduling summary is written.

// Created by Alexander Reeder, 2001 January 06

import java.io.*;
import java.sql.Array;
import java.util.*;


public class Scheduling {

  private static int processnum = 5;
  private static int meanDev = 1000;
  private static int standardDev = 100;
  private static int runtime = 1000;
  private static Vector processVector = new Vector();
  private static Results result = new Results("null","null",0);
  private static String resultsFile = "Summary-Results";
  private static int[] oldPredictedTime = new int[0];
  private static double a = 0.5;

  private static void Init(String file, String timeFile) {
    File f = new File(file);
    File g = new File(timeFile);
    String line;
    String tmp;

    double X = 0.0;
    int[] cpuTimes = new int[0];
    int[] processBlocking = new int[0];
    int[] arrTimes = new int[0];

    try {   
      int cpuTimeCount = 0;
      int arrTimeCount = 0;
      int processCount = 0;
      int preTimeCount = 0;
      DataInputStream in = new DataInputStream(new FileInputStream(f));

      while ((line = in.readLine()) != null) {
        if (line.startsWith("numprocess")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          processnum = Common.s2i(st.nextToken());
          cpuTimes = new int[processnum];
          processBlocking = new int[processnum];
          arrTimes = new int[processnum];
          oldPredictedTime = new int[processnum];
        }
        if (line.startsWith("meandev")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          meanDev = Common.s2i(st.nextToken());
        }
        if (line.startsWith("standdev")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          standardDev = Common.s2i(st.nextToken());
        }
        if (line.startsWith("time")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          cpuTimes[cpuTimeCount] = Common.s2i(st.nextToken());
          cpuTimeCount++;
        }
        if (line.startsWith("process")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          processBlocking[processCount] = Common.s2i(st.nextToken());
          processCount++;
        }
        if (line.startsWith("arrived")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          arrTimes[arrTimeCount] = Common.s2i(st.nextToken());
          arrTimeCount++;
        }
        if (line.startsWith("runtime")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          runtime = Common.s2i(st.nextToken());
        }
      }
      DataInputStream gin = new DataInputStream(new FileInputStream(g));

      while ((line = gin.readLine()) != null) {
        if (line.startsWith("time")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          oldPredictedTime[preTimeCount] = Common.s2i(st.nextToken());
          preTimeCount++;
        }
        if (line.startsWith("a")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          a = Common.s2d(st.nextToken());
        }
      }
      for(int i = 0; i < processnum; i++){
        int timePrediction = (int)(Math.random() * 300 + oldPredictedTime[i] - 150);
        int newPredict = (int)(oldPredictedTime[i] * a + timePrediction * (1 - a));
        processVector.addElement(new sProcess(cpuTimes[i], processBlocking[i], 0, 0, 0,
                arrTimes[i], processVector.size(), newPredict));
      }

      in.close();
    } catch (IOException e) { /* Handle exceptions */ }
  }

  private static void debug() {
    int i = 0;

    System.out.println("processnum " + processnum);
    System.out.println("meandevm " + meanDev);
    System.out.println("standdev " + standardDev);
    int size = processVector.size();
    for (i = 0; i < size; i++) {
      sProcess process = (sProcess) processVector.elementAt(i);
      System.out.println("process " + i + " " + process.cpuTime + " " + process.ioBlocking
              + " " + process.cpuDone + " " + process.numBlocked);
    }
    System.out.println("runtime " + runtime);
  }

  public static void main(String[] args) {
    int i = 0;

    if (args.length != 2) {
      System.out.println("Usage: 'java Scheduling <INIT FILE>'");
      System.exit(-1);
    }
    File f = new File(args[0]);
    File d = new File(args[1]);
    if (!(f.exists())) {
      System.out.println("Scheduling: error, file '" + f.getName() + "' does not exist.");
      System.exit(-1);
    }  
    if (!(f.canRead())) {
      System.out.println("Scheduling: error, read of " + f.getName() + " failed.");
      System.exit(-1);
    }
    if (!(d.exists())) {
      System.out.println("Scheduling: error, file '" + d.getName() + "' does not exist.");
      System.exit(-1);
    }
    if (!(d.canRead())) {
      System.out.println("Scheduling: error, read of " + d.getName() + " failed.");
      System.exit(-1);
    }
    System.out.println("Working...");
    Init(args[0], args[1]);
    if (processVector.size() < processnum) {
      i = 0;
      while (processVector.size() < processnum) {       
          double X = Common.R1();
          while (X == -1.0) {
            X = Common.R1();
          }
          X = X * standardDev;
        int cputime = (int) X + meanDev;
        processVector.addElement(new sProcess(cputime,i*100,0,0,0,
                (int)(Math.random() * 9 + 1), processVector.size(), (int)(Math.random() * 300 + cputime - 150)));
        i++;
      }
    }
    result = SchedulingAlgorithm.Run(runtime, processVector, result);    
    try {
      PrintStream gout = new PrintStream(new FileOutputStream(args[1]));
      PrintStream out = new PrintStream(new FileOutputStream(resultsFile));
      out.println("Scheduling Type: " + result.schedulingType);
      out.println("Scheduling Name: " + result.schedulingName);
      out.println("Simulation Run Time: " + result.compuTime);
      out.println("Mean: " + meanDev);
      out.println("Standard Deviation: " + standardDev);
      out.println("Results\t\tCPU Time\tIO Blocking\tCPU Completed\tCPU Blocked\t\tArrive Time");

      gout.println("// old execution results");
      for (i = 0; i < processVector.size(); i++) {
        sProcess process = (sProcess) processVector.elementAt(i);
        out.print(Integer.toString(process.id));
        if (process.id < 100) { out.print("\t\t\t"); } else { out.print("\t\t"); }
        out.print(Integer.toString(process.cpuTime));
        if (process.cpuTime < 100) { out.print(" (ms)\t\t"); } else { out.print(" (ms)\t"); }
        out.print(Integer.toString(process.ioBlocking));
        if (process.ioBlocking < 100) { out.print(" (ms)\t\t"); } else { out.print(" (ms)\t"); }
        out.print(Integer.toString(process.cpuDone));
        if (process.cpuDone < 100) { out.print(" (ms)\t\t\t"); } else { out.print(" (ms)\t\t"); }
        out.print(Integer.toString(process.numBlocked));
        if (process.numBlocked < 10) { out.print(" times\t\t\t"); } else { out.print(" times\t\t"); }
        out.println(Integer.toString(process.arrivedTime));
        int execResult = (int)(oldPredictedTime[i]*a + (1-a) * process.cpuTime);
        gout.println("time " + execResult);
        gout.println();
      }
      gout.println("// a #constant");
      gout.println("a " + a);
      gout.close();
      out.close();
    } catch (IOException e) { /* Handle exceptions */ }
  System.out.println("Completed.");
  }
}

