package com;

import java.io.IOException;
import java.util.Scanner;

public class Menu {
    public void menu() throws IOException {

        String action;
        Scanner selector = new Scanner(System.in);

        System.out.println("Enter argument: ");

        action = selector.nextLine();
        new Server( action).run();
    }
}
