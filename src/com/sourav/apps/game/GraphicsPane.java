/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sourav.apps.game;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JPanel;
import java.util.Random;

/**
 *
 * @author sdatta
 */
public class GraphicsPane extends JPanel implements Runnable, MouseListener {
    public static final int WIDTH = 400;
    public static final int HEIGHT = 400;
    public static final int BLOCKN = 50;
    
    private int[][] blocks;
    private Random randGen;
    private Thread runThread;
    private volatile boolean running = false;
    private volatile boolean manual = false;

    public boolean isManual() {
        return manual;
    }
    
    public void setManual(boolean m) {
        manual = m;
    }
    
    public boolean isRunning() {
        return running;
    }
    
    public GraphicsPane() {
        blocks = new int[BLOCKN][BLOCKN];    
        addMouseListener(this);
        update();
    }
    
    private void update() {
        randGen = new Random(new java.util.Date().getTime());
        for (int i = 0; i < BLOCKN; i++) {
            for (int j = 0; j < BLOCKN; j++) {                
                int dice = 0;
                if (!isManual()) {
                    dice = randGen.nextInt(2);
                }
                dice = (dice < 0)? -dice : dice;
                blocks[i][j] = dice;
            }
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {        
        int block_width = WIDTH / BLOCKN;
        int block_height = HEIGHT / BLOCKN;
        
        for (int start_y = 0, i = 0; 
             i < BLOCKN;
             i++, start_y += block_height) {
            for (int start_x = 0, j = 0;
                 j < BLOCKN; 
                 j++, start_x += block_width) {
                if (blocks[i][j] == 1) {
                    g.setColor(Color.black);
                    g.drawRect(start_y, start_x, block_width, block_height);
                    g.setColor(Color.green);
                    g.fillRect(start_y+1, start_x+1, block_width-1, block_height-1);
                }
                else {
                    g.setColor(Color.black);
                    g.drawRect(start_y, start_x, block_width, block_height);
                    g.setColor(Color.white);
                    g.fillRect(start_y+1, start_x+1, block_width-1, block_height-1);
                }
            }
        }
    }
    
    @Override
    public Dimension preferredSize() {
        return new Dimension(WIDTH, HEIGHT);
    }
    
    public void render() {    
        update();
        repaint();
    }
    
    private int prev_x(int i) {
        if (i == 0)
            return BLOCKN - 1;
        else
            return  i - 1;
    }
    
    private int prev_y(int j) {
        if (j == 0)
            return BLOCKN - 1;
        else
            return j - 1;
    }
    
    private int next_x(int i) {
        if (i == BLOCKN - 1)
            return 0;
        else
            return i + 1;
    }
    
    private int next_y(int j) {
        if (j == BLOCKN - 1)
            return 0;
        else
            return j + 1;
    }
    
    private int sumNeighbours(int i, int j) {
        int p1 = blocks[prev_x(i)][j];
        int p2 = blocks[prev_x(i)][prev_y(j)];
        int p3 = blocks[i][prev_y(j)];
        int p4 = blocks[next_x(i)][prev_y(j)];
        int p5 = blocks[next_x(i)][j];
        int p6 = blocks[next_x(i)][next_y(j)];
        int p7 = blocks[i][next_y(j)];
        int p8 = blocks[prev_x(i)][next_y(j)];
        
        return p1+p2+p3+p4+p5+p6+p7+p8;
    }
    
    public void renderCalc() {
        // Calculate new array
        int[][] newBlocks = new int[BLOCKN][BLOCKN];
        for (int i = 0; i < BLOCKN; i++) {
            for (int j = 0; j < BLOCKN; j++) {
                int sum = sumNeighbours(i, j);
                int curr = blocks[i][j];
                
                if (curr == 1 && sum < 2) {
                    curr = 0;
                }
                else if (curr == 1 && (sum == 2 || sum == 3)) {
                    curr = 1;
                }
                else if (curr == 1 && sum > 3) {
                    curr = 0;
                }
                else if (curr == 0 && sum == 3) {
                    curr = 1;
                }
                
                newBlocks[i][j] = curr;
            }
        }
        
        blocks = newBlocks;
        repaint();
    }
    
    public void start() {
        runThread = new Thread(this);
        running = true;
        runThread.start();
    }
    
    public void stop() {
        running = false;
        try {
            runThread.join();
        } catch (InterruptedException ex) {}
        runThread = null;
    }

    @Override
    public void run() {
        while (running) {
            renderCalc();
            try {
                Thread.sleep(300);
            } catch (InterruptedException ex) {}
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!isManual())
            return;
        
        int cx = e.getX();
        int cy = e.getY();
        
        System.out.printf("Clicked@(%d, %d)\n", cx, cy);
        
        int block_width = WIDTH / BLOCKN;
        int block_height = HEIGHT / BLOCKN;
        
        for (int start_y = 0, i = 0; 
             i < BLOCKN;
             i++, start_y += block_height) {
            
            if ((cy >= start_y) && (cy <= start_y + block_height)) {
                for (int start_x = 0, j = 0;
                     j < BLOCKN; 
                     j++, start_x += block_width) {
                    
                    if ((cx >= start_x) && (cx <= start_x + block_width)) {
                        // indices are actually (j, i)
                        // as they are rendered reversed
                        
                        if (blocks[j][i] == 1)
                            blocks[j][i] = 0;
                        else
                            blocks[j][i] = 1;
                    }
                }
            }
            
        }
        
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }
    
    public void saveToFile(String fileName) throws IOException {
        ObjectOutputStream ostream = 
                new ObjectOutputStream(new FileOutputStream(fileName));
        ostream.writeObject(this.blocks);
        ostream.flush();
        ostream.close();
    }
    
    public void loadFromFile(String fileName) throws Exception {
        ObjectInputStream istream =
                new ObjectInputStream(new FileInputStream(fileName));
        this.blocks = (int[][])istream.readObject();
        repaint();
    }
}
