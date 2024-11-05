import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * This Java program generates a HTML file with tag cloud from a given input
 * text.
 *
 * @author Zhuoyang Li + Xinci Ma
 *
 */
public final class TagCouldGeneratorJava {

    /**
     * Compare {@code String}s in alphabetical order.
     */
    private static class StringLT
            implements Comparator<Map.Entry<String, Integer>> {

        @Override
        public int compare(Map.Entry<String, Integer> o1,
                Map.Entry<String, Integer> o2) {
            return o1.getKey().compareToIgnoreCase(o2.getKey());
        }
    }

    /**
     * Compare {@code String}s by frequency.
     */
    private static class IntegerLT
            implements Comparator<Map.Entry<String, Integer>> {

        @Override
        public int compare(Map.Entry<String, Integer> o1,
                Map.Entry<String, Integer> o2) {
            return o2.getValue().compareTo(o1.getValue());
        }
    }

    /**
     * Private Constructor so that the class cannot be instantiated.
     */

    private TagCouldGeneratorJava() {

    }

    /**
     * Max frequency of words.
     */
    private static Integer MAX;

    /**
     * Min frequency of words.
     */
    private static Integer MIN;

    /**
     * Define separators.
     */
    private static final String SEPARATORS = "[ \t\n\r\f,.:;?!\\[\\](){}<>\"']+=/*&^%$#@~`|*_-&#$1234567890";

    /**
     * Calculate the frequency of words in {@code input}.
     *
     * @param input
     *            the input text file
     * @param separator
     *            the set of separators
     * @return a map from words to their frequencies
     * @require input != null && input.isOpen
     * @ensure return != null && return.keySet() != null
     */
    public static Map<String, Integer> wordCount(Set<Character> separator,
            BufferedReader input) {
        Map<String, Integer> wordCount = new HashMap<>();
        String line = "";
        try {
            line = input.readLine();
            while (line != null) {
                if (!line.isEmpty()) {
                    int i = 0;
                    while (i < line.length()) {
                        String next = nextStringOrSeparator(line, i)
                                .toLowerCase();

                        //check if any char in next is a separator
                        if (isWord(separator, next)) {
                            if (!wordCount.containsKey(next)) {
                                //if next is not in wordCount, add it to wordCount
                                wordCount.put(next, 1);
                            } else {
                                //if next is in wordCount, increment the count of next
                                wordCount.put(next, wordCount.get(next) + 1);
                            }
                        }
                        i += next.length();
                    }
                }
                line = input.readLine();
            }
        } catch (IOException e) {
            System.err.println("Error reading from file");
        }
        return wordCount;
    }

    /**
     * Sorts the words with frequency in {@code wordCount} in alphabetical
     * order.
     *
     * @param wordCount
     *            the map from words to their frequencies
     * @param n
     *            the number of words to be displayed
     * @update wordCount
     * @return a sorted list of words with frequency
     * @ensure return != null && return is sorted in alphabetical order
     */

    public static List<Map.Entry<String, Integer>> sorter(
            Map<String, Integer> wordscount, int n) {
        List<Map.Entry<String, Integer>> sortedKey = new ArrayList<>();
        List<Map.Entry<String, Integer>> sortedValue = new ArrayList<>();
        Comparator<Map.Entry<String, Integer>> stringLT = new StringLT();
        Comparator<Map.Entry<String, Integer>> integerLT = new IntegerLT();

        //get all words from input to the list
        for (Map.Entry<String, Integer> entry : wordscount.entrySet()) {
            sortedValue.add(entry);

        }
        //sort the list by frequency
        sortedValue.sort(integerLT);

        MAX = 0;
        MIN = sortedValue.get(0).getValue();

        //transfer the words from sortedValue to sortedKey
        while (!sortedValue.isEmpty() && sortedKey.size() < n) {
            sortedKey.add(sortedValue.remove(0));

            //update MAX and MIN
            if (sortedKey.get(sortedKey.size() - 1).getValue() > MAX) {
                MAX = sortedKey.get(sortedKey.size() - 1).getValue();
            }
            if (sortedKey.get(sortedKey.size() - 1).getValue() < MIN) {
                MIN = sortedKey.get(sortedKey.size() - 1).getValue();
            }
        }
        //sort the list by alphabetical order
        sortedKey.sort(stringLT);
        return sortedKey;

    }

    /**
     * Check if the string is a word(not a separator).
     *
     * @param separator
     *            the set of separators
     * @param next
     *            the string to be checked
     * @return true if the string is a word
     * @require {@code next} != null
     * @ensure return != null && return true if the string is a word and false
     *         if it is a separator
     */
    public static boolean isWord(Set<Character> separator, String next) {
        boolean isWord = true;
        for (int i = 0; i < next.length(); i++) {
            if (separator.contains(next.charAt(i))) {
                isWord = false;
            }
        }
        return isWord;
    }

    /**
     * Get the next word or separator in the line.
     *
     * @param line
     *            the line to be checked
     * @param position
     *            the position of the character in the line
     * @return the next word or separator in the line
     * @require {@code line} != null && {@code position} >= 0
     * @ensure return != null
     */
    public static String nextStringOrSeparator(String line, int position) {
        int i = position + 1;
        while (i < line.length() && ((SEPARATORS.indexOf(line.charAt(position))
                * SEPARATORS.indexOf(line.charAt(i))) >= 0)) {
            i++;
        }
        return line.substring(position, i);
    }

