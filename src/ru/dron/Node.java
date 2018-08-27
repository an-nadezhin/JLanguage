package ru.dron;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.objectweb.asm.Opcodes.*;

public class Node {

    public void printDot(FileWriter code) throws IOException {
    }

    public void printDotName(FileWriter code) throws IOException {
    }

    public void genCode(MethodVisitor mv) {
    }

    public static Map<String, Integer> map = new HashMap<String, Integer>();
}


class Variable extends Expression {
    private String name;

    public String getName() {
        return name;
    }

    public Variable(String varName) {
        name = varName;
    }

    public void printDotName(FileWriter code) throws IOException {
        code.write(name);
    }

    public void genCode(MethodVisitor mv) {
        mv.visitVarInsn(DLOAD, Node.map.get(name));
    }
}

class Statement extends Node {

    public void printDot(FileWriter code) throws IOException {
    }

    public void printDotName(FileWriter code) throws IOException {
    }

    public void genCode(MethodVisitor mv) {
    }

}

class If extends Statement {
    private ArrayList<Statement> statementsList;
    private Relation rel;

    public If(ArrayList<Statement> list, Relation relation) {
        statementsList = list;
        rel = relation;
    }

    public void printDotName(FileWriter code) throws IOException {
        code.write("if(");
        rel.printDotName(code);
        code.write(") {");
        Iterator<Statement> iterator = statementsList.iterator();
        while (iterator.hasNext() == true) {
            iterator.next().printDotName(code);
        }
        code.write("}");
    }

    public void printDot(FileWriter code) throws IOException {
        code.write("\"");
        printDotName(code);
        code.write("\" -> \"");
        rel.printDotName(code);
        code.write("\"\n");
        rel.printDot(code);

        Iterator<Statement> iterator = statementsList.iterator();
        while (iterator.hasNext() == true) {
            Statement stmt = iterator.next();
            code.write("\"");
            printDotName(code);
            code.write("\" -> \"");
            stmt.printDotName(code);
            code.write("\"\n");
            stmt.printDot(code);
        }
    }

    public void genCode(MethodVisitor mv) {
  //      mv.visitLdcInsn(4.0);
  //      mv.visitVarInsn(DSTORE, 1);
        Label end = new Label();
        rel.genCode(mv, end);
        Iterator<Statement> iterator = statementsList.iterator();
        while (iterator.hasNext()) {
            iterator.next().genCode(mv);
        }
        mv.visitLabel(end);
    }
}

class While extends Statement {
    private ArrayList<Statement> statementsList;
    private Relation rel;

    public While(ArrayList<Statement> list, Relation relation) {
        statementsList = list;
        rel = relation;
    }

    public void printDotName(FileWriter code) throws IOException {
        code.write("while(");
        rel.printDotName(code);
        code.write(") {");
        Iterator<Statement> iterator = statementsList.iterator();
        while (iterator.hasNext() == true) {
            iterator.next().printDotName(code);
        }
        code.write("}");
    }

    public void printDot(FileWriter code) throws IOException {
        code.write("\"");
        printDotName(code);
        code.write("\" -> \"");
        rel.printDotName(code);
        code.write("\"\n");
        rel.printDot(code);

        Iterator<Statement> iterator = statementsList.iterator();
        while (iterator.hasNext() == true) {
            Statement stmt = iterator.next();
            code.write("\"");
            printDotName(code);
            code.write("\" -> \"");
            stmt.printDotName(code);
            code.write("\"\n");
            stmt.printDot(code);
        }
    }

    public void genCode(MethodVisitor mv) {

        Label start = new Label();
        Label end = new Label();
        mv.visitLabel(start);
        rel.genCode(mv, end);
        Iterator<Statement> iterator = statementsList.iterator();
        while (iterator.hasNext()) {
            iterator.next().genCode(mv);
        }
        mv.visitJumpInsn(GOTO, start);
        mv.visitLabel(end);
    }
}

class Assign extends Statement {
    private Variable var;
    private Expression arg;

    public Assign(Variable newVar, Expression newArg) {
        var = newVar;
        arg = newArg;
    }

    public void printDotName(FileWriter code) throws IOException {
        code.write(var.getName());
        code.write(" = ");
        arg.printDotName(code);
        code.write(";");
    }

