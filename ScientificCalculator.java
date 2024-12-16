package com.lijin.JSQ;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

public class ScientificCalculator extends BasicCalculator {

    public ScientificCalculator() {
        super();
        addScientificButtons();
        frame.setTitle("科学计算器");
        frame.setSize(600, 500); // 调整窗口大小以容纳更多按钮
    }

    private void addScientificButtons() {
        JPanel scientificPanel = new JPanel();
        scientificPanel.setLayout(new GridLayout(5, 4, 10, 10)); // 按钮之间添加间距
        scientificPanel.setBackground(Color.LIGHT_GRAY); // 面板背景为浅灰色

        String[] scientificButtons = {
                "sin", "cos", "tan", "π",
                "√", "x^2", "x^3", "x^y",
                "ln", "lg", "!", "e",
                "(", ")", "1/x", "积分"
        };

        for (String button : scientificButtons) {
            JButton btn = createStyledButton(button); // 使用基础方法创建按钮
            scientificPanel.add(btn);
        }

        frame.add(scientificPanel, BorderLayout.WEST); // 将科学按钮放在左侧
    }
    private class ScientificButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            switch (command) {
                case "sin":
                case "cos":
                case "tan":
                case "ln":
                case "lg":
                case "√":
                    calculationProcess += command + "("; // 添加函数和左括号
                    break;
                case "x^2":
                    calculationProcess += "^2"; // 平方
                    break;
                case "x^3":
                    calculationProcess += "^3"; // 立方
                    break;
                case "x^y":
                    calculationProcess += "^"; // 幂运算符
                    break;
                case "!":
                    calculationProcess += "!"; // 阶乘
                    break;
                case "π":
                    calculationProcess += "π"; // 显示 π 符号
                    break;
                case "e":
                    calculationProcess += "e"; // 显示 e 符号
                    break;
                case "1/x":
                    calculationProcess += "1/"; // 倒数运算
                    break;
                case "(":
                case ")":
                    calculationProcess += command; // 直接添加括号
                    break;
                default:
                    break;
            }
            display.setText(calculationProcess);
        }
    }

    @Override
    protected double evaluateExpression(String expression) {
        try {
            // 替换科学函数和常量
            expression = replaceScientificFunctions(expression);

            // 调用父类方法进行解析和计算
            return super.evaluateExpression(expression);
        } catch (Exception ex) {
            throw new IllegalArgumentException("无法计算表达式: " + expression, ex);
        }
    }

    /**
     * 替换科学计算函数为可解析的表达式
     */
    protected String replaceScientificFunctions(String expression) {
        expression = expression.replace("π", String.valueOf(Math.PI));
        expression = expression.replace("e", String.valueOf(Math.E));

        // 替换科学函数为计算格式
        expression = expression.replace("sin", "Math.sin");
        expression = expression.replace("cos", "Math.cos");
        expression = expression.replace("tan", "Math.tan");
        expression = expression.replace("ln", "ln");
        expression = expression.replace("lg", "log10");
        expression = expression.replace("√", "Math.sqrt");

        // 阶乘特殊处理
        expression = handleFactorial(expression);

        return expression;
    }

    /**
     * 处理阶乘运算符
     */
    private String handleFactorial(String expression) {
        StringBuilder result = new StringBuilder();
        char[] tokens = expression.toCharArray();

        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] == '!') {
                int j = i - 1;
                while (j >= 0 && (Character.isDigit(tokens[j]) || tokens[j] == '.')) {
                    j--;
                }
                String number = expression.substring(j + 1, i);
                String factorialResult = String.valueOf(factorial(Integer.parseInt(number)));
                result = new StringBuilder(expression.substring(0, j + 1) + factorialResult + expression.substring(i + 1));
                return result.toString(); // 阶乘只需要替换一次即可
            }
        }
        return expression;
    }

    /**
     * 阶乘计算
     */
    private int factorial(int n) {
        if (n < 0) throw new IllegalArgumentException("阶乘仅支持非负整数");
        int result = 1;
        for (int i = 1; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    public static void main(String[] args) {
        new ScientificCalculator();
    }
}
