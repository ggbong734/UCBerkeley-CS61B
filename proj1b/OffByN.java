/** This file constructs a OffByN class
 *  This is project 1b of UC Berkeley CS 61B Spring 2019 taught by Josh Hug.
 * @author Gerry Bong
 */

public class OffByN implements CharacterComparator {

    private int difference;

    public OffByN(int N) {
        difference = N;
    }

    @Override
    public boolean equalChars(char x, char y) {
        if (Math.abs(x - y) == difference) {
            return true;
        }
        return false;
    }
}
