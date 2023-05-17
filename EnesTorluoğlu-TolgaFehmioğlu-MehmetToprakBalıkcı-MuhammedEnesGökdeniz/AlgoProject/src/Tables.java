import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Tables {
    public static void main(String[] args) {
        String pattern="11010010011001010";
        System.out.println(constructGoodSuffix(pattern));
    }

    public static HashMap<Character, Integer> constructBadTable(String pattern) {

        HashMap<Character, Integer> badTable = new HashMap<>();
        int i = 0;
        char ch;
        int shiftValue;
        int len = pattern.length();

        while (i < pattern.length() - 1) {
            ch = pattern.charAt(i);
            shiftValue = len - i - 1;
            if (badTable.containsKey(ch)) {
                badTable.replace(ch, shiftValue);
            } else {
                badTable.put(ch, shiftValue);
            }
            i++;
        }

        char last = pattern.charAt(i);
        if (!badTable.containsKey(last)) {
            badTable.put(last, len);
        }

        badTable.put('*', len);
        return badTable;
    }



    public static HashMap<Integer, Integer> constructGoodSuffix(String pattern) {
        HashMap<Integer, Integer> goodSuffix = new HashMap<>();
        int value = pattern.length();
        String currentSubstring;
        int len = pattern.length();
        for (int k = 1; k <= len - 1; k++) {
            currentSubstring = pattern.substring(len - k - 1, len);
            value = getValue(pattern, currentSubstring);
            goodSuffix.put(k, value);
        }
        for (int i = len - 2; i >= 0; i--) {
            String subs = pattern.substring(0, i + 1);
            if (IsContains(subs, pattern)) {
                value = len - 1 - i;
                break;
            }
        }
        goodSuffix.put(len, value);

        return goodSuffix;
    }




    private static int getValue(String pattern, String substring) {
        String repetitivePart = substring.substring(1);
        int len = pattern.length();
        int value = len;
        String currentSubstring;
        int subs_len = repetitivePart.length();
        for (int i = len - 2; i >= 0; i--) {
            if (i + 1 <= subs_len) {
                currentSubstring = pattern.substring(0, i + 1);
                if (IsContains(currentSubstring, repetitivePart)) {
                    value = len - i - 1;
                    break;
                }
            } else {
                char mismatch = substring.charAt(0);
                char prev_char = pattern.charAt(i - subs_len);
                currentSubstring = pattern.substring(i - subs_len + 1, i + 1);
                if (IsContains(currentSubstring, repetitivePart)) {
                    if (mismatch != prev_char) {
                        value = len - i - 1;
                        break;
                    }

                }

            }
        }
        return value;
    }

    private static boolean IsContains(String currentSubstring, String pattern) {
        char ch1;
        char ch2;
        int j = pattern.length() - 1;
        for (int i = currentSubstring.length() - 1; i >= 0; i--) {
            ch1 = currentSubstring.charAt(i);
            ch2 = pattern.charAt(j);
            if (ch1 != ch2) {
                return false;
            }
            j--;
        }
        return true;
    }
    public static void PrintBadSymbolTable(HashMap<Character, Integer> badTable) {
        Set<Map.Entry<Character, Integer>> entries = badTable.entrySet();
        for (Map.Entry<Character, Integer> entry : entries) {
            System.out.printf("t(%c)->%d\n", entry.getKey(), entry.getValue());
        }

    }


    public static void PrintGoodSuffix(HashMap<Integer, Integer> goodSuffix) {
        Set<Map.Entry<Integer, Integer>> entries = goodSuffix.entrySet();
        for (Map.Entry<Integer, Integer> entry : entries) {
            System.out.println("k is " + entry.getKey() + " d2(" + entry.getKey() + ") is " + entry.getValue());
        }

    }


}
