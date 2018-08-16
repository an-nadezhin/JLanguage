package ru.dron;

import java.io.IOException;
import java.io.StringReader;


public class Parse {

    public static double getN() throws IOException {
        double value = Double.parseDouble(new String(LexAnalyzer.number));
        LexAnalyzer.nextLex();
        return value;
    }

    public static double getE() throws IOException {
        double val1 = getT();

        if (LexAnalyzer.lex == LexAnalyzer.Lex.L_ADD) {
            LexAnalyzer.nextLex();
            double val2 = getE();
            val1 += val2;
        }
        if(LexAnalyzer.lex == LexAnalyzer.Lex.L_SUB) {
            LexAnalyzer.nextLex();
            double val2 = getE();
            val1 -= val2;
        }
        return val1;
    }

    public static double getT() throws IOException {
        double val1 = getN();

        if (LexAnalyzer.lex == LexAnalyzer.Lex.L_MUL) {
            LexAnalyzer.nextLex();
            double val2 = getT();
            val1 *= val2;
        }
        if(LexAnalyzer.lex == LexAnalyzer.Lex.L_DIV) {
            LexAnalyzer.nextLex();
            double val2 = getT();
            val1 /= val2;
        }
      return val1;
    }

    public static double getG0(StringReader readStr) throws IOException{
        new LexAnalyzer(readStr);
        LexAnalyzer.nextLex();
        return getE();
    }
}
