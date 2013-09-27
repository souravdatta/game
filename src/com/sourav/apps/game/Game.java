/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sourav.apps.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author sdatta
 */
public class Game extends JFrame implements ActionListener, ItemListener {

    /**
     * @param args the command line arguments
     */
    private GraphicsPane gPane;
    private JButton nextButton;
    private JButton resetButton;
    private JCheckBox manualCheck;
    
    public Game() {
        super("Game");
        setSize(440, 480);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        gPane = new GraphicsPane();
        nextButton = new JButton("Start");
        nextButton.addActionListener(this);
        resetButton = new JButton("Reset");
        resetButton.addActionListener(this);
        manualCheck = new JCheckBox("Manual mode");
        manualCheck.addItemListener(this);
        
        JPanel jp = new JPanel();
        jp.add(gPane);
        jp.add(manualCheck);
        jp.add(nextButton);
        jp.add(resetButton);
        
        getContentPane().add(jp);
        setVisible(true);
    }
    
    public static void main(String[] args) {
        new Game();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(resetButton)) {
            if (gPane.isRunning()) {
                gPane.stop();
                nextButton.setText("Start");
            }
            gPane.render();
            return;
        }
        if (gPane.isRunning()) {
            gPane.stop();
            nextButton.setText("Start");
        }
        else {
            gPane.start();  
            nextButton.setText("Stop");
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        boolean manualMode = (e.getStateChange() == 1)? true : false;
        gPane.setManual(manualMode);
    }
}
