import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;

/**
 * Created by Jenny Huang on 3/12/19.
 */
public class TestMyTrieSet {

    // assumes add/contains work
    @Test
    public void sanityClearTest() {
        MyTrieSet t = new MyTrieSet();
        for (int i = 0; i < 455; i++) {
            t.add("hi" + i);
            //make sure put is working via contains
            assertTrue(t.contains("hi" + i));
        }
        t.clear();
        for (int i = 0; i < 455; i++) {
            assertFalse(t.contains("hi" + i));
        }
    }

    // assumes add works
    @Test
    public void sanityContainsTest() {
        MyTrieSet t = new MyTrieSet();
        assertFalse(t.contains("waterYouDoingHere"));
        t.add("waterYouDoingHere");
        assertTrue(t.contains("waterYouDoingHere"));
    }

    // assumes add works
    @Test
    public void sanityPrefixTest() {
        String[] saStrings = new String[]{"same", "sam", "sad", "sap"};
        String[] otherStrings = new String[]{"a", "awls", "hello"};

        MyTrieSet t = new MyTrieSet();
        for (String s: saStrings) {
            t.add(s);
        }
        for (String s: otherStrings) {
            t.add(s);
        }

        List<String> keys = t.keysWithPrefix("sa");
        for (String s: saStrings) {
            assertTrue(keys.contains(s));
        }
        for (String s: otherStrings) {
            assertFalse(keys.contains(s));
        }
    }

    @Test
    public void longestPrefixTest() {
        String[] preStrings = new String[]{"gen", "general", "g", "gene", "eneraliz"};

        MyTrieSet t = new MyTrieSet();
        for (String s: preStrings) {
            t.add(s);
        }

        String longWord = "generalize";
        t.add(longWord);

        String answer = t.longestPrefixOf(longWord);
        assertEquals("general", answer);
    }

    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests(TestMyTrieSet.class);
    }



}
