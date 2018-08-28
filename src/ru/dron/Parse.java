package ru.dron;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;


public class Parse {

    LexAnalyzer LexAn;

    public Parse(Reader readStr) throws IOException {
        LexAn = new LexAnalyzer(readStr);
        LexAn.nextLex();
    }

    public Statement getG0() throws IOException {
        Node.map.put("x", Node.map.size() + 1);
   //     Node.map.put("y", Node.map.size() + 1);

        return getS();
    }

    public Statement getS() throws IOException {
        Relation relation;
        Expression val;
        ArrayList<Statement> stmt = new ArrayList<Statement>();
        switch (LexAn.lex) {
            case L_WHILE:
                LexAn.nextLex();
                LexAn.expect(LexAnalyzer.Lex.L_LEFT_PARENTHESIS);
                relation = getR();
                LexAn.expect(LexAnalyzer.Lex.L_RIGHT_PARENTHESIS);
                LexAn.expect(LexAnalyzer.Lex.L_LEFT_CURLY_BRACE);
                while (LexAn.lex != LexAnalyzer.Lex.L_RIGHT_CURLY_BRACE) {
                    stmt.add(getS());
                }
                LexAn.expect(LexAnalyzer.Lex.L_RIGHT_CURLY_BRACE);
                return new While(stmt, relation);
            case L_IF:
                LexAn.nextLex();
                LexAn.expect(LexAnalyzer.Lex.L_LEFT_PARENTHESIS);
                relation = getR();
                LexAn.expect(LexAnalyzer.Lex.L_RIGHT_PARENTHESIS);
                LexAn.expect(LexAnalyzer.Lex.L_LEFT_CURLY_BRACE);
                while (LexAn.lex != LexAnalyzer.Lex.L_RIGHT_CURLY_BRACE) {
                    stmt.add(getS());
                }
                LexAn.expect(LexAnalyzer.Lex.L_RIGHT_CURLY_BRACE);
                return new If(stmt, relation);
            case L_RETURN:
                LexAn.nextLex();
                val = getE();
                LexAn.expect(LexAnalyzer.Lex.L_SEMI_COLON);
                return new Return(val);
            case L_PRINT:
                LexAn.nextLex();
                LexAn.expect(LexAnalyzer.Lex.L_LEFT_PARENTHESIS);
                val = getE();
                LexAn.expect(LexAnalyzer.Lex.L_RIGHT_PARENTHESIS);
                LexAn.expect(LexAnalyzer.Lex.L_SEMI_COLON);
                return new Print(val);
        }

        if (Node.map.containsKey(LexAn.Id.toString()) == false) {
            error("No such variable!");
        }
        Variable var = new Variable(LexAn.Id.toString());
        LexAn.nextLex();
        LexAn.expect(LexAnalyzer.Lex.L_ASSIGN);
        val = getE();
        LexAn.expect(LexAnalyzer.Lex.L_SEMI_COLON);
        return new Assign(var, val);
    }

    public Relation getR() throws IOException {
        Expression val1 = getE();
        LexAnalyzer.Lex op = LexAn.lex;
        LexAn.nextLex();
        Expression val2 = getE();
        switch (op) {
            case L_EQ:
            case L_NE:
            case L_GE:
            case L_GT:
            case L_LE:
            case L_LT:
                break;
            default:
                error("no such relation");
        }
        return new Relation(op, val1, val2);
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
        switch (LexAn.lex) {
            case L_CONST:
                double value = Double.parseDouble(new String(LexAn.number));
                LexAn.nextLex();
                return new Number(value);
            case L_LEFT_PARENTHESIS:
                LexAn.nextLex();
                val = getE();
                LexAn.expect(LexAnalyzer.Lex.L_RIGHT_PARENTHESIS);
                return val;
            case L_SIN:
                LexAn.nextLex();
                LexAn.expect(LexAnalyzer.Lex.L_LEFT_PARENTHESIS);
                val = getE();
                LexAn.expect(LexAnalyzer.Lex.L_RIGHT_PARENTHESIS);
                return new OperatorUn(LexAnalyzer.Lex.L_SIN, val);
            case L_COS:
                LexAn.nextLex();
                LexAn.expect(LexAnalyzer.Lex.L_LEFT_PARENTHESIS);
                val = getE();
                LexAn.expect(LexAnalyzer.Lex.L_RIGHT_PARENTHESIS);
                return new OperatorUn(LexAnalyzer.Lex.L_COS, val);
            case L_SQRT:
                LexAn.nextLex();
                LexAn.expect(LexAnalyzer.Lex.L_LEFT_PARENTHESIS);
                val = getE();
                LexAn.expect(LexAnalyzer.Lex.L_RIGHT_PARENTHESIS);
                return new OperatorUn(LexAnalyzer.Lex.L_SQRT, val);
            case L_LN:
                LexAn.nextLex();
                LexAn.expect(LexAnalyzer.Lex.L_LEFT_PARENTHESIS);
                val = getE();
                LexAn.expect(LexAnalyzer.Lex.L_RIGHT_PARENTHESIS);
                return new OperatorUn(LexAnalyzer.Lex.L_LN, val);
            case L_ID:
                if (Node.map.containsKey(LexAn.Id.toString()) == true) {
                    LexAn.nextLex();
                    return new Variable(LexAn.Id.toString());
                } else {
                    error("No such variable!");
                }
            default:
                error("invalid expression");
        }
        return null;
    }

    public void error(String message) {
        System.out.println(message);
        System.exit(10);
    }
}