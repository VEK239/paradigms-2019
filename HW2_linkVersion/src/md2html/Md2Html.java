package md2html;

import java.io.IOException;

public class Md2Html implements AutoCloseable {

    private FileSource source;
    private HtmlResult result;
    private String currentLine;
    private StringBuilder line = new StringBuilder();
    private int posInLine;
    private int leftBound;

    Md2Html(final String inputName, final String outputName) {
        source = new FileSource(inputName);
        result = new HtmlResult(outputName);
    }

    public static void main(String[] args) {
        try (Md2Html converter = new Md2Html(args[0], args[1])) {
            converter.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        source.close();
        result.close();
    }

    private void tokenChecker(final String currentToken, final String tokenID) {
        appendSpan();
        int deletingPlace = line.length();
        line.append("<").append(tokenID).append(">");
        posInLine += currentToken.length();
        leftBound = posInLine;
        if (strongEmS(currentToken)) {
            line.append("</").append(tokenID).append(">");
            posInLine += currentToken.length();
            leftBound = posInLine;
        } else {
            line.delete(deletingPlace, deletingPlace + 2 + tokenID.length());
            line.insert(deletingPlace, currentToken);
        }
    }

    private boolean nextSymbolIs(final char symbol) {
        return currentLine.length() > posInLine + 1 && currentLine.charAt(posInLine + 1) == symbol;
    }

    private boolean equalsExpectedToken(final String currentToken) {
        if (currentLine.length() >= posInLine + currentToken.length()) {
            return currentLine.substring(posInLine, posInLine + currentToken.length()).equals(currentToken);
        } else {
            return false;
        }
    }

    private void linkChecker() {
        appendSpan();
        leftBound = ++posInLine;
        int linkInsertionPlace = line.length();
        strongEmS("]");
        posInLine += 2;
        leftBound = posInLine;
        while (currentLine.charAt(posInLine) != ')') {
            posInLine++;
        }
        line.insert(linkInsertionPlace, "'>")
                .insert(linkInsertionPlace, currentLine.substring(leftBound, posInLine))
                .insert(linkInsertionPlace, "<a href='");
        leftBound = ++posInLine;
        System.out.println(posInLine + leftBound);
        line.append("</a>");
    }

    private boolean strongEmS(final String expectedToken) {
        while (posInLine < currentLine.length() && !equalsExpectedToken(expectedToken)) {
            switch (currentLine.charAt(posInLine)) {
                case '*':
                    if (nextSymbolIs('*')) {
                        tokenChecker("**", "strong");
                    } else {
                        tokenChecker("*", "em");
                    }
                    break;
                case '-':
                    if (nextSymbolIs('-')) {
                        tokenChecker("--", "s");
                    } else {
                        posInLine++;
                    }
                    break;
                case '_':
                    if (nextSymbolIs('_')) {
                        tokenChecker("__", "strong");
                    } else {
                        tokenChecker("_", "em");
                    }
                    break;
                case '`':
                    tokenChecker("`", "code");
                    break;
                case '<':
                    //codeExpects(expectedToken, "&lt;");
                    appendSpan().append("&lt;");
                    leftBound = ++posInLine;
                    break;
                case '>':
                    //codeExpects(expectedToken, "&gt;");
                    appendSpan().append("&gt;");
                    leftBound = ++posInLine;
                    break;
                case '&':
                    //codeExpects(expectedToken, "&amp;");
                    appendSpan().append("&amp;");
                    leftBound = ++posInLine;
                    break;
                case '+':
                    if (nextSymbolIs('+')) {
                        tokenChecker("++", "u");
                    } else {
                        posInLine++;
                    }
                    break;
                case '\\':
                    if (nextSymbolIs('*') || nextSymbolIs('_')) {
                        appendSpan().append(currentLine.charAt(++posInLine));
                        leftBound = ++posInLine;
                    } else {
                        posInLine++;
                    }
                    break;
                case '[':
                    linkChecker();
                    break;
                default:
                    posInLine++;
            }
        }
        appendSpan();
        leftBound = posInLine;
        return equalsExpectedToken(expectedToken);
    }

    private StringBuilder appendSpan() {
        return line.append(currentLine.substring(leftBound, posInLine));
    }

    private void headers() {
        int hashCount = 0;
        while (currentLine.charAt(posInLine) == '#') {
            hashCount++;
            posInLine++;
        }

        if (posInLine != 0 && currentLine.charAt(posInLine) == ' ') {
            leftBound = ++posInLine;
            line.append("<h").append(hashCount).append(">");
            strongEmS("\0");
            line.append("</h").append(hashCount).append(">");
        } else {
            posInLine = 0;
            line.append("<p>");
            strongEmS("\0");
            line.append("</p>");
        }
    }

    void parse() throws IOException {
        while (source.nextLine() != null) {
            currentLine = source.getCurrentLine();
            posInLine = 0;
            leftBound = 0;
            line.setLength(0);

            headers();
            result.write(line.toString() + "\n");
        }
    }
}
