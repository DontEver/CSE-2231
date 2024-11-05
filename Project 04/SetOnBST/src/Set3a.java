import java.util.Iterator;
import java.util.Random;

import components.binarytree.BinaryTree;
import components.binarytree.BinaryTree1;
import components.set.Set;
import components.set.SetSecondary;

/**
 * {@code Set} represented as a {@code BinaryTree} (maintained as a binary
 * search tree) of elements with implementations of primary methods.
 *
 * @param <T>
 *            type of {@code Set} elements
 * @mathdefinitions <pre>
 * IS_BST(
 *   tree: binary tree of T
 *  ): boolean satisfies
 *  [tree satisfies the binary search tree properties as described in the
 *   slides with the ordering reported by compareTo for T, including that
 *   it has no duplicate labels]
 * </pre>
 * @convention IS_BST($this.tree)
 * @correspondence this = labels($this.tree)
 *
 * @author Zhuoyang Li + Xinci Ma
 *
 */
public class Set3a<T extends Comparable<T>> extends SetSecondary<T> {

    /*
     * Private members --------------------------------------------------------
     */

    /**
     * Elements included in {@code this}.
     */
    private BinaryTree<T> tree;

    /**
     * Returns whether {@code x} is in {@code t}.
     *
     * @param <T>
     *            type of {@code BinaryTree} labels
     * @param t
     *            the {@code BinaryTree} to be searched
     * @param x
     *            the label to be searched for
     * @return true if t contains x, false otherwise
     * @requires IS_BST(t)
     * @ensures isInTree = (x is in labels(t))
     */
    private static <T extends Comparable<T>> boolean isInTree(BinaryTree<T> t,
            T x) {
        assert t != null : "Violation of: t is not null";
        assert x != null : "Violation of: x is not null";

        boolean result = false;
        BinaryTree<T> left = t.newInstance();
        BinaryTree<T> right = t.newInstance();
        if (t.size() == 0) {
            result = false; // x is not in an empty tree t
        } else {
            T r = t.disassemble(left, right);
            if (r.equals(x)) {
                result = true; // x is in the root of t
            } else if (r.compareTo(x) > 0) {
                result = isInTree(left, x); // x might be in the left tree
            } else {
                result = isInTree(right, x); // x might be in the right tree
            }
            t.assemble(r, left, right); // restore t
        }
        return result;
    }

    /**
     * Inserts {@code x} in {@code t}.
     *
     * @param <T>
     *            type of {@code BinaryTree} labels
     * @param t
     *            the {@code BinaryTree} to be searched
     * @param x
     *            the label to be inserted
     * @aliases reference {@code x}
     * @updates t
     * @requires IS_BST(t) and x is not in labels(t)
     * @ensures IS_BST(t) and labels(t) = labels(#t) union {x}
     */
    private static <T extends Comparable<T>> void insertInTree(BinaryTree<T> t,
            T x) {
        assert t != null : "Violation of: t is not null";
        assert x != null : "Violation of: x is not null";

        BinaryTree<T> left = t.newInstance();
        BinaryTree<T> right = t.newInstance();
        if (t.size() == 0) {
            t.assemble(x, left, right); // insert x in an empty tree t
        } else {
            T r = t.disassemble(left, right);
            if (r.compareTo(x) > 0) {
                insertInTree(left, x); // insert x in the left tree
            } else {
                insertInTree(right, x); // insert x in the right tree
            }
            t.assemble(r, left, right); // restore t
        }

    }

    /**
     * Removes and returns the smallest (left-most) label in {@code t}.
     *
     * @param <T>
     *            type of {@code BinaryTree} labels
     * @param t
     *            the {@code BinaryTree} from which to remove the label
     * @return the smallest label in the given {@code BinaryTree}
     * @updates t
     * @requires IS_BST(t) and |t| > 0
     * @ensures <pre>
     * IS_BST(t)  and  removeSmallest = [the smallest label in #t]  and
     *  labels(t) = labels(#t) \ {removeSmallest}
     * </pre>
     */
    private static <T> T removeSmallest(BinaryTree<T> t) {
        assert t != null : "Violation of: t is not null";
        assert t.size() > 0 : "Violation of: |t| > 0";

        T result = t.root();
        BinaryTree<T> left = t.newInstance();
        BinaryTree<T> right = t.newInstance();
        if (t.size() == 1) {
            result = t.disassemble(left, right);
            // the smallest label is the root itself
        } else {
            T r = t.disassemble(left, right);

            if (left.size() > 0) {
                result = removeSmallest(left);
                t.assemble(r, left, right);
            } else {
                t.transferFrom(right);
            }
        }
        return result;
    }

