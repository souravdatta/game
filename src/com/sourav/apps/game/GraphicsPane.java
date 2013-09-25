/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sourav.apps.game;

import java.awt.*;
import javax.swing.JPanel;
import java.util.Random;

/**
 *
 * @author sdatta
 */
public class GraphicsPane extends JPanel implements Runnable {
    public static final int WIDTH = 200;
    public static final int HEIGHT = 200;
    public static final int BLOCKN = 20;
    
    private int[][] blocks;
    private Random randGen;
    private Thread runThread;
    private volatile boolean running = false;

    public boolean isRunning() {
        return running;
    }
    
    public GraphicsPane() {
        blocks = new int[WIDTH][HEIGHT];        
        update();
    }
    
    private void update() {
        randGen = new Random(new java.util.Date().getTime());
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {                
                int dice = randGen.nextInt(2);
                dice = (dice < 0)? -dice : dice;
                blocks[i][j] = dice;
            }
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {        
        int block_width = WIDTH / BLOCKN;
        int block_height = HEIGHT / BLOCKN;
        
        for (int start_y = 0; start_y < HEIGHT; start_y += block_height) {
            for (int start_x = 0; start_x < WIDTH; start_x += block_width) {
                if (blocks[start_y][start_x] == 1) {
                    g.setColor(Color.green);
                    g.fillRect(start_y, start_x, block_width, block_height);
                }
                else {
                    g.setColor(Color.white);
                    g.fillRect(start_y, start_x, block_width, block_height);
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
            render();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {}
        }
    }
}
