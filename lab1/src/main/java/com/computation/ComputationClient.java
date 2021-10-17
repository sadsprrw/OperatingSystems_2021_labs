package com.computation;

import com.Constants;

import os.lab1.compfuncs.basic.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Optional;
import java.util.function.Function;

public class ComputationClient {

    private final char type;
    private int arg;
    private Optional<Integer> result;

    private static final int maxCountAttempts = 3;

    public ComputationClient(char type){
        this.type = type;
        getFunction();
    }

    private void getFunction(){

    }

    public void compute(String argument) throws IOException, InterruptedException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        arg = Integer.parseInt(argument);
        int countAttempts = 0;
        int randomValue = (int)(Math.random() * 10);
        SocketChannel channel = SocketChannel.open(new InetSocketAddress(Constants.PATH, Constants.PORT));

        while (countAttempts < maxCountAttempts){
            if(randomValue < 5 && type == 'F'){
                Thread.sleep(1000);
                countAttempts++;
                randomValue = (int)(Math.random() * 10);
            }
            else{
                if(type == 'F') {
                    result = IntOps.trialF(arg);
                }
                if(type == 'G') {
                    result = IntOps.trialG(arg);
                }
                if (result.isPresent()) {
                    String resultAnswer = type + " " + result.get();
                    buffer.put(resultAnswer.getBytes());
                    buffer.flip();
                    channel.write(buffer);
                }
            }
        }
        String resultAnswer = "Function " + type + " failed " + maxCountAttempts + " times";
        buffer.put(resultAnswer.getBytes());
        buffer.flip();
        channel.write(buffer);
    }
}
