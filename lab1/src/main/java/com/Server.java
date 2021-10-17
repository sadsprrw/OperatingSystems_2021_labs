package com;

import com.computation.ComputationClient;
import com.handlers.KeyHandler;

import java.io.File;
import java.io.IOException;
import java.nio.*;
import java.net.*;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Server {

    private static Process processF;
    private static Process processG;
    public static boolean ended;

    public static Process getF(){return processF;}
    public static Process getG(){return processG;}

    private List<Integer> results;
    private List<String> resultStr;

    private final String value;


    public static Boolean endedF = false;
    public static Boolean endedG = false;

    private ServerSocketChannel ssChannel;

    public Server(String value){
        this.value = value;
        results = new ArrayList<>();
        resultStr = new ArrayList<>();
    }

    public void run() throws IOException {
        new Thread( ( ) -> {
            KeyHandler keyHandler = new KeyHandler();
            keyHandler.start();
        } ).start();
        try {
            System.out.println("Starting Server!");
            long start, end;
            start = System.nanoTime();

            ssChannel = ServerSocketChannel.open();
            ssChannel.socket().bind(new InetSocketAddress(Constants.PATH, Constants.PORT));
            ssChannel.configureBlocking(false);

            String path = "/Users/jekav/IdeaProjects/OperatingSystems_2021_labs/OperatingSystems_2021_labs/lab1/target/classes";

            ProcessBuilder builderF = new ProcessBuilder("java", "processF.FProcess", String.valueOf(value));
            ProcessBuilder builderG = new ProcessBuilder("java", "processG.GProcess", String.valueOf(value));
            builderF.directory(new File(path));
            builderG.directory(new File(path));

            System.out.println("Starting Processes...");
            processF = builderF.start();
            processG = builderG.start();
            System.out.println("Succesfull!");
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            System.out.println("Waiting for process answers...");
            while(!ended){
                SocketChannel sChannel = ssChannel.accept();
                if(sChannel != null){
                    buffer.clear();
                    buffer.put(value.getBytes());
                    buffer.flip();
                    sChannel.write(buffer);
                    buffer.clear();

                    int size = sChannel.read(buffer);
                    if(size == -1){
                        sChannel.close();
                        continue;
                    }
                    byte[] data = new byte[size];
                    buffer.flip();
                    buffer.get(data);
                    buffer.clear();
                    String getData = new String(data);



                    String[] parsedData = getData.split(" ");

                    if(parsedData[0].equals("Function")){
                        System.out.println(getData);
                        System.out.println("STATUS: HARD FAIL");
                        if(parsedData[1].equals("F")) {
                            if(!endedG) System.out.println("Function G interrupts");
                        }
                        else {
                            if(!endedF) System.out.println("Function F interrupts");
                        }
                        end = System.nanoTime();
                        System.out.println("Time of execution: " + (end - start));
                        break;
                    }

                    if (parsedData[0].equals("F")) {
                        System.out.println("Server received result from function F: "+ parsedData[1]);
                        endedF = true;
                    }

                    else if (parsedData[0].equals("G")) {
                        System.out.println("Server received result from function G: " + parsedData[1]);
                        endedG = true;
                    }
                    if (parsedData[1].equals("0")) {
                        System.out.println("The function " + parsedData[0] + " returned 0");
                        System.out.println("Result of calculation is 0");
                        end = System.nanoTime();
                        System.out.println("Time of execution: " + (end - start));
                        break;
                    }



                    results.add(Integer.parseInt(parsedData[1]));

                    if (endedG && endedF) {
                        System.out.println("STATUS: SUCCESS");
                        int res = 1;
                        for (Integer value: results) {
                            res *= value;
                        }
                        resultStr.add("Multiplication result: " + res);

                        break;
                    }
                }
            }
            System.out.println("Ended");
        } catch (IOException error) {
            ssChannel.close();
        }
        destroyProcesses();
        for (String str: resultStr) {
            System.out.println(str);
        }
        System.exit(0);
    }
    private static void destroyProcesses() {
        processF.destroy();
        processG.destroy();
    }

    public static void main(String[] args) throws IOException {
        new Menu().menu();
    }
}
