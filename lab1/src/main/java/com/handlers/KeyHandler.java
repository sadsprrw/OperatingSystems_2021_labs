package com.handlers;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import com.Server;
public class KeyHandler {
    JFrame frame;

    public void start() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(0, 0);
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.setFocusTraversalKeysEnabled(false);
        frame.addKeyListener(new KeyListener());
        frame.setFocusable(true);
    }

    private static void reasonOfCancelation() {
        if (Server.endedF && !Server.endedG) {
            System.out.println("STATUS: CANCELLED");
            System.out.println("The process is stopped because function G hangs");
        }
        else if (Server.endedG && !Server.endedF) {
            System.out.println("STATUS: CANCELLED");
            System.out.println("The process is stopped because function F hangs");
        }
        else if(!Server.endedF && !Server.endedG) {
            System.out.println("STATUS: CANCELLED");
            System.out.println("Functions F and G are not calculated");
        }
        System.out.println("Result of calculation is undefined");
    }

    private class KeyListener extends KeyAdapter implements ActionListener {

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                System.out.println("CLOSED");
                Server.getF().destroy();
                Server.getG().destroy();
                Server.ended = true;
                reasonOfCancelation();
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
}
