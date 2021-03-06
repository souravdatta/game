/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sourav.apps.game;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
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
    private JButton loadButton;
    private JButton saveButton;
    
    public Game() {
        super("Game");
        setSize(480, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        gPane = new GraphicsPane();
        nextButton = new JButton("Start");
        nextButton.addActionListener(this);
        resetButton = new JButton("Reset");
        resetButton.addActionListener(this);
        manualCheck = new JCheckBox("Manual mode");
        manualCheck.addItemListener(this);
        loadButton = new JButton("Load");
        loadButton.addActionListener(this);
        saveButton = new JButton("Save");
        saveButton.addActionListener(this);
        
        JPanel fp = new JPanel(new FlowLayout());
        fp.add(manualCheck);
        fp.add(nextButton);
        fp.add(resetButton);
        fp.add(loadButton);
        fp.add(saveButton);
        
        JPanel gp = new JPanel(new FlowLayout());
        gp.add(gPane);
        
        JPanel jp = new JPanel(new BorderLayout());
        jp.add(gp, BorderLayout.NORTH);
        jp.add(fp, BorderLayout.SOUTH);
        
        getContentPane().add(jp);
        setVisible(true);
    }
    
    public static void main(String[] args) {
        new Game();
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
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
        else if (e.getSource().equals(loadButton)) {
            try {
                gPane.loadFromFile("config.cfg");
            } catch (Exception ex) {
                showMessage("Could not find file to load");
            }
        }
        else if (e.getSource().equals(saveButton)) {
            try {
                gPane.saveToFile("config.cfg");
            } catch (Exception ex) {
                showMessage("Could not save to file");
            }
        }
        else if (e.getSource().equals(nextButton)) {
            if (gPane.isRunning()) {
                gPane.stop();
                nextButton.setText("Start");
            }
            else {
                gPane.start();  
                nextButton.setText("Stop");
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        boolean manualMode = (e.getStateChange() == 1)? true : false;
        gPane.setManual(manualMode);
    }
}
