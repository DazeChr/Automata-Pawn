package Automata;

public class Nodo {
    //* Atributos
    String lexema; //* Se usa para los caracteres
    int token; //* Token de la matriz que representa el lexema
    int linea; //* linea de codigo
    Nodo next = null; //* referencia al siguiente

    public Nodo(String lexema, int token, int linea) {
        this.lexema = lexema;
        this.token = token;
        this.linea = linea;
    }

    private String stringDato;
    char data;

    public Nodo() {
        return;
    }

    public void setDato(char newData) {
        data = newData;
    }

    public void setNext(Nodo newNext) {
        next = newNext;
    }

    public char getData() {
        return data;
    }

    public Nodo getNext() {
        return next;
    }

    public void displayNode() {
        System.out.print(data);
    }

    public String getStringDato() {
        return stringDato;
    }

    public void setStringDato(String stringDato) {
        this.stringDato = stringDato;
    }
}