import components.naturalnumber.NaturalNumber;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

public class NN3 {

    public static void main(String[] args) {
        SimpleWriter out = new SimpleWriter1L();
        NaturalNumber n = new NaturalNumber3();
        if (n.toString().length() == 0) {
            out.println("zero");
        } else {
            out.println("positive");
        }

    }

}
