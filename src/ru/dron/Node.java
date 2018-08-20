package ru.dron;

import org.objectweb.asm.MethodVisitor;

import java.io.FileWriter;
import java.io.IOException;

import static org.objectweb.asm.Opcodes.*;

public class Node {

    public void printDot(FileWriter code) throws IOException {}

    public void printDotName(FileWriter code) throws IOException{}

    public void genCode(MethodVisitor mv) {}

}

class Expression extends Node {

    public void printDot(FileWriter code) throws IOException {}

    public void printDotName(FileWriter code) throws IOException {}

    public void genCode(MethodVisitor mv) {}

}

class Number extends Expression {

    private double number;

    public void printDotName(FileWriter code) throws IOException {
        String num = Double.toString(number);
        code.write(num);
    }

    public void genCode(MethodVisitor mv) {
        mv.visitLdcInsn(number);

    }

    public Number(double val) {
        number = val;
    }
}


class OperatorBin extends Expression {

    private LexAnalyzer.Lex Lex;
    private Expression Right;
    private Expression Left;

    public OperatorBin(LexAnalyzer.Lex L, Expression first, Expression second) {
        Lex = L;
        Left = first;
        Right = second;
    }

    public void genCode(MethodVisitor mv) {
        Left.genCode(mv);
        Right.genCode(mv);
        switch (Lex) {
            case L_ADD:
                mv.visitInsn(DADD);
                break;
            case L_SUB:
                mv.visitInsn(DSUB);
                break;
            case L_MUL:
                mv.visitInsn(DMUL);
                break;
            case L_DIV:
                mv.visitInsn(DDIV);
                break;
        }
    }

    public void printDot(FileWriter code) throws IOException {

        code.write("\"");
        printDotName(code);
        code.write("\" -> \"");
        Left.printDotName(code);
        code.write("\"\n");
        Left.printDot(code);

        code.write("\"");
        printDotName(code);
        code.write("\" -> \"");
        Right.printDotName(code);
        code.write("\"\n");
        Right.printDot(code);

    }

    public void printDotName(FileWriter code) throws IOException {
        switch (Lex) {
            case L_ADD:
                Left.printDotName(code);
                code.write(" + ");
                Right.printDotName(code);
                break;
            case L_SUB:
                Left.printDotName(code);
                code.write(" - ");
                Right.printDotName(code);
                break;
            case L_MUL:
                Left.printDotName(code);
                code.write(" * ");
                Right.printDotName(code);
                break;
            case L_DIV:
                Left.printDotName(code);
                code.write(" / ");
                Right.printDotName(code);
                break;
        }
    }
}