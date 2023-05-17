import java.io.*;
import java.util.*;

// This is the main class. Which takes inputs, generates files, patterns and calls for necessary algorithms.
public class InputProcessor {
    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        // creating a random bit generator  and write it to bits.html
        ArrayList<int[]> indexLists = new ArrayList<>();
        FileReader textReader;
        String pattern;
        Scanner scanner = new Scanner(System.in);
        System.out.print("Which type of texts do you want to search(enter 1(English Text) or 2(Bit Strings)): ");
        int decision;
        boolean keepAsking = true;
        do {
            decision = scanner.nextInt();
            if (decision == 1 || decision == 2) {
                keepAsking = false;

            } else {
                System.out.print("Wrong decision please only enter 1 or 2: ");
            }


        } while (keepAsking);


        if (decision == 1) {
            File wordFile = new File("wordlibrary.txt");
            File writeFile = new File("writeword.html");
            scanner.nextLine();
            System.out.print("Do you want to read a file or create a random text(if the choice is reading enter yes otherwise no): ");
            String choice = scanner.nextLine();
            //generate random pattern
            if (choice.equalsIgnoreCase("yes")) {
                System.out.print("Whats the name of the file that you want to read(ex:english.html or give its path(ex: English Text Samples/englishText1.html)): ");
                String fileName = scanner.nextLine();
                pattern = "slaves";
                File file = new File(fileName);
                FileReader reader = new FileReader(file);
                String html = readContent(reader);
                String bodyPart = extractBody(html);
                applyAlgorithms(bodyPart, pattern, indexLists);
                write_to_file(bodyPart, indexLists, writeFile);

            } else {
                HashMap<Integer, String> dictionary = generateDictionary(wordFile);
                pattern = dictionary.get((int) (Math.random() * dictionary.size()) - 1);
                generateRandomWordFile(writeFile, dictionary);
                System.out.println("Pattern is " + pattern);
                textReader = new FileReader(writeFile);
                String html = readContent(textReader);
                applyAlgorithms(html, pattern, indexLists);
                write_to_file(html, indexLists, writeFile);
                System.out.println("ENGLISH TEXT PROCESSED");
                System.out.println();
            }

        } else {
            scanner.nextLine();
            System.out.print("do you want to create a random bit text or read an existed one(if the choice is creating yes,otherwise no): ");
            String choice = scanner.nextLine();
            System.out.print("Please give the name of file  that you want to read(ex:Random Bit Text Samples/bit1.html): ");
            String fileName = scanner.nextLine();
            File bitFile = new File(fileName);
            File outputBit = new File("outputBit.html");

            if (choice.equalsIgnoreCase("yes")) {
                File randomBit = new File("randombit.txt");
                randomBitGenerator(randomBit);
                System.out.println("A RANDOM BIT FILE İS CREATED");
            }

            System.out.print("Please enter a pattern:(ex:101,1001,01101 etc.) ");
            pattern = scanner.nextLine();
            System.out.println("Pattern is " + pattern);
            textReader = new FileReader(bitFile);
            String html = readContent(textReader);
            String bodyPart = extractBody(html);
            applyAlgorithms(bodyPart, pattern, indexLists);
            write_to_file(bodyPart, indexLists, outputBit);

            System.out.println();
        }
        // print The Results

        // Tables:
        HashMap<Character, Integer> badSymbolTable = Tables.constructBadTable(pattern);
        HashMap<Integer, Integer> goodSuffixTable = Tables.constructGoodSuffix(pattern);
        System.out.println("BadTable:");
        Tables.PrintBadSymbolTable(badSymbolTable);
        System.out.println("GoodTable");
        Tables.PrintGoodSuffix(goodSuffixTable);


        // Brute force algorithm
        System.out.println("Brute Force Occurrence is " + BruteForce.numberOfOccurence);
        System.out.println("Brute Force Total comparison is " + BruteForce.numberOfComparison);
        System.out.println("Brute Force running time is " + BruteForce.runningTime + " milliseconds");


