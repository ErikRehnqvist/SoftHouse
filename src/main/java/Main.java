
import util.FileReader;

public class Main {
    private static final String FILE_PATH = "test-data.txt";

    static void main() {
        FileReader fileReader = new FileReader();
        fileReader.readFile(FILE_PATH);
    }
}
