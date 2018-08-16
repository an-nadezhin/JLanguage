package ru.dron;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;

public class LexAnalyzer {

    private static int ch;
    private static LineNumberReader sym;

    public enum Lex {
        L_ADD,
        L_SUB,
        L_MUL,
        L_DIV,
        L_CONST,
        L_NOT_IMPLEMENTED
    }

    public static Lex lex;
    public static StringBuilder number;

    public LexAnalyzer(StringReader name) {
        sym = new LineNumberReader(name);
        number = new StringBuilder();
    }

    public static Lex nextLex() throws IOException {

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
        }

        if (ch >= '0' && ch <= '9') {
            number.setLength(0);
            while (ch >= '0' && ch <= '9') {
                number.append(ch - '0');
                sym.mark(1);
                ch = sym.read();
                if (ch == '.') {
                    number.append('.');
                    ch = sym.read();
                }
                skipSp();
            }
            sym.reset();
            return lex = Lex.L_CONST;
        }
        return lex = Lex.L_NOT_IMPLEMENTED;
    }

    private static void skipSp() throws IOException {
        while (ch == ' ' || ch == '\n' || ch == '\t') {
            ch = sym.read();
        }
    }
}
