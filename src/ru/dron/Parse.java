package ru.dron;

import java.io.IOException;
import java.io.Reader;


public class Parse {

    LexAnalyzer LexAn;

    public Parse(Reader readStr) throws IOException {
        LexAn = new LexAnalyzer(readStr);
        LexAn.nextLex();
    }

    public Expression getG0() throws IOException {
        return getE();
    }

    public Expression getE() throws IOException {
        Expression val = getT();

        while (LexAn.lex == LexAnalyzer.Lex.L_ADD || LexAn.lex == LexAnalyzer.Lex.L_SUB) {
            if (LexAn.lex == LexAnalyzer.Lex.L_ADD) {
                LexAn.nextLex();
                Expression val2 = getT();
                val = new OperatorBin(LexAnalyzer.Lex.L_ADD, val, val2);
            }
            if (LexAn.lex == LexAnalyzer.Lex.L_SUB) {
                LexAn.nextLex();
                Expression val2 = getT();
                val = new OperatorBin(LexAnalyzer.Lex.L_SUB, val, val2);
            }
        }
        return val;
    }

    public Expression getT() throws IOException {
        Expression val = getP();

        while (LexAn.lex == LexAnalyzer.Lex.L_MUL || LexAn.lex == LexAnalyzer.Lex.L_DIV) {
            if (LexAn.lex == LexAnalyzer.Lex.L_MUL) {
                LexAn.nextLex();
                Expression val2 = getP();
                val = new OperatorBin(LexAnalyzer.Lex.L_MUL, val, val2);
            }
            if (LexAn.lex == LexAnalyzer.Lex.L_DIV) {
                LexAn.nextLex();
                Expression val2 = getP();
                val = new OperatorBin(LexAnalyzer.Lex.L_DIV, val, val2);
            }
        }
        return val;
    }

    public Expression getP() throws IOException {
        Expression val;
        if (LexAn.lex == LexAnalyzer.Lex.L_LEFT_PARENTHESIS) {
            LexAn.nextLex();
            val = getE();
            LexAn.expect(LexAnalyzer.Lex.L_RIGHT_PARENTHESIS);
        } else {
            double value = Double.parseDouble(new String(LexAn.number));
            LexAn.nextLex();
            val = new Number(value);
        }
        return val;
    }
}
