/** This file constructs a Palindrome class and contains methods
 *  that checks if a word is a palindrome.
 *  This is project 1b of UC Berkeley CS 61B Spring 2019 taught by Josh Hug.
 * @author Gerry Bong
 */

public class Palindrome {

/* Given a string, this method returns a Deque
 * where the characters appear in the same order as in String.*/

    public Deque<Character> wordToDeque(String word) {
        Deque<Character> d = new LinkedListDeque<>();
        for (int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);
            d.addLast(letter);
        }
        return d;
    }

    /* Helper method for isPalindrome method below*/
    private boolean isPalindromeDeque(Deque<Character> d) {
        if ((d.size() == 1) || (d.size() == 0)) {
            return true;
        }
        char first = d.removeFirst();
        char last = d.removeLast();
        if (first == last) {
            return isPalindromeDeque(d);
        } else {
            return false;
        }
    }

    /* Return true if the word is a palindrome:
     * same word read forwards or backwards */
    public boolean isPalindrome(String word) {
        Deque<Character> d = wordToDeque(word);
        return isPalindromeDeque(d);
    }

    /* Helper method for isPalindrome method below*/
    private boolean isPalindromeOffByOne(Deque<Character> d, CharacterComparator cc) {
        if ((d.size() == 1) || (d.size() == 0)) {
            return true;
        }
        char first = d.removeFirst();
        char last = d.removeLast();
        if (cc.equalChars(first, last)) {
            return isPalindromeOffByOne(d, cc);
        } else {
            return false;
        }
    }

    /*A method that overloads isPalindrome,
    * checks if a word is an off-by-one palindrome*/
    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> d = wordToDeque(word);
        return isPalindromeOffByOne(d, cc);
    }
}
