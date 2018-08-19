import ru.dron.Node;
import ru.dron.Parse;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;


public class Language {
    public static void main(String [] args) throws IOException {

        Reader readStr = new StringReader("10.2 + 3.1 * (1 + 2)");

        Parse parse = new Parse(readStr);
        Node result = parse.getG0();

        System.out.println(result);
    }
}