    /**
     * Finds label {@code x} in {@code t}, removes it from {@code t}, and
     * returns it.
     *
     * @param <T>
     *            type of {@code BinaryTree} labels
     * @param t
     *            the {@code BinaryTree} from which to remove label {@code x}
     * @param x
     *            the label to be removed
     * @return the removed label
     * @updates t
     * @requires IS_BST(t) and x is in labels(t)
     * @ensures <pre>
     * IS_BST(t)  and  removeFromTree = x  and
     *  labels(t) = labels(#t) \ {x}
     * </pre>
     */
    private static <T extends Comparable<T>> T removeFromTree(BinaryTree<T> t,
            T x) {
        assert t != null : "Violation of: t is not null";
        assert x != null : "Violation of: x is not null";
        assert t.size() > 0 : "Violation of: x is in labels(t)";

        T result = t.root();
        if (t.size() == 1) {
            t.clear(); // delete the label
        } else {
            BinaryTree<T> left = t.newInstance();
            BinaryTree<T> right = t.newInstance();
            T r = t.disassemble(left, right);
            if (r.compareTo(x) > 0) {
                result = removeFromTree(left, x); // x is in the left tree
                t.assemble(r, left, right);
            } else if (r.compareTo(x) < 0) {
                result = removeFromTree(right, x); // x is in the right tree
                t.assemble(r, left, right);
            } else {
                // x is the root node
                result = r;
                if (right.size() == 0) {
                    t.transferFrom(left);
                } else {
                    T smallest = removeSmallest(right);
                    t.assemble(smallest, left, right);
                }

            }
        }

        return result;
    }

    /**
     * Creator of initial representation.
     */
    private void createNewRep() {

        this.tree = new BinaryTree1<T>();

    }

    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * No-argument constructor.
     */
    public Set3a() {

        this.createNewRep();

    }

    /*
     * Standard methods -------------------------------------------------------
     */

    @SuppressWarnings("unchecked")
    @Override
    public final Set<T> newInstance() {
        try {
            return this.getClass().getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(
                    "Cannot construct object of type " + this.getClass());
        }
    }

    @Override
    public final void clear() {
        this.createNewRep();
    }

    @Override
    public final void transferFrom(Set<T> source) {
        assert source != null : "Violation of: source is not null";
        assert source != this : "Violation of: source is not this";
        assert source instanceof Set3a<?> : ""
                + "Violation of: source is of dynamic type Set3<?>";
        /*
         * This cast cannot fail since the assert above would have stopped
         * execution in that case: source must be of dynamic type Set3a<?>, and
         * the ? must be T or the call would not have compiled.
         */
        Set3a<T> localSource = (Set3a<T>) source;
        this.tree = localSource.tree;
        localSource.createNewRep();
    }

    /*
     * Kernel methods ---------------------------------------------------------
     */

    @Override
    public final void add(T x) {
        assert x != null : "Violation of: x is not null";
        assert !this.contains(x) : "Violation of: x is not in this";

        insertInTree(this.tree, x);

    }

    @Override
    public final T remove(T x) {
        assert x != null : "Violation of: x is not null";
        assert this.contains(x) : "Violation of: x is in this";

        return removeFromTree(this.tree, x);

    }

    @Override
    public final T removeAny() {
        assert this.size() > 0 : "Violation of: this /= empty_set";

        return this.removeAnyHelper(this.tree);
    }

    /**
     * Helper method to remove a random element from the tree.
     *
     * @param tree
     *            the binary tree from which to remove an element
     * @return the value removed
     */
    private T removeAnyHelper(BinaryTree<T> tree) {
        Random rand = new Random();
        BinaryTree<T> left = tree.newInstance();
        BinaryTree<T> right = tree.newInstance();
        T value = tree.disassemble(left, right);

        int direction = tree.size() == 1 ? -1 : rand.nextInt(3);
        // 0: left, 1: right, -1 or 2: current node
        T removedValue;

        if (direction == 0 && left.size() > 0) {
            // Recursively remove from left
            removedValue = this.removeAnyHelper(left);
            tree.assemble(value, left, right);
        } else if (direction == 1 && right.size() > 0) {
            // Recursively remove from right
            removedValue = this.removeAnyHelper(right);
            tree.assemble(value, left, right);
        } else {
            // Remove current node
            removedValue = value;
            if (left.size() > 0 && right.size() > 0) {
                // Node has two children, find successor
                T successor = removeSmallest(right);
                tree.assemble(successor, left, right);
            } else if (left.size() > 0) {
                // Node has only left child
                tree.transferFrom(left);
            } else if (right.size() > 0) {
                // Node has only right child
                tree.transferFrom(right);
            } else {
                // Node is a leaf
                tree.clear();
            }
        }

        return removedValue;

    }

    @Override
    public final boolean contains(T x) {
        assert x != null : "Violation of: x is not null";

        return isInTree(this.tree, x);
    }

    @Override
    public final int size() {

        return this.tree.size();
    }

    @Override
    public final Iterator<T> iterator() {
        return this.tree.iterator();
    }

}
