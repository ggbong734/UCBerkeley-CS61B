import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByOne {
    // You must use this CharacterComparator and not instantiate
    // new ones, or the autograder might be upset.
    static CharacterComparator offByOne = new OffByOne();

    // Your tests go here.
    @Test
    public void testEqualChars() {
        char test1 = 'x';
        char pair1 = 'y';
        assertTrue(offByOne.equalChars(test1, pair1));
        char test2 = 'x';
        char pair2 = 'Y';
        assertFalse(offByOne.equalChars(test2, pair2));
        char test3 = '1';
        char pair3 = '2';
        assertTrue(offByOne.equalChars(test3, pair3));
        char test4 = 'a';
        char pair4 = 'a';
        assertFalse(offByOne.equalChars(test4, pair4));
        char test5 = 'a';
        char pair5 = 'b';
        assertTrue(offByOne.equalChars(test5, pair5));
        char test6 = 'r';
        char pair6 = 'q';
        assertTrue(offByOne.equalChars(test6, pair6));
    }
}
