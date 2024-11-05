import components.queue.Queue;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.statement.Statement;
import components.statement.Statement1;
import components.utilities.Reporter;
import components.utilities.Tokenizer;

/**
 * Layered implementation of secondary methods {@code parse} and
 * {@code parseBlock} for {@code Statement}.
 *
 * @author Zhuoyang Li + Xinci Ma
 *
 */
public final class Statement1Parse1 extends Statement1 {

    /*
     * Private members --------------------------------------------------------
     */

    /**
     * Converts {@code c} into the corresponding {@code Condition}.
     *
     * @param c
     *            the condition to convert
     * @return the {@code Condition} corresponding to {@code c}
     * @requires [c is a condition string]
     * @ensures parseCondition = [Condition corresponding to c]
     */
    private static Condition parseCondition(String c) {
        assert c != null : "Violation of: c is not null";
        assert Tokenizer
                .isCondition(c) : "Violation of: c is a condition string";
        return Condition.valueOf(c.replace('-', '_').toUpperCase());
    }

    /**
     * Parses an IF or IF_ELSE statement from {@code tokens} into {@code s}.
     *
     * @param tokens
     *            the input tokens
     * @param s
     *            the parsed statement
     * @replaces s
     * @updates tokens
     * @requires <pre>
     * [<"IF"> is a prefix of tokens]  and
     *  [<Tokenizer.END_OF_INPUT> is a suffix of tokens]
     * </pre>
     * @ensures <pre>
     * if [an if string is a proper prefix of #tokens] then
     *  s = [IF or IF_ELSE Statement corresponding to if string at start of #tokens]  and
     *  #tokens = [if string at start of #tokens] * tokens
     * else
     *  [reports an appropriate error message to the console and terminates client]
     * </pre>
     */
    private static void parseIf(Queue<String> tokens, Statement s) {
        assert tokens != null : "Violation of: tokens is not null";
        assert s != null : "Violation of: s is not null";
        assert tokens.length() > 0 && tokens.front().equals(
                "IF") : "Violation of: <\"IF\"> is proper prefix of tokens";

        // Remove "IF" token from the queue
        tokens.dequeue();
        // Ensure the next token is a valid condition
        Reporter.assertElseFatalError(
                tokens.length() > 0 && Tokenizer.isCondition(tokens.front()),
                "Expected condition after IF");

        String conditionToken = tokens.dequeue();
        Condition condition = parseCondition(conditionToken);

        // Ensure "THEN" follows the condition
        String then = tokens.dequeue();
        Reporter.assertElseFatalError(
                tokens.length() + 1 > 0 && then.equals("THEN"),
                "Expected THEN after condition");

        // Parse the block of statements for the IF branch
        Statement ifBranch = s.newInstance();
        Reporter.assertElseFatalError(tokens.length() > 0,
                "Unexpected end of input in IF branch");

        ifBranch.parseBlock(tokens);

        // Check for the presence of an ELSE branch or the end of the IF statement
        Reporter.assertElseFatalError(
                tokens.length() > 0 && (tokens.front().equals("ELSE")
                        || tokens.front().equals("END")),
                "Expected ELSE or END after IF branch");

        if (tokens.front().equals("ELSE")) {
            tokens.dequeue();
            // Parse the block of statements for the ELSE branch
            Statement elseBranch = s.newInstance();
            Reporter.assertElseFatalError(tokens.length() > 0,
                    "Unexpected end of input in ELSE branch");
            elseBranch.parseBlock(tokens);

            // Confirm the correct closure of an IF_ELSE statement with "END IF"
            Reporter.assertElseFatalError(
                    tokens.length() > 1 && tokens.dequeue().equals("END")
                            && tokens.dequeue().equals("IF"),
                    "Expected END IF to close IF_ELSE statement");
            s.assembleIfElse(condition, ifBranch, elseBranch);
        } else {
            // No ELSE branch, just ensure the IF statement is properly closed
            tokens.dequeue();
            Reporter.assertElseFatalError(
                    tokens.length() > 0 && tokens.dequeue().equals("IF"),
                    "Expected END IF to close IF statement");
            s.assembleIf(condition, ifBranch);
        }

    }

