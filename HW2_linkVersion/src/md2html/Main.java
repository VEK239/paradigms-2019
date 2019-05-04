package md2html;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try (Md2Html converter = new Md2Html("input.txt", "output.txt")) {
            converter.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
