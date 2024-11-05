import static org.junit.Assert.assertEquals;

import org.junit.Test;

import components.naturalnumber.NaturalNumber;

/**
 * JUnit test fixture for {@code NaturalNumber}'s constructors and kernel
 * methods.
 *
 * @author Zhuoyang Li + Xinci Ma
 *
 */
public abstract class NaturalNumberTest {

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * implementation under test and returns the result.
     *
     * @return the new number
     * @ensures constructorTest = 0
     */
    protected abstract NaturalNumber constructorTest();

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * implementation under test and returns the result.
     *
     * @param i
     *            {@code int} to initialize from
     * @return the new number
     * @requires i >= 0
     * @ensures constructorTest = i
     */
    protected abstract NaturalNumber constructorTest(int i);

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * implementation under test and returns the result.
     *
     * @param s
     *            {@code String} to initialize from
     * @return the new number
     * @requires there exists n: NATURAL (s = TO_STRING(n))
     * @ensures s = TO_STRING(constructorTest)
     */
    protected abstract NaturalNumber constructorTest(String s);

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * implementation under test and returns the result.
     *
     * @param n
     *            {@code NaturalNumber} to initialize from
     * @return the new number
     * @ensures constructorTest = n
     */
    protected abstract NaturalNumber constructorTest(NaturalNumber n);

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * reference implementation and returns the result.
     *
     * @return the new number
     * @ensures constructorRef = 0
     */
    protected abstract NaturalNumber constructorRef();

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * reference implementation and returns the result.
     *
     * @param i
     *            {@code int} to initialize from
     * @return the new number
     * @requires i >= 0
     * @ensures constructorRef = i
     */
    protected abstract NaturalNumber constructorRef(int i);

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * reference implementation and returns the result.
     *
     * @param s
     *            {@code String} to initialize from
     * @return the new number
     * @requires there exists n: NATURAL (s = TO_STRING(n))
     * @ensures s = TO_STRING(constructorRef)
     */
    protected abstract NaturalNumber constructorRef(String s);

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * reference implementation and returns the result.
     *
     * @param n
     *            {@code NaturalNumber} to initialize from
     * @return the new number
     * @ensures constructorRef = n
     */
    protected abstract NaturalNumber constructorRef(NaturalNumber n);

    // TODO - add test cases for four constructors, multiplyBy10, divideBy10, isZero

    @Test
    /**
     * Test constructor().
     */
    public final void testConstructor() {
        /*
         * Set up variables
         */
        NaturalNumber n = this.constructorTest();
        NaturalNumber nExpected = this.constructorRef();
        /*
         * Assert that values of n and nExpected are equal
         */
        assertEquals(nExpected, n);
    }

    @Test
    public final void testConstructorInt5() {
        /*
         * Set up variables
         */
        NaturalNumber n = this.constructorTest(5);
        NaturalNumber nExpected = this.constructorRef(5);
        /*
         * Assert that values of n and nExpected are equal
         */
        assertEquals(nExpected, n);
    }

    @Test
    public final void testConstructorString5() {
        /*
         * Set up variables
         */
        NaturalNumber n = this.constructorTest("0");
        NaturalNumber nExpected = this.constructorRef(0);
        /*
         * Assert that values of n and nExpected are equal
         */
        assertEquals(nExpected, n);
    }

    @Test
    public final void testConstructorNaturalNumber5() {
        /*
         * Set up variables
         */
        NaturalNumber n = this.constructorTest(this.constructorRef(5));
        NaturalNumber nExpected = this.constructorRef(5);
        /*
         * Assert that values of n and nExpected are equal
         */
        assertEquals(nExpected, n);
    }

    @Test
    public final void testConstructorInt0() {
        /*
         * Set up variables
         */
        NaturalNumber n = this.constructorTest(0);
        NaturalNumber nExpected = this.constructorRef(0);
        /*
         * Assert that values of n and nExpected are equal
         */
        assertEquals(nExpected, n);
    }

    @Test
    public final void testConstructorString0() {
        /*
         * Set up variables
         */
        NaturalNumber n = this.constructorTest("0");
        NaturalNumber nExpected = this.constructorRef(0);
        /*
         * Assert that values of n and nExpected are equal
         */
        assertEquals(nExpected, n);
    }

