package com.lijin.JSQ;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("计算器");
        JButton basicButton = new JButton("基础计算器");
        JButton scientificButton = new JButton("科学计算器");

        basicButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new BasicCalculator();
            }
        });

        scientificButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ScientificCalculator();
            }
        });

        JPanel panel = new JPanel();
        panel.add(basicButton);
        panel.add(scientificButton);

        frame.add(panel);
        frame.setSize(300, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}