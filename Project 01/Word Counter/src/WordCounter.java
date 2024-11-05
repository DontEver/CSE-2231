import components.map.Map;
import components.map.Map1L;
import components.queue.Queue;
import components.queue.Queue1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 *
 * @author Xinci Ma
 */

public class WordCounter {
    private static void sortQueue(Queue<String> words) {
        if (words.length() <= 1) {
            return; // Already sorted or empty
        }

        Queue<String> sortedQueue = new Queue1L<>();
        while (words.length() > 0) {
            // Find the smallest word in case-sensitive manner
            String smallest = words.dequeue();
            for (int i = 0; i < words.length(); i++) {
                String current = words.dequeue();
                if (current.compareToIgnoreCase(smallest) < 0
                        || (current.compareToIgnoreCase(smallest) == 0
                                && current.compareTo(smallest) < 0)) {
                    words.enqueue(smallest);
                    smallest = current;
                } else {
                    words.enqueue(current);
                }
            }
            // Enqueue the smallest word to the sorted queue
            sortedQueue.enqueue(smallest);
        }

        // Transfer from sortedQueue back to words
        while (sortedQueue.length() > 0) {
            words.enqueue(sortedQueue.dequeue());
        }
    }

    public static void main(String[] args) {
        SimpleReader input = new SimpleReader1L();
        SimpleWriter output = new SimpleWriter1L();

        output.print("Enter the name of the input file: ");
        String inputFile = input.nextLine();
        output.print("Enter the name of the output file: ");
        String outputFile = input.nextLine();

        SimpleReader fileReader = new SimpleReader1L(inputFile);
        Map<String, Integer> wordCounts = new Map1L<>();
        Queue<String> wordsQueue = new Queue1L<>();

        while (!fileReader.atEOS()) {
            String line = fileReader.nextLine();
            String[] words = line.split("\\W+");
            for (String word : words) {
                if (!word.isEmpty()) {
                    if (wordCounts.hasKey(word)) {
                        wordCounts.replaceValue(word,
                                wordCounts.value(word) + 1);
                    } else {
                        wordCounts.add(word, 1);
                        wordsQueue.enqueue(word);
                    }
                }
            }
        }
        fileReader.close();

        // Sort the words alphabetically
        sortQueue(wordsQueue);

        SimpleWriter fileWriter = new SimpleWriter1L(outputFile);
        fileWriter
                .println("<html><head><title>Word Count</title></head><body>");
        fileWriter.println("<h1>Words Counted in " + inputFile + "</h1>");
        fileWriter.println(
                "<table border='1'><tr><th>Words</th><th>Counts</th></tr>");

        while (wordsQueue.length() > 0) {
            String word = wordsQueue.dequeue();
            fileWriter.println("<tr><td>" + word + "</td><td>"
                    + wordCounts.value(word) + "</td></tr>");
        }

        fileWriter.println("</table></body></html>");
        fileWriter.close();

        output.println("Word count has been written to " + outputFile);
        input.close();
        output.close();
    }
}
