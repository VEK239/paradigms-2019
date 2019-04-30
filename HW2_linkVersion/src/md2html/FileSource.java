package md2html;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

class FileSource {
    private String currentLine;
    private final BufferedReader reader;

    FileSource(final String inputName) {
        try {
            reader = new BufferedReader(new FileReader(inputName, StandardCharsets.UTF_8));
        } catch (final IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Can't open input file!");
        }
    }

    String getCurrentLine() {
        return currentLine;
    }

    private String getNotEmptyLine() throws IOException {
        String temp;
        do {
            temp = reader.readLine();
        } while (temp != null && temp.equals(""));
        return temp;
    }

    String nextLine() throws IOException {
        try {
            StringBuilder tempPart = new StringBuilder(getNotEmptyLine());
            String tempLine = reader.readLine();
            while (tempLine != null && !tempLine.equals("")) {
                tempPart.append('\n');
                tempPart.append(tempLine);
                tempLine = reader.readLine();
            }
            currentLine = tempPart.toString();
            return currentLine;
        } catch (NullPointerException e) {
            return null;
        }
    }

    void close() {
        try {
            reader.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
