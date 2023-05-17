import java.util.HashMap;

public class Horspool {
    static int numberOfOccurence;
    static int numberOfComparison = 0;

    static long runningTime;

    public static void main(String[] args) {
        String text = "10010101010011001";
        String pattern = "101";


        long start = System.currentTimeMillis();
        applyHorspool(Tables.constructBadTable(pattern),pattern,text);
        long end = System.currentTimeMillis();
        long timeOfHorspool = end - start;
        System.out.println("HorsPool time: " + timeOfHorspool);
        System.out.println("Total comp is " + numberOfComparison);
        System.out.println("occurance : " + numberOfOccurence);
    }

    public static void applyHorspool(HashMap<Character, Integer> badTable, String key, String text) {
        long start = System.nanoTime();
        char lastC = key.charAt(key.length() - 1);
        int length = key.length();
        int index = length - 1;
        int lastIndex = index;
        if (length> text.length()){
            numberOfComparison=0;
            return;
        }

        do {
            if (lastC == text.charAt(index)) {
                lastIndex = index;
                numberOfComparison++;
                boolean check = checkFullMatch(index, key, text);
                index += badTable.get(text.charAt(lastIndex));

            } else {
                if (badTable.get(text.charAt(index)) == null)
                    index += badTable.get('*');
                else
                    index += badTable.get(text.charAt(index));

                numberOfComparison++;
            }
        } while (index <= text.length() - 1);

        long end = System.nanoTime();
        runningTime= (long) ((end-start)/Math.pow(10,6));
    }

    private static boolean checkFullMatch(int index, String key, String text) {
        boolean check = true;
        int k = key.length() - 2;
        for (int i = index -1; i >= 0; i--) {
            if (key.charAt(k) != text.charAt(i)) {
                check = false;
                numberOfComparison++;
                break;
            }
            numberOfComparison++;
            k--;
            if (k < 0)
                break;
        }
        if (check) {
            numberOfOccurence++;

        }
        return check;
    }


}