        // Horspool Algorithm
        System.out.println("HorsPool  Occurrence is " + Horspool.numberOfOccurence);
        System.out.println("HorsPool Total comparison is " + Horspool.numberOfComparison);
        System.out.println("HorsPool running time is " + Horspool.runningTime + " milliseconds");

        // Boyer Moore Algorithm
        System.out.println("Boyer Moore Occurrence is " + BoyerMoore.numberOfOccurrence);
        System.out.println("Boyer Moore comparison is " + BoyerMoore.numberOfComparison);
        System.out.println("Boyer Moore running time is " + BoyerMoore.runningTime + " milliseconds");


    }


    private static HashMap<Integer, String> generateDictionary(File file) {
        HashMap<Integer, String> map = new HashMap<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();

            int i = 0;
            while (line != null) {
                //System.out.println(line);
                map.put(i, line);
                line = reader.readLine();
                i++;
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }

    private static void generateRandomWordFile(File file, HashMap<Integer, String> map) throws IOException {
        FileWriter writer = new FileWriter(file);
        for (int i = 0; i < map.size() - 1; i++) {
            writer.write((map.get((int) (Math.random() * map.size()) - 1)) + "");
        }
        writer.close();
    }

    private static String generatePattern() {
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        int patternLen = random.nextInt(20);
        int bit;
        for (int i = 0; i < patternLen; i++) {
            bit = random.nextInt(2);
            str.append(bit);
        }
        return str.toString();

    }

    private static void applyAlgorithms(String bodyPart, String pattern, ArrayList<int[]> indexLists) {
        BruteForce.applyBruteForce(bodyPart, pattern, indexLists);
        HashMap<Character, Integer> horsPoolBadTable = Tables.constructBadTable(pattern);
        Horspool.applyHorspool(horsPoolBadTable, pattern, bodyPart);
        HashMap<Character, Integer> BoyerMooreBadTable = Tables.constructBadTable(pattern);
        HashMap<Integer, Integer> goodSuffixTable = Tables.constructGoodSuffix(pattern);
        BoyerMoore.applyBoyerMoore(BoyerMooreBadTable, goodSuffixTable, bodyPart, pattern);
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
                if (j == listLen) break;
                startingIndex = indexLists.get(j)[0];
                markSize = indexLists.get(j)[1];
                if (startingIndex == i) {
                    String pattern = getPattern(startingIndex, markSize, bodyPart);
                    writer.write("<mark>" + pattern + "</mark>");
                    i += markSize;
                    j++;
                } else {
                    writer.write(ch);
                    i++;


                }

            }
            writer.write(bodyPart.substring(i));
            writer.write("</body>\n" +
                    "</html>");
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static String getPattern(int startingIndex, int currentLen, String bodyPart) {
        StringBuilder str = new StringBuilder();
        int last = startingIndex + currentLen;
        for (int i = startingIndex; i < last; i++) {
            str.append(bodyPart.charAt(i));
        }
        return str.toString();

    }

    private static String extractBody(String result) {
        int firstIndex = result.indexOf("<body>") + 6;
        int lastIndex = result.indexOf("</body>");
        return result.substring(firstIndex, lastIndex);
    }

    private static String readContent(FileReader fileReader) {
        StringBuilder html = new StringBuilder();
        String result = "";
        try {

            BufferedReader bf = new BufferedReader(fileReader);
            String val;
            while ((val = bf.readLine()) != null) {
                html.append(val);
            }
            //   bf.close();
            // converting to string
            result = html.toString();

        } catch (Exception e) {
            System.out.println("Check the directory of the file!");
        }
        return result;
    }


    private static void randomBitGenerator(File file) throws IOException {
        FileWriter writer = new FileWriter(file);
        Random random = new Random();
        Scanner scanner = new Scanner(System.in);
        System.out.print("whats the size of input that you want to create randomly( 1-2-3-4-5..)mb: ");
        int size = scanner.nextInt();
        scanner.nextLine();
        int len = size * 1048376;
        int bit;
        writer.write(htmlBody());
        for (int i = 0; i < len; i++) {
            bit = random.nextInt(2);
            writer.write(String.valueOf(bit));
        }

        writer.write("</body>\n" +
                "</html>");
        writer.close();


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

}


