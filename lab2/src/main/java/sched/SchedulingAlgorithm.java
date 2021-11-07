package sched;
// Run() is called from Scheduling.main() and is where
// the scheduling algorithm written by the user resides.
// User modification should occur within the Run() function.

import java.util.Vector;
import java.io.*;

public class SchedulingAlgorithm {

    private static int get_minimal_arrive(Vector processes) {
        sProcess process = (sProcess) processes.elementAt(0);
        int minArrtime =  process.arrivedtime;
        for (int i = processes.size() - 1; i >= 0; i--){
            process = (sProcess) processes.elementAt(i);
            if(process.arrivedtime < minArrtime){
                minArrtime = process.arrivedtime;
            }
        }
        return minArrtime;
    }

    private static Vector get_arrived_processes(Vector processes, int current_time){
        sProcess process;
        Vector processVector = new Vector();
        for (int i = processes.size() - 1; i >= 0; i--){
            process = (sProcess) processes.elementAt(i);
            if(process.arrivedtime == current_time){
                processVector.addElement(process);
            }
        }
        return processVector;
    }

    private static int get_minimal_cputime(Vector arrProcesses){
        sProcess process =  (sProcess) arrProcesses.firstElement();
        int minCpu = process.bursttime - process.cpudone/process.ioblocking;

        int minCpuIndex = arrProcesses.indexOf(process);
        for (int i = arrProcesses.size() - 1; i >= 0; i--){
            process = (sProcess) arrProcesses.elementAt(i);
            if(process.bursttime - process.cpudone/process.ioblocking < minCpu){
                minCpu = process.bursttime - process.cpudone/process.ioblocking;
                minCpuIndex = i;
            }
        }
        return minCpuIndex;
    }

    public static Results Run(int runtime, Vector processVector, Results result) {
        int i = 0;
        int comptime = 0;
        int previousProcess = 0;
        int size = processVector.size();
        int currentTime = get_minimal_arrive(processVector);
        Vector arrivedProcessVector = get_arrived_processes(processVector, currentTime);
        int currentProcessId = get_minimal_cputime(arrivedProcessVector);
        int completed = 0;
        String resultsFile = "Summary-Processes";

        result.schedulingType =  "Preemptive";
        result.schedulingName = "Shortest Job First";
        try {
            //BufferedWriter out = new BufferedWriter(new FileWriter(resultsFile));
            //OutputStream out = new FileOutputStream(resultsFile);
            PrintStream out = new PrintStream(new FileOutputStream(resultsFile));
            sProcess process = (sProcess) arrivedProcessVector.elementAt(currentProcessId);
            out.println("Process: " + process.id + " registered... (" + process.cputime + " " +
                    process.ioblocking + " " + process.cpudone + " " + process.cpudone + ")");
            while (comptime < runtime) {
                if(currentProcessId == -1){
                    currentTime++;
                    Vector newArrived = get_arrived_processes(processVector, currentTime);
                    for (i = newArrived.size() - 1; i >= 0; i--){
                        process = (sProcess) newArrived.elementAt(i);
                        arrivedProcessVector.addElement(process);
                        out.println("Process: " + process.id + " arrived... (" + process.cputime + " " +
                                process.ioblocking + " " + process.cpudone + " " + process.cpudone + ")");
                    }
                    if (arrivedProcessVector.size() > 0) {
                        currentProcessId = get_minimal_cputime(arrivedProcessVector);
                        process = (sProcess) arrivedProcessVector.elementAt(currentProcessId);
                        out.println("Process: " + process.id + " registered... (" + process.cputime + " " +
                                process.ioblocking + " " + process.cpudone + " " + process.cpudone + ")");
                    }
                    else{
                        currentProcessId = -1;
                    }
                }
                else {
                    if (process.cpudone == process.cputime) {
                        completed++;
                        out.println("Process: " + process.id + " completed... (" + process.cputime + " " +
                                process.ioblocking + " " + process.cpudone + " " + process.cpudone + ")");
                        arrivedProcessVector.remove(currentProcessId);
                        if (completed == size) {
                            result.compuTime = comptime;
                            out.close();
                            return result;
                        }
                        currentTime++;
                        Vector newArrived = get_arrived_processes(processVector, currentTime);
                        for (i = newArrived.size() - 1; i >= 0; i--) {
                            process = (sProcess) newArrived.elementAt(i);
                            arrivedProcessVector.addElement(process);
                            out.println("Process: " + process.id + " arrived... (" + process.cputime + " " +
                                    process.ioblocking + " " + process.cpudone + " " + process.cpudone + ")");
                        }
                        if (arrivedProcessVector.size() > 0) {
                            currentProcessId = get_minimal_cputime(arrivedProcessVector);
                            process = (sProcess) arrivedProcessVector.elementAt(currentProcessId);
                            out.println("Process: " + process.id + " registered... (" + process.cputime + " " +
                                    process.ioblocking + " " + process.cpudone + " " + process.cpudone + ")");
                        } else {
                            currentProcessId = -1;
                        }
                    }
                    if (process.ioblocking == process.ionext) {
                        out.println("Process: " + process.id + " I/O blocked... (" + process.cputime + " " +
                                process.ioblocking + " " + process.cpudone + " " + process.cpudone + ")");
                        process.numblocked++;
                        process.ionext = 0;
                        currentTime++;
                        Vector newArrived = get_arrived_processes(processVector, currentTime);
                        for (i = newArrived.size() - 1; i >= 0; i--) {
                            process = (sProcess) newArrived.elementAt(i);
                            arrivedProcessVector.addElement(process);
                            out.println("Process: " + process.id + " arrived... (" + process.cputime + " " +
                                    process.ioblocking + " " + process.cpudone + " " + process.cpudone + ")");
                        }
                        if (arrivedProcessVector.size() > 0) {
                            currentProcessId = get_minimal_cputime(arrivedProcessVector);
                            process = (sProcess) arrivedProcessVector.elementAt(currentProcessId);
                            out.println("Process: " + process.id + " registered... (" + process.cputime + " " +
                                    process.ioblocking + " " + process.cpudone + " " + process.cpudone + ")");
                        } else {
                            currentProcessId = -1;
                        }
                    }
                    process.cpudone++;
                    if (process.ioblocking > 0) {
                        process.ionext++;
                    }
                    comptime++;
                }
            }
            out.close();
        } catch (IOException e) { /* Handle exceptions */ }
        result.compuTime = comptime;
        return result;
    }
}
