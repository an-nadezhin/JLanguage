import ru.dron.*;

import java.io.IOException;
import java.io.StringReader;

//import static ru.dron.LexAnalyzer.nextLex;

public class Language {
    public static void main(String [] args) throws IOException {

        StringReader readStr = new StringReader("35 - 6*3*2");

        double result = Parse.getG0(readStr);

        System.out.println(result);
    }
}
