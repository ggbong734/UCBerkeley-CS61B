import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }

    @Test
    public void testIsPalindrome() {
        assertFalse(palindrome.isPalindrome("cat"));
        assertFalse(palindrome.isPalindrome("rancor"));
        assertFalse(palindrome.isPalindrome("Radar"));
        assertFalse(palindrome.isPalindrome("rEdder"));
        assertTrue(palindrome.isPalindrome("r"));
        assertTrue(palindrome.isPalindrome(""));
        assertTrue(palindrome.isPalindrome("RadaR"));
        assertTrue(palindrome.isPalindrome("noon"));
        assertTrue(palindrome.isPalindrome("racecar"));
    }

    @Test
    public void testIsPalindromeOffByOne() {
        OffByOne obo = new OffByOne();
        assertFalse(palindrome.isPalindrome("cat", obo));
        assertFalse(palindrome.isPalindrome("madam", obo));
        assertTrue(palindrome.isPalindrome("die", obo));
        assertTrue(palindrome.isPalindrome("cod", obo));
        assertTrue(palindrome.isPalindrome("&adyxeb%", obo));
    }
}
