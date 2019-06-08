package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.StringInputDevice;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class Engine {
    TERenderer ter = new TERenderer();
    private boolean gameOver;
    private boolean hasKey;
    private int turnNumber;
    private ArrayList<String> allMoves = new ArrayList<>();

    /* Feel free to change the width and height. */
    public static final int WIDTH = 60;
    public static final int HEIGHT = 40;
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
            "You got this!", "You're a star!", "Go Bears!",
            "Too easy for you!", "Wow, so impressive!"};

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        ter.initialize(WIDTH, HEIGHT, 0 , 1); // top offset for frame
        gameOver = false;
        drawMenu();
        String menuChoice = solicitMenuInput();
        if(menuChoice.equals("Q")){ // quit selected
            gameOver = true;
            drawMiddle("You have chosen to quit the game. Later!");

        } else if (menuChoice.equals("L")) {
            //TODO: Fill Load functionality

        } else if (menuChoice.equals("N")) { // new game selected
            String seedS = solicitSeedInput();
            int seed = parseInt(seedS);
            TETile[][] world = new TETile[WIDTH][HEIGHT];
            MapGenerator mg = new MapGenerator(seed);
            mg.generateRoomsAndHallways(world);
            ter.renderFrame(world);

            turnNumber = 1;
            while (!gameOver) {
                drawHUDHeader(world, turnNumber);
                String moveS = solicitMoveInput();
                mg.moveAvatar(world, moveS);
                ter.renderFrame(world);
                allMoves.add(moveS);
                turnNumber += 1;
                gameOver = mg.isGameOver();
                hasKey = mg.hasKey();
            }
            StdDraw.pause(700);
            drawMiddle("Congratulations, you beat the game!");
        }
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        drawMenu();

        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];

        InputSource inputSource;
        inputSource = new StringInputDevice(input);
        boolean initiateGame = false;
        int seed = 0;

        while (inputSource.possibleNextInput()) {
            char c = inputSource.getNextKey();
            c = Character.toUpperCase(c);
            if (c == 'N') {
                System.out.println("New Game selected");
                initiateGame = true;
            }
            if (c == 'Q') {
                System.out.println("Quit program.");
                break;
            }
            if (c == 'L') {
                //load game
            }
            if (Character.isDigit(c)) {
                seed = seed * 10 + c;
            }
            if (c == 'S') {
                if (initiateGame && seed > 0) {
                    MapGenerator mg = new MapGenerator(seed);
                    mg.generateRoomsAndHallways(finalWorldFrame);
                } else if (!initiateGame) {
                    throw new IllegalArgumentException("game not initiated, input should have N first");
                } else if (seed == 0) {
                    throw new IllegalArgumentException("seed is 0. Enter seed pls");
                }
            }
        }
        // empty world is returned if new game is not initiated or seed is not entered.
        return finalWorldFrame;
    }


    public void drawMenu() {
        //draw menu with black background
        StdDraw.clear();
        StdDraw.clear(Color.BLACK);

        Font font = new Font("Monaco", Font.BOLD, 35);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(WIDTH / 2, HEIGHT * 3 / 4, "CS61B: THE GAME");
        Font smallFont = new Font("Serif", Font.BOLD, 25);
        StdDraw.setFont(smallFont);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 2, "New Game (Press N)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "Load Game (Press L)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 2, "Quit (Press Q)");
        StdDraw.show();
    }

    // draw words in the middle of the frame
    public void drawMiddle(String s) {
        StdDraw.clear();
        StdDraw.clear(Color.BLACK);

        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, s);
        StdDraw.show();
    }

    public void drawHUDHeader(TETile[][] world, int turnNumber) {
        Font header = new Font("Monaco", Font.BOLD, 14);
        StdDraw.setFont(header);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.textLeft(1, HEIGHT - 1, "Round: " + turnNumber);
        StdDraw.text(WIDTH / 2, HEIGHT - 1, "Move with W, S, A, or D");
        StdDraw.text (WIDTH / 2 + 15, HEIGHT - 1, hasKey ? "Key Acquired, find Exit" : "Find the Key");
        int xPos = (int) Math.min(Math.max(0, StdDraw.mouseX()), WIDTH - 1);
        int yPos = (int) Math.min(Math.max(0, StdDraw.mouseY() - 1), HEIGHT - 1);
        StdDraw.textRight(WIDTH - 1, HEIGHT - 1, world[xPos][yPos].description());
        StdDraw.textRight(WIDTH - 1, 1, ENCOURAGEMENT[turnNumber % ENCOURAGEMENT.length]);
        StdDraw.show();
    }

    public String solicitMoveInput() {
        //Collect movement (W, S, A, D) input from user
        String s = "";
        while (s.length() < 1) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char c = Character.toUpperCase(StdDraw.nextKeyTyped());
            if (c == 'W' || c == 'S' || c == 'A'|| c == 'D') {
                s += c;
            }
        }
        StdDraw.pause(100);
        return s;
    }

    public String solicitMenuInput() {
        //Collect menu selection from user
        String s = "";
        while (s.length() < 1) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char c = Character.toUpperCase(StdDraw.nextKeyTyped());
            if (c == 'N' || c == 'L' || c == 'Q') {
                s += c;
                drawMiddle("You have pressed: " + s);
            } else {
                drawMiddle ("Please select from the options");
            }
        }
        StdDraw.pause(500);
        return s;
    }

    public String solicitSeedInput() {
        //Collect seed from user, remove last letter which is S
        String s = "";
        drawMiddle("Please enter a seed (integer) followed by the letter S");
        while (!s.contains("S")) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char c = Character.toUpperCase(StdDraw.nextKeyTyped());
            s += c;
            drawMiddle(s);
        }
        StdDraw.pause(500);
        return s.substring(0, s.length() - 1); // removes last letter S
    }
}
