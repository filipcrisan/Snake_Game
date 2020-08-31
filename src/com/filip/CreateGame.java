package com.filip;

import javax.swing.*;
import java.awt.*;

public class CreateGame extends JFrame {
    public static void create() {
        JFrame mySnake=new JFrame();
        mySnake.add(new Game());
        mySnake.pack();
        mySnake.setResizable(false);
        mySnake.setLocationRelativeTo(null);
        mySnake.setVisible(true);
        mySnake.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                create();
            }
        });
    }
}


