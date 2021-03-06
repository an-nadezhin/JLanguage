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
        L_LEFT_CURLY_BRACE,
        L_RIGHT_CURLY_BRACE,
        L_SEMI_COLON,
        L_EQ, // ==
        L_NE, // NOT EQUAL (!=)
        L_GE, // GREATER OR EQUAL (>=)
        L_GT, // GREATER THAN (>)
        L_LE, // LESS THAN OR EQUAL TO (<=)
        L_LT, // LESS THAN (<)
        L_ASSIGN, // =
        L_ID,
        L_WHILE,
        L_IF,
        L_RETURN,
        L_DOUBLE,
        L_SIN,
        L_COS,
        L_LN,
        L_SQRT,
        L_PRINT,
        L_NOT_IMPLEMENTED
    }


    public Lex lex;
    public StringBuilder number;
    public StringBuilder Id;

    public LexAnalyzer(Reader name) {
        sym = new LineNumberReader(name);
        number = new StringBuilder();
        Id = new StringBuilder();
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
            case '{':
                return lex = Lex.L_LEFT_CURLY_BRACE;
            case '}':
                return lex = Lex.L_RIGHT_CURLY_BRACE;
            case ';':
                return lex = Lex.L_SEMI_COLON;
            case '=':
                sym.mark(1);
                ch = sym.read();
                if (ch == '=') {
                    return lex = Lex.L_EQ;
                } else {
                    sym.reset();
                    return lex = Lex.L_ASSIGN;
                }
            case '>':
                sym.mark(1);
                ch = sym.read();
                if (ch == '=') {
                    return lex = Lex.L_GE;
                } else {
                    sym.reset();
                    return lex = Lex.L_GT;
                }
            case '<':
                sym.mark(1);
                ch = sym.read();
                if (ch == '=') {
                    return lex = Lex.L_LE;
                } else {
                    sym.reset();
                    return lex = Lex.L_LT;
                }
            case '!':
                ch = sym.read();
                if (ch == '=') {
                    return lex = Lex.L_NE;
                } else {
                    Error(ch == '=', "Not right symbol");
                }
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

        if (Character.isLetter(ch)) {
            Id.setLength(0);
            while (Character.isLetter(ch) || Character.isDigit(ch)) {
                Id.append(Character.toChars(ch));
                sym.mark(1);
                ch = sym.read();
            }
            sym.reset();
        }

        if (Id.toString().equals("while")) return lex = Lex.L_WHILE;
        if (Id.toString().equals("if")) return lex = Lex.L_IF;
        if (Id.toString().equals("return")) return lex = Lex.L_RETURN;
        if (Id.toString().equals("double")) return lex = Lex.L_DOUBLE;
        if (Id.toString().equals("sin")) return lex = Lex.L_SIN;
        if (Id.toString().equals("cos")) return lex = Lex.L_COS;
        if (Id.toString().equals("sqrt")) return lex = Lex.L_SQRT;
        if (Id.toString().equals("ln")) return lex = Lex.L_LN;
        if (Id.toString().equals("print")) return lex = Lex.L_PRINT;

        return lex = Lex.L_ID;
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
        if (condition == true) throw new RuntimeException(messege);
    }
}
