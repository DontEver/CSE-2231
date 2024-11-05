import components.map.Map;
import components.program.Program;
import components.program.Program1;
import components.queue.Queue;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.statement.Statement;
import components.utilities.Reporter;
import components.utilities.Tokenizer;

/**
 * Layered implementation of secondary method {@code parse} for {@code Program}.
 *
 * @author Zhuoyang Li + Xinci Ma
 *
 */
public final class Program1Parse1 extends Program1 {

    /*
     * Private members --------------------------------------------------------
     */

    /**
     * Parses a single BL instruction from {@code tokens} returning the
     * instruction name as the value of the function and the body of the
     * instruction in {@code body}.
     *
     * @param tokens
     *            the input tokens
     * @param body
     *            the instruction body
     * @return the instruction name
     * @replaces body
     * @updates tokens
     * @requires <pre>
     * [<"INSTRUCTION"> is a prefix of tokens]  and
     *  [<Tokenizer.END_OF_INPUT> is a suffix of tokens]
     * </pre>
     * @ensures <pre>
     * if [an instruction string is a proper prefix of #tokens]  and
     *    [the beginning name of this instruction equals its ending name]  and
     *    [the name of this instruction does not equal the name of a primitive
     *     instruction in the BL language] then
     *  parseInstruction = [name of instruction at start of #tokens]  and
     *  body = [Statement corresponding to the block string that is the body of
     *          the instruction string at start of #tokens]  and
     *  #tokens = [instruction string at start of #tokens] * tokens
     * else
     *  [report an appropriate error message to the console and terminate client]
     * </pre>
     */
    private static String parseInstruction(Queue<String> tokens,
            Statement body) {
        assert tokens != null : "Violation of: tokens is not null";
        assert body != null : "Violation of: body is not null";
        assert tokens.length() > 0 && tokens.front().equals("INSTRUCTION") : ""
                + "Violation of: <\"INSTRUCTION\"> is proper prefix of tokens";

        //get rid of instruction
        String ins = tokens.dequeue();
        //get access to identifier
        String id = tokens.dequeue();
        String pri = "turnleft,turnright,turnback,skip,move";
        boolean isPrim = pri.indexOf(id) != -1;
        //get rid of is
        String is = tokens.dequeue();
        //save to body
        body.parseBlock(tokens);
        //check if the instruction is complete
        String end = tokens.dequeue();
        String checkEnd = tokens.dequeue();
        Reporter.assertElseFatalError(ins.equals("INSTRUCTION"),
                "Error: Expect String INSTRUCTION");
        Reporter.assertElseFatalError(Tokenizer.isIdentifier(id),
                "Error: Expect a unique instrcution name");
        Reporter.assertElseFatalError(!isPrim,
                "Error: you can not put a primitive call as an instruction name");
        Reporter.assertElseFatalError(is.equals("IS"),
                "Error: Expect String IS");
        Reporter.assertElseFatalError(end.equals("END"),
                "Error: Expect String END");
        Reporter.assertElseFatalError(checkEnd.equals(id),
                "Error: Expect a match instrcution name");

        return id;
    }

    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * No-argument constructor.
     */
    public Program1Parse1() {
        super();
    }

    /*
     * Public methods ---------------------------------------------------------
     */

    @Override
    public void parse(SimpleReader in) {
        assert in != null : "Violation of: in is not null";
        assert in.isOpen() : "Violation of: in.is_open";
        Queue<String> tokens = Tokenizer.tokens(in);
        this.parse(tokens);
    }

    @Override
    public void parse(Queue<String> tokens) {
        assert tokens != null : "Violation of: tokens is not null";
        assert tokens.length() > 0 : ""
                + "Violation of: Tokenizer.END_OF_INPUT is a suffix of tokens";

        //check header
        String pro = tokens.dequeue();
        String pre = tokens.dequeue();
        this.setName(pre);
        String is = tokens.dequeue();
        Reporter.assertElseFatalError(pro.equals("PROGRAM"),
                "Error: Expect String PROGRAM");
        Reporter.assertElseFatalError(is.equals("IS"),
                "Error: Expect String IS");
        //check header//

        //Instruction
        Map<String, Statement> context = this.newContext();
        while (tokens.front().equals("INSTRUCTION")) {
            Statement body = this.newBody();
            String ins = parseInstruction(tokens, body);
            context.add(ins, body);
        }
        this.swapContext(context);
        //Instruction//

        //check end
        Reporter.assertElseFatalError(tokens.front().equals("BEGIN"),
                "Error: Expect String BEGIN");
        tokens.dequeue();

        Statement newB = this.newBody();
        newB.parseBlock(tokens);
        this.swapBody(newB);

        String end = tokens.dequeue();
        String backid = tokens.dequeue();
        Reporter.assertElseFatalError(end.equals("END"),
                "Error: Expect String END");
        Reporter.assertElseFatalError(backid.equals(pre),
                "Error: Expect A match program name");
        Reporter.assertElseFatalError(
                tokens.front().equals("### END OF INPUT ###"), "Error");
        //check end//
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
        out.print("Enter valid BL program file name: ");
        String fileName = in.nextLine();
        /*
         * Parse input file
         */
        out.println("*** Parsing input file ***");
        Program p = new Program1Parse1();
        SimpleReader file = new SimpleReader1L(fileName);
        Queue<String> tokens = Tokenizer.tokens(file);
        file.close();
        p.parse(tokens);
        /*
         * Pretty print the program
         */
        out.println("*** Pretty print of parsed program ***");
        p.prettyPrint(out);

        in.close();
        out.close();
    }

}
