package byow.lab13;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        int seed = Integer.parseInt(args[0]);
        MemoryGame game = new MemoryGame(40, 40);
        game.rand.setSeed(seed);
        game.startGame();
    }

    public MemoryGame(int width, int height) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        //Initialize random number generator
        this.rand = new Random();
    }

    public String generateRandomString(int n) {
        //Generate random string of letters of length n
        StringBuilder s = new StringBuilder();
        while (s.length() < n) {
            int rd = rand.nextInt(CHARACTERS.length);
            s.append(CHARACTERS[rd]);
        }
        return s.toString();
    }

    public void drawFrame(String s) {
        //Take the string and display it in the center of the screen
        StdDraw.clear();
        StdDraw.clear(Color.BLACK);

        //If game is not over, display relevant game information at the top of the screen
        if (!gameOver) {
            Font header = new Font("Serif", Font.BOLD, 25);
            StdDraw.setFont(header);
            StdDraw.textLeft(1, height - 1, "Round: " + round);
            StdDraw.text(width / 2, height - 1, playerTurn ? "Type!" : "Watch!");
            StdDraw.textRight(width - 1, height - 1, ENCOURAGEMENT[round % ENCOURAGEMENT.length]);
            StdDraw.line(0, height - 2, width, height - 2);
        }

        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(width / 2, height / 2, s);
        StdDraw.show();
    }

    public void flashSequence(String letters) {
        //Display each character in letters, making sure to blank the screen between letters
        for(int i = 0; i < letters.length(); i += 1) {
            drawFrame(letters.substring(i, i + 1));
            StdDraw.pause(750);
            drawFrame("");
            StdDraw.pause(750);
        }
    }

    public String solicitNCharsInput(int n) {
        //Read n letters of player input
        String s = "";
        drawFrame(s);
        while (s.length() < n) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            s += StdDraw.nextKeyTyped();
            drawFrame(s);
        }
        StdDraw.pause(500);
        return s;
    }

    public void startGame() {
        //Set any relevant variables before the game starts
        round = 1;
        gameOver = false;
        playerTurn = false;

        while (!gameOver) {
            playerTurn = false;
            drawFrame("Round:" + round);
            StdDraw.pause(1500);
            String rds = generateRandomString(round);
            flashSequence(rds);

            playerTurn = true;
            String input = solicitNCharsInput(round);
            if (input.equals(rds)) {
                drawFrame("Correct, well done!");
                round += 1;
                StdDraw.pause(1500);
            } else {
                gameOver = true;
                drawFrame("Game Over! You made it to round: " + round);
            }
        }
        //TODO: Establish Engine loop
    }

}
