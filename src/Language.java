import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import ru.dron.Node;
import ru.dron.Parse;

import java.io.*;

import static org.objectweb.asm.Opcodes.*;


public class Language {

    static void printJpeg(Node node) throws IOException {
        FileWriter code = new FileWriter("graph.dot");
        code.write("digraph {\n");
        node.printDot(code);
        code.write("}");
        code.close();
        Runtime.getRuntime().exec("dot -Tps graph.dot -o graph.ps");
        Runtime.getRuntime().exec("evince graph.ps");
    }

    static void genBytecode(Node node) throws IOException {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        cw.visit(V1_6, ACC_PUBLIC + ACC_SUPER, "ownLangProg", null, "java/lang/Object", null);

        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
        mv.visitCode();
        node.genCode(mv);
        mv.visitInsn(RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();

        cw.visitEnd();
        //save bytecode into disk
        FileOutputStream out = new FileOutputStream("ownLangProg.class");
        out.write(cw.toByteArray());
        out.close();
    }

    public static void main(String[] args) throws IOException {

        Reader readStr = new StringReader("while(x < 50) {x = x + y; print(x);}");

        Parse parse = new Parse(readStr);
        Node result = parse.getG0();
        printJpeg(result);
        genBytecode(result);
    }


}
