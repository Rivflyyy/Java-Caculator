package com.lijin.JSQ;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ButtonManager {
    public static JButton createButton(String label, ActionListener listener) {
        JButton button = new JButton(label);

        // 设置背景颜色为浅灰色
        button.setBackground(Color.LIGHT_GRAY);

        // 设置边框为纯白色
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        // 设置按钮字体和大小
        button.setFont(new Font("Arial", Font.PLAIN, 16));

        // 添加按钮点击事件监听器
        button.addActionListener(listener);

        // 设置按钮之间的间距
        button.setMargin(new Insets(5, 5, 5, 5));

        // 设置按钮的透明度以确保背景色可见
        button.setOpaque(true);

        return button;
    }
}
