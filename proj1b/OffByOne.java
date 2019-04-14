/** This file constructs a OffByOne class
 *  This is project 1b of UC Berkeley CS 61B Spring 2019 taught by Josh Hug.
 * @author Gerry Bong
 */

public class OffByOne implements CharacterComparator {

    /* Returns true if the two characters are off by one */
    @Override
    public boolean equalChars(char x, char y) {
        if (Math.abs(x - y) == 1) {
            return true;
        }
        return false;
    }
}
