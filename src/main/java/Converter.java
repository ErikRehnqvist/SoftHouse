
import util.FileReader;

public class Converter {
    private static final String FILE_PATH = "test-data.txt";

    static void main() {
        FileReader fileReader = new FileReader();
        fileReader.readFile(FILE_PATH);
    }
}
