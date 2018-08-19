package ru.dron;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;

public class LexAnalyzer {

    private int ch;
    private LineNumberReader sym;

    public enum Lex {
        L_ADD,
        L_SUB,
        L_MUL,
        L_DIV,
        L_CONST,
        L_LEFT_PARENTHESIS,
        L_RIGHT_PARENTHESIS,
        L_NOT_IMPLEMENTED
    }

    public Lex lex;
    public StringBuilder number;

    public LexAnalyzer(Reader name) {
        sym = new LineNumberReader(name);
        number = new StringBuilder();
    }

    public Lex nextLex() throws IOException {

        ch = sym.read();
        skipSp();
        switch (ch) {
            case '+':
                return lex = Lex.L_ADD;
            case '-':
                return lex = Lex.L_SUB;
            case '*':
                return lex = Lex.L_MUL;
            case '/':
                return lex = Lex.L_DIV;
            case '(':
                return lex = Lex.L_LEFT_PARENTHESIS;
            case ')':
                return lex = Lex.L_RIGHT_PARENTHESIS;
        }

        if (ch >= '0' && ch <= '9') {
            number.setLength(0);
            boolean dot = false;
            while (ch >= '0' && ch <= '9') {
                number.append(ch - '0');
                sym.mark(1);
                ch = sym.read();
                if (ch == '.') {
                    Error(dot == true, "Second dot in number");
                    number.append('.');
                    ch = sym.read();
                    dot = true;
                }
            }
            sym.reset();
            return lex = Lex.L_CONST;
        }
        return lex = Lex.L_NOT_IMPLEMENTED;
    }

    private void skipSp() throws IOException {
        while (ch == ' ' || ch == '\n' || ch == '\t') {
            ch = sym.read();
        }
    }
    public void expect(Lex l) throws IOException, RuntimeException {
        Error(l != lex, "ERROR : expect lex not found!");
        nextLex();
    }

    private void Error(boolean condition, String messege) {
        if(condition == true) throw new RuntimeException(messege);
    }
}
