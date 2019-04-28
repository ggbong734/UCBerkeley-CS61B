
/** A client that uses the synthesizer package to replicate a plucked guitar string sound */
import es.datastructur.synthesizer.GuitarString;

/** @author  Gerry Bong
 *  @version 1.0
 *  This is part of Josh Hug's CS 61B Homework 1 (UC Berkeley)
 */

public class GuitarHero {
    private static final double CONCERT_A = 440.0;
    private static final double CONCERT_C = CONCERT_A * Math.pow(2, 3.0 / 12.0);
    private static GuitarString[] CONCERT;

    public static void main(String[] args) {
        /* create two guitar strings, for concert A and C */
        CONCERT = new GuitarString[37];
        for (int i = 0; i < 37; i++) {
            CONCERT[i] = new GuitarString(440 * Math.pow(2, (i - 24) / 12));
        }
        String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";

        while (true) {

            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int indexOfKeyPressed = keyboard.indexOf(key);
                if (indexOfKeyPressed != -1) {
                    GuitarString stringPressed = CONCERT[indexOfKeyPressed];
                    stringPressed.pluck();
                }
            }

            /* compute the superposition of samples */
            double sample = 0;
            for (int i = 0; i < 37; i++) {
                sample += CONCERT[i].sample();
            }

            /* play the sample on standard audio */
            StdAudio.play(sample);

            /* advance the simulation of each guitar string by one step */
            for (GuitarString stringX : CONCERT) {
                stringX.tic();
            }
        }
    }
}