    public void printDot(FileWriter code) throws IOException {
        code.write("\"");
        printDotName(code);
        code.write("\" -> \"");
        var.printDotName(code);
        code.write("\"\n");
        code.write("\"");
        printDotName(code);
        code.write("\" -> \"");
        arg.printDotName(code);
        code.write("\"\n");
        arg.printDot(code);
    }

    public void genCode(MethodVisitor mv) {

   //     mv.visitLdcInsn(7.0);
   //     mv.visitVarInsn(DSTORE, 2);
        arg.genCode(mv);
        mv.visitVarInsn(DSTORE, Node.map.get(var.getName()));
    }
}

class Expression extends Node {

    public void printDot(FileWriter code) throws IOException {
    }

    public void printDotName(FileWriter code) throws IOException {
    }

    public void genCode(MethodVisitor mv) {
    }

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


class Relation extends Node {
    private Expression val1;
    private Expression val2;
    private LexAnalyzer.Lex Lex;

    public Relation(LexAnalyzer.Lex L, Expression value1, Expression value2) {
        val1 = value1;
        val2 = value2;
        Lex = L;
    }

    public void printDot(FileWriter code) throws IOException {
        code.write("\"");
        printDotName(code);
        code.write("\" -> \"");
        val1.printDotName(code);
        code.write("\"\n");
        val1.printDot(code);

        code.write("\"");
        printDotName(code);
        code.write("\" -> \"");
        val2.printDotName(code);
        code.write("\"\n");
        val2.printDot(code);
    }

    public void printDotName(FileWriter code) throws IOException {
        val1.printDotName(code);
        switch (Lex) {
            case L_EQ:
                code.write(" == ");
                break;
            case L_NE:
                code.write(" != ");
                break;
            case L_LE:
                code.write(" <= ");
                break;
            case L_LT:
                code.write(" < ");
                break;
            case L_GE:
                code.write(" >= ");
                break;
            case L_GT:
                code.write(" > ");
                break;
        }
        val2.printDotName(code);
    }

    public void genCode(MethodVisitor mv, Label end) {
        val1.genCode(mv);
        val2.genCode(mv);
        mv.visitInsn(DCMPG);
        switch (Lex) {
            case L_EQ:
                mv.visitJumpInsn(IFNE, end);
                break;
            case L_NE:
                mv.visitJumpInsn(IFEQ, end);
                break;
            case L_GE:
                mv.visitJumpInsn(IFLT, end);
                break;
            case L_GT:
                mv.visitJumpInsn(IFLE, end);
                break;
            case L_LE:
                mv.visitJumpInsn(IFGT, end);
                break;
            case L_LT:
                mv.visitJumpInsn(IFGE, end);
                break;
        }
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

class OperatorUn extends Expression {
    private LexAnalyzer.Lex Lex;
    private Expression arg;

    public OperatorUn(LexAnalyzer.Lex lex, Expression newArg) {
        Lex = lex;
        arg = newArg;
    }

    public void printDotName(FileWriter code) throws IOException {
        switch (Lex) {
            case L_SIN:
                code.write("sin(");
                arg.printDotName(code);
                code.write(")");
                break;
            case L_COS:
                code.write("cos(");
                arg.printDotName(code);
                code.write(")");
                break;
            case L_LN:
                code.write("ln(");
                arg.printDotName(code);
                code.write(")");
                break;
            case L_SQRT:
                code.write("sqrt(");
                arg.printDotName(code);
                code.write(")");
                break;
        }
    }

    public void printDot(FileWriter code) throws IOException {
        code.write("\"");
        printDotName(code);
        code.write("\" -> \"");
        arg.printDotName(code);
        code.write("\"\n");
        arg.printDot(code);
    }

    public void genCode(MethodVisitor mv) {
        arg.genCode(mv);
        switch (Lex) {
            case L_SIN:
                mv.visitMethodInsn(INVOKESTATIC, "java.lang.Math", "sin", "(D)D");
                break;
            case L_COS:
                mv.visitMethodInsn(INVOKESTATIC, "java.lang.Math", "cos", "(D)D");
                break;
            case L_LN:
                mv.visitMethodInsn(INVOKESTATIC, "java.lang.Math", "exp", "(D)D");
                break;
            case L_SQRT:
                mv.visitMethodInsn(INVOKESTATIC, "java.lang.Math", "sqrt", "(D)D");
                break;
        }
    }
}