    /**
     * Store the separators in the set.
     *
     * @param separator
     *            the set of separators
     * @replace separator
     * @ensure All separators are stored in the set.
     */
    public static void separatorsSet(Set<Character> separator) {
        for (int i = 0; i < SEPARATORS.length(); i++) {
            separator.add(SEPARATORS.charAt(i));
        }
    }

    /**
     * Generate the HTML file.
     *
     * @param output
     *            the output stream
     * @param inputFile
     *            the input file
     * @param n
     *            the number of words to be displayed
     * @require output != null && inputFile != null && n > 0
     * @ensure the HTML file is generated
     */
    public static void HtmlGenerator(PrintWriter output, String inputFile,
            int n) {
        output.println("<html>");
        output.println("<head>");
        output.println("<title>");
        output.println("Tag Cloud Generator");
        output.println("</title>");
        output.println(
                "<link href=\"http://web.cse.ohio-state.edu/software/2231/web-sw2/assignments/projects/tag-cloud-generator/data/tagcloud.css\" rel=\"stylesheet\" type=\"text/css\">");
        output.println("</head>");

        output.println("<body>");
        output.println("<h2>Top " + n + " words in " + inputFile + "</h2>");

        output.println("<hr>");
        output.println("<div class=\"cdiv\">");
        output.println("<p class=\"cbox\">");

    }

    /**
     * Calculate the font size of the displayed word.
     *
     * @param frequency
     *            the frequency of the word
     * @param max
     *            the max frequency of the words
     * @param min
     *            the min frequency of the words
     * @return the font size of the word
     * @require frequency >= 0 && max >= 0 && min >= 0
     * @ensure return >= 11 && return <= 48
     */
    public static int fontSize(int frequency, Integer max, Integer min) {
        int size = 0;
        if (max == min) {
            size = (48 - 11) * (frequency - min) + 11;
        } else {
            size = (48 - 11) * (frequency - min) / (max - min) + 11;
        }
        return size;
    }

    /**
     * Output the words in the HTML file.
     *
     * @param out
     *            the output stream
     * @param sorted
     *            the sorted list of words with frequency
     * @param max
     *            the max frequency of the words
     * @param min
     *            the min frequency of the words
     * @require out != null && sorted != null && max >= 0 && min >= 0
     * @ensure the words are displayed in the HTML file
     */
    public static void outputWords(PrintWriter out,
            TreeMap<String, Integer> sorted, Integer max, Integer min) {
        int t = sorted.size();
        for (int i = 0; i < t; i++) {
            out.println("<span style=\"cursor:default\" class=\"f"
                    + fontSize(sorted.firstEntry().getValue(), max, min)
                    + "\" title=\"frequency: " + sorted.firstEntry().getValue()
                    + "\">" + sorted.firstEntry().getKey() + "</span>");
            sorted.remove(sorted.firstEntry().getKey());

        }
        out.println("</p>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }

    /**
     * Main program.
     *
     * @param args
     *            the command line arguments
     * @throws IOException
     */
    public static void main(String[] args) {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(System.in));
        BufferedReader input = null;
        String inputFile = "";
        PrintWriter output = null;

        System.out.println("Please enter the input file name: ");

        //open the input file
        try {
            inputFile = in.readLine();

        } catch (IOException e) {
            System.err.println("Error reading from file");
            return;
        }

        //read the input file
        try {
            input = new BufferedReader(new FileReader(inputFile));
        } catch (IOException e) {
            System.err.println("Error reading from file location");
            return;
        }

        System.out.println("Please enter the output file name: ");
        String outputFile = "";

        //output name
        try {
            outputFile = in.readLine();
        } catch (IOException e) {
            System.err.println("Error reading from input");

        }

        //number of words
        System.out
                .println("Please enter the number of words to be displayed: ");
        int topN = -20;
        String str = "";

        while (topN < 0) {
            try {
                str = in.readLine();

            } catch (IOException e) {
                System.err.println("Error reading from input");
            }

            if (str != null) {
                topN = Integer.parseInt(str);
            }

            if (topN < 0) {
                System.out.println("Please enter a positive number.");
            }
        }
        try {
            output = new PrintWriter(
                    new BufferedWriter(new FileWriter(outputFile)));
        } catch (IOException e) {
            System.err.println("Error writing to file");
            return;
        }
        HtmlGenerator(output, inputFile, topN);
        Set<Character> separator = new HashSet<>();
        separatorsSet(separator);
        Map<String, Integer> wordscount = wordCount(separator, input);

        //if the input file is empty
        if (wordscount.size() > 0) {

            List<Map.Entry<String, Integer>> sortedWords = sorter(wordscount,
                    topN);
            TreeMap<String, Integer> sorted = new TreeMap<>();
            for (int i = 0; i < sortedWords.size(); i++) {
                sorted.put(sortedWords.get(i).getKey(),
                        sortedWords.get(i).getValue());
            }
            outputWords(output, sorted, MAX, MIN);
        }

        //close the input and output file
        try {
            input.close();
        } catch (IOException e) {
            System.err.println("Error closing file");
        }
        try {
            in.close();
        } catch (IOException e) {
            System.err.println("Error closing file");
        }
        output.close();
    }
}
