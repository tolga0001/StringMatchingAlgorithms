import java.util.HashMap;


// This is Boyer Moore Algorithm class,
public class BoyerMoore {
    public static void main(String[] args) {
        String text = "WHICH_FINALLY_HALTS. _ _ AT_THAT POINT";
        String pattern = "AT_THAT";

        long start = System.currentTimeMillis();
        applyBoyerMoore(Tables.constructBadTable(pattern),Tables.constructGoodSuffix(pattern),text,pattern);
        long end = System.currentTimeMillis();

        System.out.println("Total occurrence is " + numberOfOccurrence);
        System.out.println("total comparison is " + numberOfComparison);
    }

    static int numberOfOccurrence;
    static int numberOfComparison;
    static long runningTime;


    //  This function is called from the InputProcessor class with giving necessary inputs like
    // bad symbol, good suffix tables, text and the pattern.
    public static void applyBoyerMoore(HashMap<Character, Integer> badSymbol, HashMap<Integer, Integer> goodSuffix, String text, String pattern) {
        long start = System.nanoTime();
        int texLen = text.length();
        int patLen = pattern.length();

        int texIndex = patLen - 1;
        int patIndex = patLen - 1;
        while (texIndex < texLen) {
            if (pattern.charAt(patIndex) == text.charAt(texIndex)) {

                // This is the case where all chars matches
                if (patIndex == 0) {
                    // TODO: match
                    numberOfOccurrence++;
                    texIndex += patLen - 1;
                    texIndex += goodSuffix.get(patLen);
                    patIndex = patLen - 1;
                } else {
                    patIndex--;
                    texIndex--;
                }

            }
            // No matches
            else {
                // That means first index is not a match, so we will shift using bad symbol method
                if (patIndex == patLen - 1) {
                    if (badSymbol.get(text.charAt(texIndex)) != null) {
                        texIndex += badSymbol.get(text.charAt(texIndex));
                    } else {
                        texIndex += badSymbol.get('*');
                    }

                } else {
                    // This is the case where at least one character matches but not all.
                    // So we will shift using max of d1 or d2, where d1 is obtained using bad symbol
                    // d2 is obtained using good suffix table.
                    int k = (patLen - 1) - patIndex;
                    int t1 = (badSymbol.get(text.charAt(texIndex)) != null) ? badSymbol.get(text.charAt(texIndex)) : badSymbol.get('*');
                    int d1 = Math.max(t1 - k, 1);
                    int d2 = goodSuffix.get(k);
                    texIndex += k;
                    texIndex += Math.max(d1, d2);
                    patIndex = (patLen - 1);
                }

            }
            numberOfComparison++;
        }
        System.out.println("finished searching");
        long end = System.nanoTime();
        runningTime = (long) ((end - start) / Math.pow(10, 6));

    }
}


