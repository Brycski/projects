package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.algs4.StdDraw;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import java.awt.*;
import java.io.*;

public class Engine {
    /* Feel free to change the width and height. */
    public static final int WIDTH = 60;
    public static final int HEIGHT = 30;
    private World world;
    boolean pathShowing = false;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        drawMenu();
        while (!(StdDraw.hasNextKeyTyped())) {
            StdDraw.pause(500);
        }
        if (StdDraw.hasNextKeyTyped() || StdDraw.isMousePressed()) {
            char key = StdDraw.nextKeyTyped();
            if (key == 'N' || key == 'n') {
                initializeWorld();
                while (true) {
                    while (!(StdDraw.hasNextKeyTyped())) {
                        hudMouse();
                    }
                    if (StdDraw.hasNextKeyTyped()) {
                        playerMovement(StdDraw.nextKeyTyped());
                    }
                }
            }
            if (key == 'L' || key == 'l') {
                loadGame();
                TERenderer ter = new TERenderer();
                ter.initialize(WIDTH, HEIGHT + 2, 0, 2);
                ter.renderFrame(world.map);
                while (true) {
                    while (!(StdDraw.hasNextKeyTyped())) {
                        hudMouse();
                    }
                    if (StdDraw.hasNextKeyTyped()) {
                        playerMovement(StdDraw.nextKeyTyped());
                    }
                }
            }
            if (key == 'Q' || key == 'q') {
                System.exit(0);
            }
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
        // Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        for (int i = 0; i < input.length(); i++) {
            // Requesting a new World
            if (input.charAt(i) == 'N' || input.charAt(i) == 'n') {
                StringBuilder charSeed = new StringBuilder();
                int v = i + 1;
                while (input.charAt(v) != 'S' && input.charAt(v) != 's') {
                    charSeed.append(input.charAt(v));
                    v++;
                }
                i = v;
                String stringSeed = charSeed.toString();
                world = new World(WIDTH, HEIGHT, Long.parseLong(stringSeed));
                finalWorldFrame = world.map;
                if (i == input.length()) {
                    break;
                }
            }
            if (input.charAt(i) == 'L' || input.charAt(i) == 'l') {
                loadGame();
                finalWorldFrame = world.map;
            }
            if (input.charAt(i) == 'Q' || input.charAt(i) == 'q') {
                break;
            }
            if (input.charAt(i) == 'w') {
                world.playerMove(world.player.position.x, world.player.position.y,
                        world.player.position.x, world.player.position.y + 1, world);
                finalWorldFrame = world.map;
            }
            if (input.charAt(i) == 'a') {
                world.playerMove(world.player.position.x, world.player.position.y,
                        world.player.position.x - 1, world.player.position.y, world);
                finalWorldFrame = world.map;
            }
            if (input.charAt(i) == 's') {
                world.playerMove(world.player.position.x, world.player.position.y,
                        world.player.position.x, world.player.position.y - 1, world);
                finalWorldFrame = world.map;
            }
            if (input.charAt(i) == 'd') {
                world.playerMove(world.player.position.x, world.player.position.y,
                        world.player.position.x + 1, world.player.position.y, world);
                finalWorldFrame = world.map;
            }
            if (input.charAt(i) == ':') {
                i++;
                if (input.charAt(i) == 'q') {
                    saveMGStatus(world);
                    break;
                }
            }
        }
        return finalWorldFrame;
    }

    // made static might want to remove
    public static void drawMenu() {
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.clear(Color.BLUE);
        StdDraw.picture(WIDTH/2,HEIGHT/2,
                "/Users/bryceszarzynski/cs61bl/su22-p214/proj3/byow/Core/images/back1.png");
        StdDraw.picture(WIDTH/2, HEIGHT/2 +13,
                "/Users/bryceszarzynski/cs61bl/su22-p214/proj3/byow/Core/images/gamename.png");
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Baskerville", Font.BOLD, 20);
        StdDraw.setFont(fontBig);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 5, "New World (N)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "Load (L)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 5, "Quit (Q)");
        StdDraw.setPenColor(Color.PINK);
        StdDraw.setPenRadius(0.01);
        StdDraw.square(30,15,8);
        StdDraw.setPenColor(Color.ORANGE);
        StdDraw.square(30,15,9);
        StdDraw.setPenColor(Color.RED);
        StdDraw.square(30,15,8.5);
        StdDraw.setPenColor(Color.YELLOW);
        StdDraw.square(30,15,9.5);
        StdDraw.setPenColor(Color.GREEN);
        StdDraw.square(30,15,10);
        StdDraw.setPenColor(Color.MAGENTA);
        StdDraw.square(30,15,10.5);
        MusicHandler.runMusic("/Users/bryceszarzynski/cs61bl/su22-p214/proj3/byow/Res/infinitywar.wav");
    }

    public void initializeWorld() {
        StringBuilder getSeed = new StringBuilder();
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.clear(Color.BLUE);
        StdDraw.picture(WIDTH/2,HEIGHT/2,
                "/Users/bryceszarzynski/cs61bl/su22-p214/proj3/byow/Core/images/back1.png");
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Bakersville", Font.BOLD, 20);
        StdDraw.setFont(fontBig);
        StdDraw.show();
        // CHOOSE SKIN
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 5, "Please Enter a Seed");
        while (!(StdDraw.hasNextKeyTyped())) {
            StdDraw.pause(500);
        }
        if (StdDraw.hasNextKeyTyped()) {
            char seedKey = StdDraw.nextKeyTyped();
            while (seedKey != 's' && seedKey != ('S')) {
                getSeed.append(seedKey);
                StdDraw.clear(Color.BLUE);
                StdDraw.picture(WIDTH/2,HEIGHT/2,
                        "/Users/bryceszarzynski/cs61bl/su22-p214/proj3/byow/Core/images/back1.png");
                StdDraw.text(WIDTH / 2, HEIGHT / 2 + 5, getSeed.toString());
                StdDraw.show();
                while (!(StdDraw.hasNextKeyTyped())) {
                    StdDraw.pause(500);
                }
                seedKey = StdDraw.nextKeyTyped();
            }
        }
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT + 2, 0, 2);
        world = new World(WIDTH, HEIGHT, Long.parseLong(getSeed.toString()));
        ter.renderFrame(world.map);
        while (!(StdDraw.hasNextKeyTyped())) {
            StdDraw.pause(500);
        }
    }

    //save mid-game status
    public static void saveMGStatus(World savedWorld) {
        File file = new File("./save_game.txt");
        try {
            if (!(file.exists())) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(savedWorld);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    //loading the game
    public void loadGame() {
        File file = new File("./save_game.txt");
        if (file.exists()) {
            try {
                FileInputStream fos = new FileInputStream(file);
                ObjectInputStream oos = new ObjectInputStream(fos);
                world = World.class.cast(oos.readObject());
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void playerMovement(char key) {
        TERenderer ter = new TERenderer();
        if (key == 'w') {
            world.playerMove(world.player.position.x, world.player.position.y,
                    world.player.position.x, world.player.position.y + 1, world);
        } else if (key == 'a') {
            world.playerMove(world.player.position.x, world.player.position.y,
                    world.player.position.x - 1, world.player.position.y, world);
        } else if (key == 's') {
            world.playerMove(world.player.position.x, world.player.position.y,
                    world.player.position.x, world.player.position.y - 1, world);
        } else if (key == 'd') {
            world.playerMove(world.player.position.x, world.player.position.y,
                    world.player.position.x + 1, world.player.position.y, world);
        } else if (key == ':') {
            while (!(StdDraw.hasNextKeyTyped())) {
                StdDraw.pause(500);
            }
            if (StdDraw.hasNextKeyTyped()) {
                char inputQ = StdDraw.nextKeyTyped();
                if (inputQ == 'Q' || inputQ == 'q') {
                    saveMGStatus(world);
                    System.exit(0);
                }
            }
        } else if (key == 't') {
            if (pathShowing == false) {
                pathShowing = true;
            } else {
                pathShowing = false;
            }
        }
        ter.renderFrame(world.map);
    }

    private void hudMouse() {
        TERenderer ter = new TERenderer();
        int mouseX = (int) Math.floor(StdDraw.mouseX());
        int mouseY = (int) Math.floor(StdDraw.mouseY());
        Font fontBig = new Font("Bakersville", Font.BOLD, 10);
        if (!(mouseX >= WIDTH -1 || mouseX < 0 || mouseY > HEIGHT-1 || mouseY <0)) {
            if (world.map[mouseX][mouseY].equals(world.wall)) {
                ter.renderFrame(world.map);
                StdDraw.setPenColor(Color.white);
                StdDraw.setFont(fontBig);
                StdDraw.text(WIDTH / 2, HEIGHT + 1, "This is a wall."
                        + " Sorry, only Dr.Strange can pass through!");
            } else if (world.map[mouseX][mouseY].equals(world.floor)) {
                ter.renderFrame(world.map);
                StdDraw.setPenColor(Color.white);
                StdDraw.setFont(fontBig);
                StdDraw.text(WIDTH / 2, HEIGHT + 1, "Floor!"
                        + " Now go collect the stones!");
            } else if (world.map[mouseX][mouseY].equals(world.door)) {
                ter.renderFrame(world.map);
                StdDraw.setPenColor(Color.white);
                StdDraw.setFont(fontBig);
                StdDraw.text(WIDTH / 2, HEIGHT + 1, "Hello you, the player!");
            } else if (world.map[mouseX][mouseY].equals(world.avatar)) {
                ter.renderFrame(world.map);
                StdDraw.setPenColor(Color.white);
                StdDraw.setFont(fontBig);
                StdDraw.text(WIDTH / 2, HEIGHT + 1, "This is your enemy!"
                        + " Get the stones before him.");
            } else if (world.map[mouseX][mouseY].equals(world.flower)) {
                ter.renderFrame(world.map);
                StdDraw.setPenColor(Color.white);
                StdDraw.setFont(fontBig);
                StdDraw.text(WIDTH / 2, HEIGHT + 1, "Reality Stone,"
                        + " alters your perception of reality.");
            } else if (world.map[mouseX][mouseY].equals(world.tree)) {
                ter.renderFrame(world.map);
                StdDraw.setPenColor(Color.white);
                StdDraw.setFont(fontBig);
                StdDraw.text(WIDTH / 2, HEIGHT + 1, "Time Stone,"
                        + " controls and manipulates time.");
            } else if (world.map[mouseX][mouseY].equals(world.water)) {
                ter.renderFrame(world.map);
                StdDraw.setPenColor(Color.white);
                StdDraw.setFont(fontBig);
                StdDraw.text(WIDTH / 2, HEIGHT + 1, "Space Stone,"
                        + " used to teleport to different places.");
            } else if (world.map[mouseX][mouseY].equals(world.sand)) {
                ter.renderFrame(world.map);
                StdDraw.setPenColor(Color.white);
                StdDraw.setFont(fontBig);
                StdDraw.text(WIDTH / 2, HEIGHT + 1, "Mind Stone,"
                        + " controls minds");
            } else if (world.map[mouseX][mouseY].equals(world.mountain)) {
                ter.renderFrame(world.map);
                StdDraw.setPenColor(Color.white);
                StdDraw.setFont(fontBig);
                StdDraw.text(WIDTH / 2, HEIGHT + 1, "Soul Stone,"
                        + " must sacrifice a loved one in order to control souls");
            } else if (world.map[mouseX][mouseY].equals(world.grass)) {
                ter.renderFrame(world.map);
                StdDraw.setPenColor(Color.white);
                StdDraw.setFont(fontBig);
                StdDraw.text(WIDTH / 2, HEIGHT + 1, "Power Stone,"
                        + " increases your strength.");
            } else {
                ter.renderFrame(world.map);
                StdDraw.setPenColor(Color.white);
                StdDraw.setFont(fontBig);
                StdDraw.text(WIDTH / 2, HEIGHT + 1, "Wakanda Forever!");
            }
        }
        StdDraw.setFont(fontBig);
        StdDraw.text(WIDTH * 7 / 8, HEIGHT + 1, "Stone Count:" + world.player.stoneCount);
        String time = "Date: " + OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME.ofPattern
                ("EEE MMM d HH:mm:ss"));
        StdDraw.text(WIDTH * 1 / 8, HEIGHT + 1, time);
        Font font = new Font("Arial", Font.BOLD, 15);
        StdDraw.setFont(font);
        if (pathShowing == true) {
            for (Point tile : world.enemy.path) {
                StdDraw.text(tile.x, tile.y, "o");
            }
        }
        StdDraw.show();
    }
}