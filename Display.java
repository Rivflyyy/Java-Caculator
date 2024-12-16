package com.lijin.JSQ;

import javax.swing.*;
import java.awt.*;

public class Display {
    private JTextField displayField;

    public Display() {
        displayField = new JTextField("0", 20);
        displayField.setFont(new Font("楷书", Font.PLAIN, 20));
        displayField.setHorizontalAlignment(JTextField.RIGHT);
        displayField.setEditable(false);
    }

    public JTextField getDisplayField() {
        return displayField;
    }

    public void setText(String text) {
        displayField.setText(text);
    }
}