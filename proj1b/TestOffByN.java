import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByN {

    static CharacterComparator offByN = new OffByN(5);

    @Test
    public void testEqualChars() {
        char test1 = 'a';
        char pair1 = 'f';
        assertTrue(offByN.equalChars(test1, pair1));
        char test2 = 'f';
        char pair2 = 'a';
        assertTrue(offByN.equalChars(test2, pair2));
        char test3 = 'h';
        char pair3 = 'a';
        assertFalse(offByN.equalChars(test3, pair3));
        char test4 = 'j';
        char pair4 = '.';
        assertFalse(offByN.equalChars(test4, pair4));

    }
}
