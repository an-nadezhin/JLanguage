package ru.dron;

import java.io.IOException;
import java.io.StringReader;


public class Parse {

    LexAnalyzer LexAn;

    public Parse(StringReader readStr) throws IOException {
        LexAn = new LexAnalyzer(readStr);
        LexAn.nextLex();
    }

    public double getG0() throws IOException {
        return getE();
    }

    public double getE() throws IOException {
        double val = getT();

        while (LexAn.lex == LexAnalyzer.Lex.L_ADD || LexAn.lex == LexAnalyzer.Lex.L_SUB) {
            if(LexAn.lex == LexAnalyzer.Lex.L_ADD) {
                LexAn.nextLex();
                double val2 = getT();
                val += val2;
            }
            if(LexAn.lex == LexAnalyzer.Lex.L_SUB) {
                LexAn.nextLex();
                double val2 = getT();
                val -= val2;
            }
        }
        return val;
    }

    public double getT() throws IOException {
        double val = getP();

        while (LexAn.lex == LexAnalyzer.Lex.L_MUL || LexAn.lex == LexAnalyzer.Lex.L_DIV) {
            if (LexAn.lex == LexAnalyzer.Lex.L_MUL) {
                LexAn.nextLex();
                double val2 = getP();
                val *= val2;
            }
            if (LexAn.lex == LexAnalyzer.Lex.L_DIV) {
                LexAn.nextLex();
                double val2 = getP();
                assert (val2 == 0);
                val /= val2;
            }
        }
      return val;
    }

    public double getP() throws IOException {
        double val;
        if (LexAn.lex == LexAnalyzer.Lex.L_LEFT_PARENTHESIS) {
            LexAn.nextLex();
            val = getE();
            LexAn.expect(LexAnalyzer.Lex.L_RIGHT_PARENTHESIS);
        } else {
            val = getN();
        }
        return val;
    }

    public double getN() throws IOException {
        double value = Double.parseDouble(new String(LexAn.number));
        LexAn.nextLex();
        return value;
    }
}
