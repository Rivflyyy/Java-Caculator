package com.lijin.JSQ;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BasicCalculator {
    protected JFrame frame;
    protected JPanel panel;
    protected JTextField display;
    protected String calculationProcess = "";

    public BasicCalculator() {
        frame = new JFrame("基础计算器");
        frame.getContentPane().setBackground(Color.LIGHT_GRAY); // 设置整体背景为浅灰色
        panel = new JPanel();
        display = new JTextField("0", 20);

        display.setFont(new Font("楷书", Font.PLAIN, 20));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);

        panel.setLayout(new GridLayout(5, 4, 10, 10)); // 按钮之间的间距
        panel.setBackground(Color.LIGHT_GRAY); // 按钮面板背景为浅灰色

        String[] buttons = {
                "C", "/", "*", "Del",
                "7", "8", "9", "-",
                "4", "5", "6", "+",
                "1", "2", "3", "=",
                "%", "0", ".", "sci"
        };

        for (String button : buttons) {
            JButton btn = createStyledButton(button);
            if ("=".equals(button) || "sci".equals(button)) {
                btn.setBackground(new Color(0, 122, 255)); // 蓝色背景
                btn.setForeground(Color.WHITE); // 白色字体
            } else if ("C".equals(button) || "/".equals(button) || "*".equals(button) || "Del".equals(button) || "+".equals(button) || "-".equals(button)){
                btn.setForeground(Color.BLUE); // 蓝色字体
            }
            panel.add(btn);
        }

        frame.add(display, BorderLayout.NORTH);
        frame.add(panel, BorderLayout.CENTER);
        frame.setSize(400, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * 创建带圆角和自定义样式的按钮
     */
    public JButton createStyledButton(String label) {
        JButton button = new JButton(label) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g); // 确保调用父类的绘制方法
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // 圆角设置
                g2.setColor(getForeground());
                FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(getText());
                int textHeight = fm.getHeight();
                int x = (getWidth() - textWidth) / 2;
                int y = (getHeight() + textHeight / 2) / 2 - 2;
                g2.drawString(getText(), x, y); // 居中显示文本
            }
        };

        button.setContentAreaFilled(false); // 禁用默认填充
        button.setOpaque(true); // 使背景可见
        button.setBackground(Color.WHITE); // 默认背景为纯白
        button.setForeground(Color.BLACK); // 字体颜色为黑色
        button.setFocusPainted(false); // 禁用聚焦时的边框
        button.setFont(new Font("楷书", Font.BOLD, 16));
        button.addActionListener(new ButtonClickListener());
        return button;
    }

    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            switch (command) {
                case "C":
                    calculationProcess = "";
                    display.setText("0");
                    break;
                case "Del":
                    if (!calculationProcess.isEmpty()) {
                        calculationProcess = calculationProcess.substring(0, calculationProcess.length() - 1);
                        display.setText(calculationProcess.isEmpty() ? "0" : calculationProcess);
                    }
                    break;
                case "=":
                    try {
                        double result = evaluateExpression(calculationProcess);
                        display.setText(String.valueOf(result));
                        calculationProcess = ""; // 清空表达式
                    } catch (Exception ex) {
                        display.setText("错误");
                        System.err.println("计算错误: " + calculationProcess);
                        ex.printStackTrace();
                        calculationProcess = ""; // 清空表达式
                    }
                    break;
                case "sci":
                    frame.dispose();
                    new ScientificCalculator(); // 切换到科学计算器
                    break;
                default:
                    calculationProcess += command;
                    display.setText(calculationProcess);
                    break;
            }
        }
    }

    /**
     * 解析和计算表达式
     */
    protected double evaluateExpression(String expression) {
        try {
            System.out.println("正在计算: " + expression);
            return evaluateWithFunctions(expression);
        } catch (Exception ex) {
            System.err.println("计算错误: " + expression);
            throw new IllegalArgumentException("无法计算表达式: " + expression, ex);
        }
    }

    /**
     * 解析表达式中科学函数的计算
     */
    protected double evaluateWithFunctions(String expression) {
        // 打印初始表达式
        System.out.println("初始表达式: " + expression);

        // 替换科学函数为 Java 表达式
        if (expression.contains("ln")) {
            expression = expression.replaceAll("ln\\(", "Math.log(");
        }
        if (expression.contains("log10")) {
            expression = expression.replaceAll("log10\\(", "Math.log10(");
        }

        // 打印修正后的表达式
        System.out.println("修正后的表达式: " + expression);

        String functionPattern = "Math\\.([a-zA-Z0-9]+)\\(([^)]+)\\)";
        Pattern pattern = Pattern.compile(functionPattern);
        Matcher matcher = pattern.matcher(expression);

        // 匹配并处理科学函数
        while (matcher.find()) {
            String fullMatch = matcher.group(0); // 完整的函数匹配，例如 Math.log10(100)
            String functionName = matcher.group(1); // 函数名，例如 log10
            String innerExpression = matcher.group(2); // 括号内的表达式，例如 100

            // 打印函数名和括号内的表达式
            System.out.println("当前函数: " + functionName);
            System.out.println("括号内的表达式: " + innerExpression);

            try {
                // 递归计算括号内的内容
                double innerValue = evaluateExpression(innerExpression);

                // 根据函数名调用相应的数学函数
                double result;
                switch (functionName) {
                    case "sin":
                        result = Math.sin(innerValue);
                        break;
                    case "cos":
                        result = Math.cos(innerValue);
                        break;
                    case "tan":
                        result = Math.tan(innerValue);
                        break;
                    case "log10":
                        result = Math.log10(innerValue);
                        break;
                    case "log":
                        result = Math.log(innerValue);
                        break;
                    case "sqrt":
                        result = Math.sqrt(innerValue);
                        break;
                    default:
                        throw new UnsupportedOperationException("不支持的函数: " + functionName);
                }

                // 针对接近零的结果归零处理
                if (Math.abs(result) < 1E-10) {
                    result = 0;
                }

                // 打印函数计算结果
                System.out.println("函数计算结果: " + result);

                // 替换计算结果到表达式
                expression = expression.replace(fullMatch, String.valueOf(result));
            } catch (Exception e) {
                System.err.println("计算错误: " + functionName + "(" + innerExpression + ")");
                throw e;
            }
        }

        // 打印处理后的最终表达式
        System.out.println("处理后的表达式: " + expression);

        // 没有更多科学函数，直接返回基础表达式计算结果
        return evaluateBasicExpression(expression);
    }




    /**
     * 解析基础表达式
     */
    private double evaluateBasicExpression(String expression) {
        Stack<Double> values = new Stack<>();
        Stack<Character> operators = new Stack<>();

        char[] tokens = expression.toCharArray();
        for (int i = 0; i < tokens.length; i++) {
            char c = tokens[i];

            if (c == ' ') continue;

            // 数字或小数点处理
            if (Character.isDigit(c) || c == '.') {
                StringBuilder buffer = new StringBuilder();
                while (i < tokens.length && (Character.isDigit(tokens[i]) || tokens[i] == '.')) {
                    buffer.append(tokens[i++]);
                }
                values.push(Double.parseDouble(buffer.toString()));
                i--; // 回退一位，避免跳过字符
            }
            // 左括号处理
            else if (c == '(') {
                operators.push(c);
            }
            // 右括号处理
            else if (c == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    values.push(applyOperator(operators.pop(), values.pop(), values.pop()));
                }
                if (!operators.isEmpty() && operators.peek() == '(') {
                    operators.pop();
                } else {
                    throw new IllegalArgumentException("括号不匹配");
                }
            }
            // 处理操作符，包括负号作为负数的处理
            else if (isOperator(c)) {
                // 检查负号是否表示负数
                if (c == '-' && (i == 0 || tokens[i - 1] == '(' || isOperator(tokens[i - 1]))) {
                    // 如果是表达式开头、括号后或操作符后，负号视为负数
                    StringBuilder buffer = new StringBuilder();
                    buffer.append(c); // 加上负号
                    i++;
                    while (i < tokens.length && (Character.isDigit(tokens[i]) || tokens[i] == '.')) {
                        buffer.append(tokens[i++]);
                    }
                    values.push(Double.parseDouble(buffer.toString()));
                    i--; // 回退一位，避免跳过字符
                } else {
                    // 正常处理减法或其他操作符
                    while (!operators.isEmpty() && precedence(c) <= precedence(operators.peek())) {
                        values.push(applyOperator(operators.pop(), values.pop(), values.pop()));
                    }
                    operators.push(c);
                }
            } else {
                throw new IllegalArgumentException("无效的字符: " + c);
            }
        }

        // 处理栈中剩余的操作符
        while (!operators.isEmpty()) {
            values.push(applyOperator(operators.pop(), values.pop(), values.pop()));
        }

        if (values.size() != 1) {
            throw new IllegalArgumentException("表达式无效: " + expression);
        }

        return values.pop();
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^' || c == '%';
    }

    private int precedence(char operator) {
        return switch (operator) {
            case '+', '-' -> 1;
            case '*', '/', '%' -> 2;
            case '^' -> 3;
            default -> -1;
        };
    }

    protected double applyOperator(char operator, double b, double a) {
        return switch (operator) {
            case '+' -> a + b;
            case '-' -> a - b;
            case '*' -> a * b;
            case '/' -> {
                if (b == 0) throw new ArithmeticException("除数不能为0");
                yield a / b;
            }
            case '%' -> {
                if (b == 0) throw new ArithmeticException("除数不能为0");
                yield a % b;
            }
            case '^' -> Math.pow(a, b);
            default -> throw new UnsupportedOperationException("不支持的操作符: " + operator);
        };
    }
}
