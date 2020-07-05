package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.*;

import static java.lang.Integer.parseInt;

/** @author Gerry Bong
 *  This file starts the the CS61B game.
 *  User can move an avatar (W/S/A/D) to find a key in a maze and make his/her way to the locked door
 *  to win the game. The game has a save game feature by entering ":" followed by "Q" during
 *  the move screen. Game will load from the same condition when program is restarted.
 *
 */

public class Engine {
    private TERenderer ter = new TERenderer();
    private boolean gameOver;
    private boolean hasKey;
    private int turnNumber;
    private StringBuilder allMoves = new StringBuilder();

    /* Feel free to change the width and height. */
    static final int WIDTH = 60;
    static final int HEIGHT = 40;
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
            "You got this!", "You're a star!", "Go Bears!",
            "Too easy for you!", "Wow, so impressive!"};

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        ter.initialize(WIDTH, HEIGHT, 0, 1); // top offset for frame
        gameOver = false;
        drawMenu();
        String menuChoice = solicitMenuInput();
        if (menuChoice.equals("Q")) { // quit selected
            gameOver = true;
            drawMiddle("You have chosen to quit the game. Later!");

        } else if (menuChoice.equals("L")) {
            //load string from file
            String moves = loadMovesFromTextFile();
            int seed = getSeedFromMovesString(moves);
            moves = removeSeedFromMoves(moves);
            TETile[][] world = new TETile[WIDTH][HEIGHT];
            MapGenerator mg = new MapGenerator(seed);
            loadGame(world, mg, moves);
            allMoves.insert(0, "S");
            allMoves.insert(0, seed);
            continueGame(world, mg);

        } else if (menuChoice.equals("N")) { // new game selected
            String seedS = solicitSeedInput();
            int seed = parseInt(seedS);
            TETile[][] world = new TETile[WIDTH][HEIGHT];
            MapGenerator mg = new MapGenerator(seed);
            mg.generateRoomsAndHallways(world);
            ter.renderFrame(world);
            allMoves.append(seedS);
            allMoves.append("S");
            turnNumber = 1;
            continueGame(world, mg);
        }
    }

    /** This method traverses the list of moves in a previously saved file to get the world
     * to when the user saved and left the game.
     * @param moves list of moves from a previously saved file (when saving game)
     */
    public void loadGame(TETile[][] world, MapGenerator mg, String moves) {
        mg.generateRoomsAndHallways(world);
        StringBuilder sbHelper = new StringBuilder();
        sbHelper.append(moves);
        while (sbHelper.length() > 0) {
            String moveS = Character.toString(sbHelper.charAt(0));
            if (moveS.equals("S") || moveS.equals("A") || moveS.equals("D") || moveS.equals("W")) {
                mg.moveAvatar(world, moveS);
                allMoves.append(moveS);
                turnNumber += 1;
            }
            sbHelper.deleteCharAt(0);
        }
        ter.renderFrame(world);
    }

    /** Method to continue the game until it is won (user finds the key and exit the locked door)
     */
    public void continueGame(TETile[][] world, MapGenerator mg) {
        while (!gameOver) {
            drawHUDHeader(world, turnNumber);
            String moveS = solicitMoveInput();
            mg.moveAvatar(world, moveS);
            ter.renderFrame(world);
            allMoves.append(moveS);
            //check if quit game is activated
            checkIfUserQuits();
            turnNumber += 1;
            gameOver = mg.isGameOver();
            hasKey = mg.hasKey();
            //System.out.println(allMoves.toString());
        }
        StdDraw.pause(700);
        drawMiddle("Congratulations, you beat the game!");
    }

    public void checkIfUserQuits() {
        if (allMoves.length() > 1 && allMoves.substring(allMoves.length() - 1, allMoves.length()).equals("Q")
                && allMoves.substring(allMoves.length() - 2, allMoves.length() - 1).equals(":")) {
            File file = new File("C:\\Users\\ggbong\\Desktop\\Online Courses\\CS 61B\\UCBerkeley-CS61B-Sp2019\\proj3\\byow\\Core\\saved-game.txt");
            // save allMoves into file
            saveMovesIntoTextFile(file);
            drawMiddle("Game is saved, load game when restarting to continue");
        }
    }

    /** Save all moves into a text file except for the last two characters ":" and "Q"
     * @param file filepath to save file to
     */
    public void saveMovesIntoTextFile(File file) {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file);
            writer.write(allMoves.substring(0, allMoves.length() - 2));
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    /** Load all saved moves from a text file which includes seed number
     */
    public String loadMovesFromTextFile() {
        String s = "";
        File file = new File("C:\\Users\\ggbong\\Desktop\\Online Courses\\CS 61B\\UCBerkeley-CS61B-Sp2019\\proj3\\byow\\Core\\saved-game.txt");
        try {
            FileReader freader = new FileReader(file);
            BufferedReader br = new BufferedReader(freader);
            s = br.readLine();
            br.close();
            freader.close();
        } catch (FileNotFoundException e) {
            drawMiddle("No File found to load game");
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
        return s;
    }
    /** Draw menu with new game, load game, or quit game options
     */
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

    /** Draw words in the middle of the frame
     * @param s String to be drawn on the screen
     */
    public void drawMiddle(String s) {
        StdDraw.clear();
        StdDraw.clear(Color.BLACK);

        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, s);
        StdDraw.show();
    }

    /** Draw header during game play to indicate number of turns, instructions
     *  for moving avatar,whether key is found, and a description of the tile
     *  that the mouse cursor is pointing at.
     ** @param turnNumber number of turns that have elapsed
     */
    public void drawHUDHeader(TETile[][] world, int turnNumber) {
        Font header = new Font("Monaco", Font.BOLD, 14);
        StdDraw.setFont(header);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.textLeft(1, HEIGHT - 1, "Round: " + turnNumber);
        StdDraw.text(WIDTH / 2, HEIGHT - 1, "Move with W, S, A, or D");
        StdDraw.text(WIDTH / 2 + 15, HEIGHT - 1, hasKey ? "Key Acquired, find Exit" : "Find the Key");
        int xPos = (int) Math.min(Math.max(0, StdDraw.mouseX()), WIDTH - 1);
        int yPos = (int) Math.min(Math.max(0, StdDraw.mouseY() - 1), HEIGHT - 1);
        StdDraw.textRight(WIDTH - 1, HEIGHT - 1, world[xPos][yPos].description());
        StdDraw.textRight(WIDTH - 1, 1, ENCOURAGEMENT[turnNumber % ENCOURAGEMENT.length]);
        StdDraw.show();
    }

    /** Returns the seed from the list of saved moves (String)
     */
    public int getSeedFromMovesString(String s) {
        StringBuilder sb = new StringBuilder();
        sb.append(s);
        StringBuilder seedSB = new StringBuilder();
        while (sb.charAt(0) != ('S')) {
            seedSB.append(sb.charAt(0));
            sb.deleteCharAt(0);
        }
        return parseInt(seedSB.toString());
    }

    /** Removes the seed and the letter "S" from the list of saved moves (String) and returns the new
     * String
     */
    public String removeSeedFromMoves(String s) {
        StringBuilder sb = new StringBuilder();
        sb.append(s);
        while (sb.charAt(0) != ('S')) {
            sb.deleteCharAt(0);
        }
        sb.deleteCharAt(0); //delete S
        return sb.toString();
    }

    /** Removes the seed and the letter "S" from the list of saved moves (StringBuilder)
     * and returns the new StringBuilder
     */
    public StringBuilder removeSeedFromMovesSB(StringBuilder sb) {
        while (sb.charAt(0) != ('S')) {
            sb.deleteCharAt(0);
        }
        sb.deleteCharAt(0); //delete S
        return sb;
    }

    /** Collect one movement (W, S, A, D) input or save game input (:, Q) from user
     *  during game play and returns a string with the character
     */
    public String solicitMoveInput() {
        String s = "";
        while (s.length() < 1) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char c = Character.toUpperCase(StdDraw.nextKeyTyped());
            if (c == 'W' || c == 'S' || c == 'A' || c == 'D' || c == ':' || c == 'Q') {
                s += c;
            }
        }
        StdDraw.pause(100);
        return s;
    }

    /** Collect user selection in the menu screen (new game, load game, or quit game)
     *  and returns a String of the selected letter.
     */
    public String solicitMenuInput() {
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
                drawMiddle("Please select from the options");
            }
        }
        StdDraw.pause(500);
        return s;
    }

    /** Collect a set of integers followed by the letter S from the user to serve
     * as the seed for random number generator
     */
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

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, both of these calls:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        StringBuilder sb = new StringBuilder();
        sb.append(input);
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        int seed;

        //check if first char is N or L
        if (Character.toUpperCase(sb.charAt(0)) == 'N') {
            sb.deleteCharAt(0); // removes the N in front
            seed = getSeedFromMovesString(sb.toString());
            sb = removeSeedFromMovesSB(sb);
            MapGenerator mg = new MapGenerator(seed);
            mg.generateRoomsAndHallways(world);
            allMoves.insert(0, "S");
            allMoves.insert(0, seed);
            continueGameFromInputString(world, mg, sb);

        } else if (Character.toUpperCase(sb.charAt(0)) == 'L') {
            sb.deleteCharAt(0); // removes the L in front
            String savedMoves = loadMovesFromTextFile();
            seed = getSeedFromMovesString(savedMoves);
            savedMoves = removeSeedFromMoves(savedMoves);
            StringBuilder savedInputSB = new StringBuilder();
            savedInputSB.append(savedMoves);
            MapGenerator mg = new MapGenerator(seed);
            mg.generateRoomsAndHallways(world);
            allMoves.insert(0, "S");
            allMoves.insert(0, seed);
            continueGameFromInputString(world, mg, savedInputSB); //take moves from saved file
            continueGameFromInputString(world, mg, sb);

        } else {
            throw new IllegalArgumentException("Start input String with N or L");
        }

        return world;
    }

    /**
     * Method to take inputs and move avatar in the world
     * If W,S,A,D is entered, avatar is moved when possible
     * If Q is entered, then past move list is saved into text file
     *
     * @param sb input moves in the form of StringBuilder
     */
    private void continueGameFromInputString(TETile[][] world, MapGenerator mg, StringBuilder sb) {
        while (sb.length() > 0) {
            String moveS = Character.toString(sb.charAt(0));
            if (moveS.equals("S") || moveS.equals("A") || moveS.equals("D") || moveS.equals("W")) {
                mg.moveAvatar(world, moveS);
                allMoves.append(moveS);
                turnNumber += 1;
            } else if (moveS.equals("Q")) {
                File file = new File("C:\\Users\\ggbong\\Desktop\\Online Courses\\CS 61B\\UCBerkeley-CS61B-Sp2019\\proj3\\byow\\Core\\saved-game.txt");
                saveMovesIntoTextFile(file);
            }
            sb.deleteCharAt(0);
        }
    }
}