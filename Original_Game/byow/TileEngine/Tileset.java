package byow.TileEngine;


import java.awt.*;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
    public static final TETile AVATAR = new TETile('@', Color.white, Color.black, "you",
            "/Users/bryceszarzynski/cs61bl/su22-p214/proj3/byow/Core/images/ironman.png");
    public static final TETile WALL = new TETile('#', new Color(216, 128, 128), Color.darkGray,
            "wall",
            "/Users/bryceszarzynski/cs61bl/su22-p214/proj3/byow/Core/images/wall.png");
    public static final TETile FLOOR = new TETile('·', new Color(128, 192, 128), Color.black,
            "floor",
            "/Users/bryceszarzynski/cs61bl/su22-p214/proj3/byow/Core/images/floor.png");
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing",
            "/Users/bryceszarzynski/cs61bl/su22-p214/proj3/byow/Core/images/nothing.png");
    public static final TETile GRASS = new TETile('"', Color.green, Color.black, "grass",
            "/Users/bryceszarzynski/cs61bl/su22-p214/proj3/byow/Core/images/powerstone.png");
    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, "water",
            "/Users/bryceszarzynski/cs61bl/su22-p214/proj3/byow/Core/images/spacestone.png");
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink, "flower",
            "/Users/bryceszarzynski/cs61bl/su22-p214/proj3/byow/Core/images/realitystone.png");
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            "locked door",
            "/Users/bryceszarzynski/cs61bl/su22-p214/proj3/byow/Core/images/babythan.png");
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            "unlocked door");
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand",
            "/Users/bryceszarzynski/cs61bl/su22-p214/proj3/byow/Core/images/mindstone.png");
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "mountain",
            "/Users/bryceszarzynski/cs61bl/su22-p214/proj3/byow/Core/images/soulstone.png");
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "tree",
            "/Users/bryceszarzynski/cs61bl/su22-p214/proj3/byow/Core/images/timestone.png");


}