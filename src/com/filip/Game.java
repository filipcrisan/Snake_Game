package com.filip;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

public class Game extends JPanel implements ActionListener {
    private Deque<Point> snakeLoc=new ArrayDeque<>();
    private Point appleLoc=new Point();
    private Point tail=new Point();

    private Image appleIm=(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("res/apple.png")))).getImage();
    private Image bodyIm=(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("res/body.png")))).getImage();
    private Image headIm=(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("res/head.png")))).getImage();

    private final int boardW=150;
    private final int boardL=150;

    private Timer timer=new Timer(140, this);

    private boolean inGame=true;
    private boolean goN=false;
    private boolean goE=true;
    private boolean goS=false;
    private boolean goW=false;

    public Game() {
        setBackground(Color.black);
        setPreferredSize(new Dimension(boardL, boardW));

        setFocusable(true);  // to fire keyboard events
        addKeyListener(new changeDir());

        startGame();
    }

    private void startGame() {
        snakeLoc.clear();

        for(int i=1;i<=3;i++)
            snakeLoc.addLast(new Point(50-i*10, 50));

        tail=new Point(snakeLoc.getLast());

        createAL();

        timer.start();
    }

    private void createAL() {
        boolean ok=true;
        while(ok) {
            appleLoc.x=(int)(Math.random()*19);
            appleLoc.x*=10; // put on right pos: multiple of ten (bcs every image has 10px)
            appleLoc.y=(int)(Math.random()*19);
            appleLoc.y*=10;

            ok=false;
            for(Point p:snakeLoc)
                if(p.equals(appleLoc)) {
                    ok=true;
                    break;
                }

            if(appleLoc.x>=boardL-10 || appleLoc.y>=boardW-10)
                ok=true;
        }
    }

    private void moveSnake() {
        Point nextHP=new Point(snakeLoc.getFirst());
        if(goN)
            nextHP.y-=10;
        else if(goE)
            nextHP.x+=10;
        else if(goS)
            nextHP.y+=10;
        else if(goW)
            nextHP.x-=10;

        tail=new Point(snakeLoc.getLast());

        snakeLoc.removeLast();
        snakeLoc.addLast(nextHP);
        int length=snakeLoc.size()-1;
        while(length>=1) {
            snakeLoc.addLast(snakeLoc.getFirst());
            snakeLoc.removeFirst();
            length--;
        }
    }

    private void checkInGame() {
        boolean ok=true;
        Point hPos=new Point(snakeLoc.getFirst());
        snakeLoc.addLast(hPos);
        snakeLoc.removeFirst();
        int length=snakeLoc.size()-1;
        while(length>=1) {
            if(snakeLoc.getFirst().equals(hPos) && length>=3)
                ok=false;
            snakeLoc.addLast(snakeLoc.getFirst());
            snakeLoc.removeFirst();
            length--;
        }

        if(!ok || snakeLoc.getFirst().x<0 || snakeLoc.getFirst().x>=boardL || snakeLoc.getFirst().y<0 || snakeLoc.getFirst().y>=boardW)
            inGame=false;

        if(!inGame)
            timer.stop();
    }

    private void grow()  {
        if(snakeLoc.getFirst().equals(appleLoc)) {
            snakeLoc.addLast(tail);
            createAL();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(inGame) {
            grow();
            moveSnake();
            checkInGame();
        }

        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(inGame) {
            g.drawImage(appleIm, appleLoc.x, appleLoc.y, this);

            int length=snakeLoc.size();
            Point headLoc=new Point(snakeLoc.getFirst());
            while(length>=1) {
                if(snakeLoc.getFirst().equals(headLoc))
                    g.drawImage(headIm, snakeLoc.getFirst().x, snakeLoc.getFirst().y, this);
                else
                    g.drawImage(bodyIm, snakeLoc.getFirst().x, snakeLoc.getFirst().y, this);
                snakeLoc.addLast(snakeLoc.getFirst());
                snakeLoc.removeFirst();
                length--;
            }
            Toolkit.getDefaultToolkit().sync();
        }
        else
            gameOver(g);
    }

    private void gameOver(Graphics g) {
        g.setFont(new Font("", Font.PLAIN, 15));
        g.setColor(Color.GREEN);
        g.drawString("\uD835\uDC06\uD835\uDC1A\uD835\uDC26\uD835\uDC1E \uD835\uDC28\uD835\uDC2F\uD835\uDC1E\uD835\uDC2B", 37, 75);
    }

    private class changeDir extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int keyCode=e.getKeyCode();
            if(keyCode==KeyEvent.VK_UP && !goS) {
                goN=true;
                goE=false;
                goW=false;
            }
            else if(keyCode==KeyEvent.VK_RIGHT && !goW) {
                goE=true;
                goN=false;
                goS=false;
            }
            else if(keyCode==KeyEvent.VK_DOWN && !goN) {
                goS=true;
                goE=false;
                goW=false;
            }
            else if(keyCode==KeyEvent.VK_LEFT && !goE) {
                goW=true;
                goN=false;
                goS=false;
            }
        }
    }
}