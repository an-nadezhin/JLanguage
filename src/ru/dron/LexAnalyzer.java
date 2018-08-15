package ru.dron;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;

public class LexAnalyzer {
    enum Lex {L_ADD, L_SUB, L_MUL, L_DIV, L_CONST, L_NOT_IMPLEMENTED}
    static int symbol;
    static Lex lex;

    static StringBuilder number = new StringBuilder();
    static LineNumberReader sym;

    public LexAnalyzer(StringReader name) {
        sym = new LineNumberReader(name);
    }

    public static Lex nextLex() throws IOException {
        symbol = sym.read();
        skipSp();
        switch (symbol) {
            case '+':
                return lex = Lex.L_ADD;
            case '-':
                return lex = Lex.L_SUB;
            case '*':
                return lex = Lex.L_MUL;
            case ':':
                return lex = Lex.L_DIV;
        }

        if(symbol >= '0' && symbol <= '9') {
            number.setLength(0);
            while (symbol >= '0' && symbol <= '9') {

                number.append(symbol - '0');
                sym.mark(1);
                symbol = sym.read();
                if(symbol == '.') {
                    number.append('.');
                    symbol = sym.read();
                }
                skipSp();
            }
            sym.reset();
            return Lex.L_CONST;
        }

        return Lex.L_NOT_IMPLEMENTED;
    }

    static void skipSp() throws IOException {
        while(symbol == ' ' || symbol == '\n' || symbol == '\t') {
            symbol = sym.read();
        }
    }
}
