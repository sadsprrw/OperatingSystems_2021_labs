package sched;
// Run() is called from Scheduling.main() and is where
// the scheduling algorithm written by the user resides.
// User modification should occur within the Run() function.

import java.util.Vector;
import java.io.*;

public class SchedulingAlgorithm {

    private static int get_minimal_arrive(Vector processes) {
        sProcess process = (sProcess) processes.elementAt(0);
        int minArrtime =  process.arrivedTime;
        for (int i = processes.size() - 1; i >= 0; i--){
            process = (sProcess) processes.elementAt(i);
            if(process.arrivedTime < minArrtime){
                minArrtime = process.arrivedTime;
            }
        }
        return minArrtime;
    }

    private static Vector get_arrived_processes(Vector processes, int current_time){
        sProcess process;
        Vector processVector = new Vector();
        for (int i = processes.size() - 1; i >= 0; i--){
            process = (sProcess) processes.elementAt(i);
            if(process.arrivedTime == current_time){
                processVector.addElement(process);
            }
        }
        return processVector;
    }

    private static int get_minimal_prediction(Vector arrProcesses){
        sProcess process =  (sProcess) arrProcesses.firstElement();
        int minPre = process.predictedTime;
        int minPreIndex = arrProcesses.indexOf(process);
        for (int i = arrProcesses.size() - 1; i >= 0; i--){
            process = (sProcess) arrProcesses.elementAt(i);
            if(process.predictedTime < minPre){
                minPre = process.predictedTime;
                minPreIndex = i;
            }
        }
        return minPreIndex;
    }

    public static Results Run(int runtime, Vector processVector, Results result) {
        int i = 0;
        int comptime = 0;
        int previousProcess = 0;
        int size = processVector.size();
        int currentTime = get_minimal_arrive(processVector);
        Vector arrivedProcessVector = get_arrived_processes(processVector, currentTime);
        int currentProcessId = get_minimal_prediction(arrivedProcessVector);
        int completed = 0;
        String resultsFile = "Summary-Processes";

        result.schedulingType =  "Preemptive";
        result.schedulingName = "Shortest Process Next";
        try {
            //BufferedWriter out = new BufferedWriter(new FileWriter(resultsFile));
            //OutputStream out = new FileOutputStream(resultsFile);
            PrintStream out = new PrintStream(new FileOutputStream(resultsFile));
            sProcess process = (sProcess) arrivedProcessVector.elementAt(currentProcessId);
            out.println("Process: " + process.id + " registered... (" + process.cpuTime + " " +
                    process.ioBlocking + " " + process.cpuDone + " " + process.cpuDone + " " + process.predictedTime + ")");
            while (comptime < runtime) {
                if(currentProcessId == -1){
                    currentTime++;
                    Vector newArrived = get_arrived_processes(processVector, currentTime);
                    for (i = newArrived.size() - 1; i >= 0; i--){
                        process = (sProcess) newArrived.elementAt(i);
                        arrivedProcessVector.addElement(process);
                        out.println("Process: " + process.id + " arrived... (" + process.cpuTime + " " +
                                process.ioBlocking + " " + process.cpuDone + " " + process.cpuDone + " " + process.predictedTime + ")");
                    }
                    if (arrivedProcessVector.size() > 0) {
                        currentProcessId = get_minimal_prediction(arrivedProcessVector);
                        process = (sProcess) arrivedProcessVector.elementAt(currentProcessId);
                        out.println("Process: " + process.id + " registered... (" + process.cpuTime + " " +
                                process.ioBlocking + " " + process.cpuDone + " " + process.cpuDone + " " + process.predictedTime +")");
                    }
                    else{
                        currentProcessId = -1;
                    }
                }
                else {
                    if (process.cpuDone == process.cpuTime) {
                        completed++;
                        out.println("Process: " + process.id + " completed... (" + process.cpuTime + " " +
                                process.ioBlocking + " " + process.cpuDone + " " + process.cpuDone + " " + process.predictedTime + ")");
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
                            out.println("Process: " + process.id + " arrived... (" + process.cpuTime + " " +
                                    process.ioBlocking + " " + process.cpuDone + " " + process.cpuDone + " " + process.predictedTime + ")");
                        }
                        if (arrivedProcessVector.size() > 0) {
                            currentProcessId = get_minimal_prediction(arrivedProcessVector);
                            process = (sProcess) arrivedProcessVector.elementAt(currentProcessId);
                            out.println("Process: " + process.id + " registered... (" + process.cpuTime + " " +
                                    process.ioBlocking + " " + process.cpuDone + " " + process.cpuDone + " " + process.predictedTime + ")");
                        } else {
                            currentProcessId = -1;
                        }
                    }
                    if (process.ioBlocking == process.ioNext) {
                        out.println("Process: " + process.id + " I/O blocked... (" + process.cpuTime + " " +
                                process.ioBlocking + " " + process.cpuDone + " " + process.cpuDone + " " + process.predictedTime + ")");
                        process.numBlocked++;
                        process.ioNext = 0;
                        currentTime++;
                        out.println("Process: " + process.id + " is ready... (" + process.cpuTime + " " +
                                process.ioBlocking + " " + process.cpuDone + " " + process.cpuDone + " " + process.predictedTime + ")");
                        Vector newArrived = get_arrived_processes(processVector, currentTime);
                        for (i = newArrived.size() - 1; i >= 0; i--) {
                            process = (sProcess) newArrived.elementAt(i);
                            arrivedProcessVector.addElement(process);
                            out.println("Process: " + process.id + " arrived... (" + process.cpuTime + " " +
                                    process.ioBlocking + " " + process.cpuDone + " " + process.cpuDone + " " + process.predictedTime + ")");
                        }
                        if (arrivedProcessVector.size() > 0) {
                            currentProcessId = get_minimal_prediction(arrivedProcessVector);
                            process = (sProcess) arrivedProcessVector.elementAt(currentProcessId);
                            out.println("Process: " + process.id + " registered... (" + process.cpuTime + " " +
                                    process.ioBlocking + " " + process.cpuDone + " " + process.cpuDone + " " + process.predictedTime + ")");
                        } else {
                            currentProcessId = -1;
                        }
                    }
                    process.cpuDone++;
                    if (process.ioBlocking > 0) {
                        process.ioNext++;
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