    @Test
    public final void testConstructorNaturalNumber0() {
        /*
         * Set up variables
         */
        NaturalNumber n = this.constructorTest(this.constructorRef(0));
        NaturalNumber nExpected = this.constructorRef(0);
        /*
         * Assert that values of n and nExpected are equal
         */
        assertEquals(nExpected, n);
    }

    @Test
    public final void test5MultiplyBy10() {
        /*
         * Set up variables
         */
        NaturalNumber n = this.constructorTest(5);
        NaturalNumber nExpected = this.constructorRef(55);
        /*
         * Call method under test
         */
        n.multiplyBy10(5);
        /*
         * Assert that values of n and nExpected are equal
         */
        assertEquals(nExpected, n);
    }

    @Test
    public final void test0MultiplyBy10() {
        /*
         * Set up variables
         */
        NaturalNumber n = this.constructorTest(0);
        NaturalNumber nExpected = this.constructorRef(5);
        /*
         * Call method under test
         */
        n.multiplyBy10(5);
        /*
         * Assert that values of n and nExpected are equal
         */
        assertEquals(nExpected, n);
    }

    @Test
    public final void test5multiplyBy10With0() {
        /*
         * Set up variables
         */
        NaturalNumber n = this.constructorTest(5);
        NaturalNumber nExpected = this.constructorRef(50);
        /*
         * Call method under test
         */
        n.multiplyBy10(0);

        /*
         * Assert that values of n and nExpected are equal
         */
        assertEquals(nExpected, n);
    }

    @Test
    public final void test0multiplyBy10With0() {
        /*
         * Set up variables
         */
        NaturalNumber n = this.constructorTest(0);
        NaturalNumber nExpected = this.constructorRef(0);
        /*
         * Call method under test
         */
        n.multiplyBy10(0);
        nExpected.multiplyBy10(0);
        /*
         * Assert that values of n and nExpected are equal
         */
        assertEquals(nExpected, n);
    }

    @Test
    public final void test5DivideBy10() {
        /*
         * Set up variables
         */
        NaturalNumber n = this.constructorTest(5);
        NaturalNumber nExpected = this.constructorRef(0);
        /*
         * Call method under test
         */
        int a = n.divideBy10();
        /*
         * Assert that values of n and nExpected are equal
         */
        assertEquals(nExpected, n);
        assertEquals(5, a);
    }

    @Test
    public final void test0DivideBy10() {
        /*
         * Set up variables
         */
        NaturalNumber n = this.constructorTest(0);
        NaturalNumber nExpected = this.constructorRef(0);
        /*
         * Call method under test
         */
        int a = n.divideBy10();
        /*
         * Assert that values of n and nExpected are equal
         */
        assertEquals(nExpected, n);
        assertEquals(0, a);
    }

    @Test
    public final void test14DivideBy10() {
        /*
         * Set up variables
         */
        NaturalNumber n = this.constructorTest(14);
        NaturalNumber nExpected = this.constructorRef(1);
        /*
         * Call method under test
         */
        int a = n.divideBy10();
        /*
         * Assert that values of n and nExpected are equal
         */
        assertEquals(nExpected, n);
        assertEquals(4, a);
    }

    @Test
    public final void test100DivideBy10() {
        /*
         * Set up variables
         */
        NaturalNumber n = this.constructorTest(100);
        NaturalNumber nExpected = this.constructorRef(10);
        /*
         * Call method under test
         */
        int a = n.divideBy10();
        /*
         * Assert that values of n and nExpected are equal
         */
        assertEquals(nExpected, n);
        assertEquals(0, a);
    }

    @Test
    public final void test0IsZero() {
        /*
         * Set up variables
         */
        NaturalNumber n = this.constructorTest(0);
        boolean b = n.isZero();
        /*
         * Assert that values of n and nExpected are equal
         */
        assertEquals(true, b);
    }

    @Test
    public final void test5IsZero() {
        /*
         * Set up variables
         */
        NaturalNumber n = this.constructorTest(5);
        boolean b = n.isZero();
        /*
         * Assert that values of n and nExpected are equal
         */
        assertEquals(false, b);
    }

    @Test
    public final void test10IsZero() {
        /*
         * Set up variables
         */
        NaturalNumber n = this.constructorTest(10);
        boolean b = n.isZero();
        /*
         * Assert that values of n and nExpected are equal
         */
        assertEquals(false, b);
    }

}
