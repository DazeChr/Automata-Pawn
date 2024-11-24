package Automata;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class ProcOperaciones {
    public static List<Quadruple> quadruples = new ArrayList<>();
    public static String res = null;

    public static ArrayList<String> listaErrores2 = new ArrayList<>();
    static boolean errorSinFound2 = false;

    static Stack<String> operandStack = new Stack<>();
    static Map<String, Integer> variableValues = new HashMap<>();

    public static int Operaciones(char c) {
        switch (c) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
        }
        return 0;
    }

    public static String ConversionIntoPostFix(String expression) {
        String result = "";
        StackLIFO stack = new StackLIFO();
        String numberBuffer = "";
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (Character.isDigit(c) || Character.isLetter(c)) {
                numberBuffer += c;
                if (i == expression.length() - 1 || (!Character.isDigit(expression.charAt(i + 1)) && !Character.isLetter(expression.charAt(i + 1)))) {
                    result += numberBuffer + " ";
                    numberBuffer = "";
                }
            } else if (Operaciones(c) > 0) {
                while (!stack.empty() && Operaciones(stack.ontop()) >= Operaciones(c)) {
                    result += stack.pop() + " ";
                }
                stack.push(c);
            } else if (c == ')') {
                char x = stack.pop();
                while (x != '(') {
                    result += x + " ";
                    if (stack.empty()) {
                        errorSinFound2 = true;
                        listaErrores2.add("Error 521: Sobran parentesis o corchetes");
                        return "";
                    }
                    x = stack.pop();
                }
            } else if (c == '(') {
                stack.push(c);
            }
        }

        while (!stack.empty()) {
            char top = stack.pop();
            if (top == '(' || top == ')') {
                errorSinFound2 = true;
                listaErrores2.add("Error 521: Sobran parentesis o corchetes");
                return "";
            }
            result += top + " ";
        }

        result = result.trim();
        return result;
    }

    public static void generateQuadruples(String postfix) {
        try (BufferedReader reader = new BufferedReader(new FileReader("CodigoIntermedio.txt"))) {
            String line;
            boolean inCodeSegment = false;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Segmento Codigo")) {
                    inCodeSegment = true;
                } else if (line.startsWith("Segmento dato")) {
                    inCodeSegment = false;
                } else if (inCodeSegment) {
                    if (line.startsWith("asigna")) {
                        String[] parts = line.split(",");
                        if (parts.length == 4) {
                            String variable = parts[1];
                            int value = Integer.parseInt(parts[3]);
                            variableValues.put(variable, value);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] tokens = postfix.split(" ");
        
        for (String token : tokens) {
            if (token.length() == 1 && Operaciones(token.charAt(0)) > 0) {
                String arg2 = operandStack.pop();
                String arg1 = operandStack.pop();
                
                int val1 = isNumeric(arg1) ? Integer.parseInt(arg1) : variableValues.getOrDefault(arg1, 0);
                int val2 = isNumeric(arg2) ? Integer.parseInt(arg2) : variableValues.getOrDefault(arg2, 0);
                
                int resNum = 0;
                switch (token.charAt(0)) {
                    case '+':
                        resNum = val1 + val2;
                        break;
                    case '-':
                        resNum = val1 - val2;
                        break;
                    case '*':
                        resNum = val1 * val2;
                        break;
                    case '/':
                        resNum = val1 / val2;
                        break;
                }
                
                String result = String.valueOf(resNum);
                quadruples.add(new Quadruple(token, String.valueOf(val1), String.valueOf(val2), result));
                operandStack.push(result);
            } else {
                operandStack.push(token);
            }
        }
        res = operandStack.pop();
        printQuadruples();
    }

    private static void printQuadruples() {
        System.out.println("Cuadruplos:");
        for (Quadruple q : quadruples) {
            System.out.println(q);
        }
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

class Quadruple {
    private String operator;
    private String arg1;
    private String arg2;
    private String result;

    public Quadruple(String operator, String arg1, String arg2, String result) {
        this.operator = operator;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.result = result;
    }

    @Override
    public String toString() {
        return operator + ", " + arg1 + ", " + arg2 + ", " + result;
    }
}

class StackLIFO {
    private Nodo top;
    private int size;

    public StackLIFO() {
        top = null;
        size = 0;
    }

    public boolean empty() {
        return top == null;
    }

    public void push(char number) {
        Nodo newNode = new Nodo();
        newNode.setDato(number);
        newNode.setNext(top);
        top = newNode;
        size++;
    }

    public char pop() {
        char i = 0;
        try {
            i = top.getData();
            top = top.getNext();
            size--;
        } catch (NullPointerException ex) {
        }
        return i;
    }

    public char ontop() {
        char i = pop();
        push(i);
        return i;
    }

    public void pushString(String str) {
        Nodo newNode = new Nodo();
        newNode.setStringDato(str);
        newNode.setNext(top);
        top = newNode;
        size++;
    }

    public String popString() {
        String result = "";
        try {
            result = top.getStringDato();
            top = top.getNext();
            size--;
        } catch (NullPointerException ex) {
        }
        return result;
    }

    public int size() {
        return size;
    }
}