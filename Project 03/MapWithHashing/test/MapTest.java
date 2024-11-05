import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import components.map.Map;

/**
 * JUnit test fixture for {@code Map<String, String>}'s constructor and kernel
 * methods.
 *
 * @author Zhuoyang Li + Xinci Ma
 *
 */
public abstract class MapTest {

    /**
     * Invokes the appropriate {@code Map} constructor for the implementation
     * under test and returns the result.
     *
     * @return the new map
     * @ensures constructorTest = {}
     */
    protected abstract Map<String, String> constructorTest();

    /**
     * Invokes the appropriate {@code Map} constructor for the reference
     * implementation and returns the result.
     *
     * @return the new map
     * @ensures constructorRef = {}
     */
    protected abstract Map<String, String> constructorRef();

    /**
     *
     * Creates and returns a {@code Map<String, String>} of the implementation
     * under test type with the given entries.
     *
     * @param args
     *            the (key, value) pairs for the map
     * @return the constructed map
     * @requires <pre>
     * [args.length is even]  and
     * [the 'key' entries in args are unique]
     * </pre>
     * @ensures createFromArgsTest = [pairs in args]
     */
    private Map<String, String> createFromArgsTest(String... args) {
        assert args.length % 2 == 0 : "Violation of: args.length is even";
        Map<String, String> map = this.constructorTest();
        for (int i = 0; i < args.length; i += 2) {
            assert !map.hasKey(args[i]) : ""
                    + "Violation of: the 'key' entries in args are unique";
            map.add(args[i], args[i + 1]);
        }
        return map;
    }

    /**
     *
     * Creates and returns a {@code Map<String, String>} of the reference
     * implementation type with the given entries.
     *
     * @param args
     *            the (key, value) pairs for the map
     * @return the constructed map
     * @requires <pre>
     * [args.length is even]  and
     * [the 'key' entries in args are unique]
     * </pre>
     * @ensures createFromArgsRef = [pairs in args]
     */
    private Map<String, String> createFromArgsRef(String... args) {
        assert args.length % 2 == 0 : "Violation of: args.length is even";
        Map<String, String> map = this.constructorRef();
        for (int i = 0; i < args.length; i += 2) {
            assert !map.hasKey(args[i]) : ""
                    + "Violation of: the 'key' entries in args are unique";
            map.add(args[i], args[i + 1]);
        }
        return map;
    }

    // TODO - add test cases for constructor, add, remove, removeAny, value,
    // hasKey, and size

    //no argument constructor
    @Test
    public final void testConstructor1() {
        Map<String, String> test = this.constructorTest();
        Map<String, String> ref = this.constructorRef();
        assertEquals(ref, test);
    }

    //argument constructor
    @Test
    public final void testConstructor2() {
        Map<String, String> test = this.createFromArgsTest("a", "1", "c", "d");
        Map<String, String> ref = this.createFromArgsRef("a", "1", "c", "d");
        assertEquals(ref, test);
        assertTrue(test.size() == 2);
    }

    //add one pair
    @Test
    public void testAdd1() {
        Map<String, String> test = this.createFromArgsTest("a", "1");
        test.add("b", "2");
        Map<String, String> ref = this.createFromArgsRef("a", "1", "b", "2");
        assertEquals(test, ref);
    }

    //add pairs to an empty map
    @Test
    public void testAdd2() {
        Map<String, String> test = this.createFromArgsTest();
        test.add("a", "2");
        test.add("b", "3");
        Map<String, String> ref = this.createFromArgsRef("a", "2", "b", "3");
        assertEquals(test, ref);
    }

    @Test
    public final void testRemove1() {
        Map<String, String> test = this.createFromArgsTest("a", "b", "c", "d");
        Map<String, String> ref = this.createFromArgsRef("c", "d");

        Map.Pair<String, String> removed = test.remove("a");

        assertEquals(test, ref);
        assertEquals(removed.key(), "a");
        assertEquals(removed.value(), "b");
    }

    @Test
    public final void testRemove2() {
        Map<String, String> test = this.createFromArgsTest("c", "d");
        Map<String, String> ref = this.createFromArgsRef();
        Map.Pair<String, String> removed = test.remove("c");
        assertEquals(test, ref);
        assertTrue(test.size() == 0);
        assertEquals(removed.key(), "c");
        assertEquals(removed.value(), "d");

    }

    @Test
    public final void testRemoveAny1() {
        Map<String, String> test = this.createFromArgsTest("a", "b", "c", "d",
                "e", "f", "g", "h", "i", "j");
        Map<String, String> ref = this.createFromArgsRef("a", "b", "c", "d",
                "e", "f", "g", "h", "i", "j");
        test.removeAny();
        ref.removeAny();
        assertEquals(test.size(), ref.size());
    }

    @Test
    public final void testRemoveAny2() {
        Map<String, String> test = this.createFromArgsTest("1", "2");
        test.removeAny();

        assertEquals(test.size(), 0);
    }

    @Test
    public final void testValue1() {
        Map<String, String> test = this.createFromArgsTest("a", "b", "c", "d");
        Map<String, String> ref = this.createFromArgsRef("a", "b", "c", "d");
        assertEquals(test.value("a"), "b");
        assertEquals(test, ref); //check if the map is restored

    }

    @Test
    public final void hasKey1() {
        Map<String, String> test = this.createFromArgsTest("a", "b", "c", "d");
        assertTrue(test.hasKey("a"));
    }

    @Test
    public final void hasKey2() {
        Map<String, String> test = this.createFromArgsTest("a", "b", "c", "d");
        assertTrue(!test.hasKey("e"));
    }

    @Test
    public final void hasKey3() {
        Map<String, String> test = this.createFromArgsTest();
        assertTrue(!test.hasKey("a"));
    }

    @Test
    public final void size1() {
        Map<String, String> test = this.createFromArgsTest("a", "b", "c", "d");
        assertEquals(test.size(), 2);
    }

    @Test
    public final void size2() {
        Map<String, String> test = this.createFromArgsTest();
        assertEquals(test.size(), 0);
    }

}
