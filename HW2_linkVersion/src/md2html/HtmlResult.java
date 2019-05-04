package md2html;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

class HtmlResult {
    private BufferedWriter writer;

    HtmlResult(final String outputName) {
        try {
            writer = new BufferedWriter(new FileWriter(outputName));
        } catch (final IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Can't open output file!");
        }
    }

    void write(String text) {
        try {
            writer.write(text);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    void close() {
        try {
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
