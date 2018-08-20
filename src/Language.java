import ru.dron.Node;
import ru.dron.Parse;

import java.io.*;


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

    public static void main(String[] args) throws IOException {

        Reader readStr = new StringReader("10 + 3 - 4*2/5 + 6");

        Parse parse = new Parse(readStr);
        Node result = parse.getG0();
        printJpeg(result);
    }


}
