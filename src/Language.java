import ru.dron.LexAnalyzer;

import java.io.IOException;
import java.io.StringReader;

public class Language {

    static double getN(){
        return 0;
    }

    public static void main(String [] args) throws IOException {
        StringReader readStr = new StringReader("35.8 + 4");
        new LexAnalyzer(readStr);
        LexAnalyzer.nextLex();
        getN();
    }
}
