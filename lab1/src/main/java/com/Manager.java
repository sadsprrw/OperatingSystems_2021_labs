package com;

import com.Constants;

import java.net.*;
import java.io.*;

public class Manager {

    public static void main(String[] args) {

        String hostname = Constants.PATH;
        int port = Constants.PORT;

        try (Socket socket = new Socket(hostname, port)) {

            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);


            String text;
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            do {
                System.out.print("Enter text: ");
                text = br.readLine();

                writer.println(text);

                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                String time = reader.readLine();

                System.out.println(time);

            } while (!text.equals("bye"));

            socket.close();

        } catch (UnknownHostException ex) {

            System.out.println("Server not found: " + ex.getMessage());

        } catch (IOException ex) {

            System.out.println("I/O error: " + ex.getMessage());
        }
    }
}