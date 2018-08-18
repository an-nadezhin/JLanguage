import ru.dron.*;

import java.io.IOException;
import java.io.StringReader;



public class Language {
    public static void main(String [] args) throws IOException {

        StringReader readStr = new StringReader("35*10/5 - ((10*3*(1 + 2) + 10) + 30*3*4/12)");

        Parse parse = new Parse(readStr);
        double result = parse.getG0();

        System.out.println(result);
    }
}
