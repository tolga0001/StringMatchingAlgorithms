import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class BruteForce {
    public static void main(String[] args) {
        String text = "WHICH_FINALLY_HALTS. _ _ AT_THAT POINT";
        String pattern = "AT_THAT";
        ArrayList<int[]> lists = new ArrayList<>();
        applyBruteForce(text, pattern, lists);
        for (int[] list : lists) {
            System.out.print(Arrays.toString(list));
        }
        System.out.println();
        System.out.println("Text is " + text);
        System.out.println("pattern is " + pattern);
        File file = new File("sample.html");
        write_to_file(text, lists, file);
        System.out.println("occurrence is "+numberOfOccurence);
        System.out.println("comparison is "+numberOfComparison);
    }

    private static void write_to_file(String bodyPart, ArrayList<int[]> indexLists, File bitFile) {
        try {
            FileWriter writer = new FileWriter(bitFile);
            int startingIndex;
            int markSize;
            int j = 0;
            char ch;
            writer.write(htmlBody());
            int i = 0;
            int listLen = indexLists.size();
            while (i < bodyPart.length()) {
                ch = bodyPart.charAt(i);
              //  System.out.println("index: " + i);
                startingIndex = indexLists.get(j)[0];
               // System.out.println("starting index: " + startingIndex);
                markSize = indexLists.get(j)[1];
                //System.out.println("size is " + markSize);
                if (startingIndex == i) {
                    String pattern = getPattern(startingIndex, markSize, bodyPart);
                    writer.write("<mark>" + pattern + "</mark>");
                    // System.out.print("<mark>" + pattern + "</mark>");
                    i += markSize;
                    j++;
                } else {
                    writer.write(ch);
                    i++;
                    //  System.out.print(ch);
                }
                if (j == listLen) break;
            }
            writer.write(bodyPart.substring(i));
            writer.write("</body>\n" +
                    "</html>");
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static String htmlBody() {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Document</title>\n" +
                "</head><style>body {word-wrap: break-word;}</style><body>";
    }

    private static String getPattern(int startingIndex, int currentLen, String bodyPart) {
        StringBuilder str = new StringBuilder();
        int last = startingIndex + currentLen;
        for (int i = startingIndex; i < last; i++) {
            str.append(bodyPart.charAt(i));
        }
        return str.toString();

    }

    static int numberOfOccurence = 0;
    static int numberOfComparison = 0;
    static long runningTime;

    public static void applyBruteForce(String bodyPart, String pattern, ArrayList<int[]> indexLists) {
        // GLOBAL OLARAK TANIMLA
        long start = System.nanoTime();
        int lengthOfResult = bodyPart.length();
        int lengthOfPattern = pattern.length();
        ArrayList<Integer> startingIndexes = new ArrayList<>();
        for (int i = 0; i <= lengthOfResult - lengthOfPattern; i++) {

            int j;

            for (j = 0; j < lengthOfPattern; j++) {

                if (pattern.charAt(j) != bodyPart.charAt(j + i)) {
                    numberOfComparison++;
                    break;
                }
                numberOfComparison++;
            }

            // number of pattern occurrence in the text
            if (j == lengthOfPattern) {
                //indexList.add(i);
                if (numberOfOccurence == 0) {
                    indexLists.add(new int[]{i, lengthOfPattern});
                    startingIndexes.add(i);
                } else {
                    int last = indexLists.size() - 1;
                    int[] lastElement = indexLists.get(last);
                    int prevIndex = lastElement[0];
                    if (i - prevIndex < lastElement[1]) {
                        lastElement[1] += i - startingIndexes.get(startingIndexes.size() - 1);
                    } else {
                        indexLists.add(new int[]{i, lengthOfPattern});
                    }
                    startingIndexes.add(i);
                }
                numberOfOccurence++;
            }

        }
        // index listi update etmemiz gerekiyor.
        long end = System.nanoTime();
        runningTime = (long) ((end - start) / Math.pow(10, 6));

    }
}