    /**
     * Parses a WHILE statement from the input token queue into the provided
     * statement object. This involves identifying and consuming a "WHILE"
     * keyword, followed by a condition, and a block of statements to execute
     * while the condition is true. The process concludes with expecting and
     * consuming an "END WHILE" to properly close the loop.
     *
     * @param tokens
     *            The queue of tokens representing the source code, which is
     *            being parsed.
     * @param s
     *            The statement object to populate based on the WHILE statement
     *            parsed from the tokens.
     * @replaces s
     * @updates tokens
     * @requires <pre>
     * ["WHILE" is a prefix of tokens] and
     * [Tokenizer.END_OF_INPUT is a suffix of tokens]
     * </pre>
     * @ensures <pre>
     * if [a while string is a proper prefix of #tokens] then
     * s = [WHILE Statement corresponding to the while string at the start of #tokens] and
     * #tokens = [while string at the start of #tokens] * tokens
     * else
     * [reports an appropriate error message to the console and terminates the client]
     * </pre>
     */
    private static void parseWhile(Queue<String> tokens, Statement s) {
        assert tokens != null : "Violation of: tokens is not null";
        assert s != null : "Violation of: s is not null";
        assert tokens.length() > 0 && tokens.front().equals(
                "WHILE") : "Violation of: <\"WHILE\"> is a proper prefix of tokens";

        // Remove "WHILE" keyword to proceed with condition parsing
        tokens.dequeue();

        // Check for a valid condition following "WHILE"
        Reporter.assertElseFatalError(
                tokens.length() > 0 && Tokenizer.isCondition(tokens.front()),
                "Expected a condition after WHILE");

        // Extract and parse condition token
        String conditionToken = tokens.dequeue();
        Condition condition = parseCondition(conditionToken);

        // Expect "DO" indicating the start of the loop's body
        Reporter.assertElseFatalError(
                tokens.length() > 0 && tokens.front().equals("DO"),
                "Expected DO after condition");

        tokens.dequeue();

        // Parse the loop body
        Statement loopBody = s.newInstance();
        Reporter.assertElseFatalError(tokens.length() > 0,
                "Unexpected end of input when parsing WHILE block");
        loopBody.parseBlock(tokens);

        // Ensure loop closure with "END WHILE"
        Reporter.assertElseFatalError(
                tokens.length() > 1 && tokens.dequeue().equals("END")
                        && tokens.dequeue().equals("WHILE"),
                "Expected END WHILE to properly close the loop");

        // Assemble the while loop statement
        s.assembleWhile(condition, loopBody);
    }

    /**
     * Parses a CALL statement from {@code tokens} into {@code s}.
     *
     * @param tokens
     *            the input tokens
     * @param s
     *            the parsed statement
     * @replaces s
     * @updates tokens
     * @requires [identifier string is a proper prefix of tokens]
     * @ensures <pre>
     * s =
     *   [CALL Statement corresponding to identifier string at start of #tokens]  and
     *  #tokens = [identifier string at start of #tokens] * tokens
     * </pre>
     */
    private static void parseCall(Queue<String> tokens, Statement s) {
        assert tokens != null : "Violation of: tokens is not null";
        assert s != null : "Violation of: s is not null";
        assert tokens.length() > 0
                && Tokenizer.isIdentifier(tokens.front()) : ""
                        + "Violation of: identifier string is proper prefix of tokens";

        String identifier = tokens.dequeue();
        s.assembleCall(identifier);
    }

    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * No-argument constructor.
     */
    public Statement1Parse1() {
        super();
    }

    /*
     * Public methods ---------------------------------------------------------
     */

    @Override
    public void parse(Queue<String> tokens) {
        assert tokens != null : "Violation of: tokens is not null";
        assert tokens.length() > 0 : ""
                + "Violation of: Tokenizer.END_OF_INPUT is a suffix of tokens";

        if (tokens.front().equals("IF")) {
            parseIf(tokens, this);
        } else if (tokens.front().equals("WHILE")) {
            parseWhile(tokens, this);
        } else if (Tokenizer.isIdentifier(tokens.front())) {
            parseCall(tokens, this);
        } else {
            Reporter.fatalErrorToConsole("Expected statement");
        }

    }

    @Override
    public void parseBlock(Queue<String> tokens) {
        assert tokens != null : "Violation of: tokens is not null";
        assert tokens.length() > 0 : ""
                + "Violation of: Tokenizer.END_OF_INPUT is a suffix of tokens";

        String token = tokens.front();
        int i = 0;
        while (token.equals("IF") || token.equals("WHILE")
                || Tokenizer.isIdentifier(token)) {
            Statement tempt = this.newInstance();

            tempt.parse(tokens);
            this.addToBlock(i, tempt);
            token = tokens.front();
            i++;
        }

    }

    /*
     * Main test method -------------------------------------------------------
     */

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();
        /*
         * Get input file name
         */
        out.print("Enter valid BL statement(s) file name: ");
        String fileName = in.nextLine();
        /*
         * Parse input file
         */
        out.println("*** Parsing input file ***");
        Statement s = new Statement1Parse1();
        SimpleReader file = new SimpleReader1L(fileName);
        Queue<String> tokens = Tokenizer.tokens(file);
        file.close();
        s.parse(tokens); // replace with parseBlock to test other method
        /*
         * Pretty print the statement(s)
         */
        out.println("*** Pretty print of parsed statement(s) ***");
        s.prettyPrint(out, 0);

        in.close();
        out.close();
    }

}
