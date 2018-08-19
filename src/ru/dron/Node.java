package ru.dron;

public class Node {

}

class Expression extends Node {

}

class Number extends Expression {
    private double number;
    public Number(double val) {
        number = val;
    }
}
class OpertorBin extends Expression {

    private LexAnalyzer.Lex Lex;
    private Expression right;
    private Expression left;

    public OpertorBin(LexAnalyzer.Lex L, Expression first, Expression second) {
        Lex = L;
        left = first;
        right = second;
    }
}