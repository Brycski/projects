package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Random;

import static byow.Core.RandomUtils.uniform;

public class World implements Serializable {

    TETile[][] map;

    int width;

    int height;

    private Random RANDOM;

    LinkedList<Stone> infinityStones = new LinkedList<>();

    Player player;

    Enemy enemy;

    TETile wall = Tileset.WALL;

    TETile floor = Tileset.FLOOR;

    TETile door = Tileset.LOCKED_DOOR;

    TETile avatar = Tileset.AVATAR;

    TETile flower = Tileset.FLOWER;

    TETile tree = Tileset.TREE;

    TETile water = Tileset.WATER;

    TETile sand = Tileset.SAND;

    TETile mountain = Tileset.MOUNTAIN;

    TETile grass = Tileset.GRASS;

    public World(int widthWorld, int heightWorld, Long seed) {
        width = widthWorld;
        height = heightWorld;
        RANDOM = new Random(seed);
        map = getNewWorld();
    }

    public TETile[][] getNewWorld() {
        TETile[][] world = new TETile[width][height];
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        LinkedList<Point> midPointList = new LinkedList<Point>();
        for (int i = 0; i < 800; i++) {
            int end = randomToWidth();
            int start = randomFromWidth(end);
            int top = randomToHeight();
            int bottom = randomFromHeight(top);
            if (end - start < 10) {
                if (top - bottom < 10) {
                    if (!(alreadyThere(world, start, end, bottom, top))) {
                        for (int w = start; w <= end; w++) {
                            for (int h = bottom; h <= top; h++) {
                                world[w][h] = Tileset.WALL;
                            }
                        }
                        for (int w = start + 1; w < end; w++) {
                            for (int h = bottom + 1; h < top; h++) {
                                world[w][h] = Tileset.FLOOR;
                            }
                        }
                        int heightMP = ((top + bottom) / 2);
                        int widthMP = ((end + start) / 2);
                        Point midPoint = new Point(widthMP, heightMP);
                        midPointList.add(midPoint);
                    }
                }
            }
        }
        hallways(world, midPointList);
        hallways(world, midPointList);
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                if (world[x][y] == Tileset.WALL) {
                    // above and below
                    if (y + 1 != height && y - 1 != -1) {
                        if (world[x][y + 1] == Tileset.FLOOR && world[x][y - 1] == Tileset.FLOOR) {
                            world[x][y] = Tileset.FLOOR;
                        }
                    }
                    // left and right
                    if (x + 1 != width && x - 1 != -1) {
                        if (world[x + 1][y] == Tileset.FLOOR && world[x - 1][y] == Tileset.FLOOR) {
                            world[x][y] = Tileset.FLOOR;
                        }
                    }
                }
            }
        }
        player = new Player(RANDOM, width, height, world, Tileset.LOCKED_DOOR);
        world[player.position.x][player.position.y] = player.playerSkin;
        spawnStones(world);
        Stone startStone = infinityStones.get(uniform(RANDOM, 0, infinityStones.size()));
        enemy = new Enemy(RANDOM, width, height, world, player, Tileset.AVATAR, startStone);
        world[enemy.position.x][enemy.position.y] = enemy.enemySkin;
        enemy.pathToStone();
        return world;
    }

    public boolean alreadyThere(TETile[][] world, int widthFrom,
                                int widthTo, int heightFrom, int heightTo) {
        for (int w = widthFrom; w <= widthTo; w++) {
            for (int h = heightFrom; h <= heightTo; h++) {
                if (!(world[w][h] == Tileset.NOTHING)) {
                    return true;
                }
            }
        }
        return false;
    }

    public int randomToWidth() {
        return uniform(RANDOM, 6, width);
    }

    public int randomFromWidth(int end) {
        return uniform(RANDOM, 1, end - 3);
    }

    public int randomToHeight() {
        return uniform(RANDOM, 6, height);
    }

    public int randomFromHeight(int top) {
        return uniform(RANDOM, 1, top - 3);
    }

    public void goUp(TETile[][] world, Point room, Point higherRoom) {
        int topSide = room.y;
        while (world[room.x][topSide] == Tileset.FLOOR) {
            topSide++;
        }
        world[room.x][topSide] = Tileset.FLOOR;
        if (world[room.x - 1][topSide] == Tileset.NOTHING) {
            world[room.x - 1][topSide] = Tileset.WALL;
        }
        if (world[room.x + 1][topSide] == Tileset.NOTHING) {
            world[room.x + 1][topSide] = Tileset.WALL;
        }
        int currHeight = topSide + 1;
        if (currHeight == height) {
            currHeight = topSide;
        }
        while (world[room.x][currHeight] == Tileset.NOTHING && currHeight != higherRoom.y + 1
                && currHeight != height - 1) {
            world[room.x][currHeight] = Tileset.FLOOR;
            world[room.x + 1][currHeight] = Tileset.WALL;
            world[room.x - 1][currHeight] = Tileset.WALL;
            currHeight++;
        }
        if (world[room.x][currHeight] == Tileset.WALL) {
            world[room.x][currHeight] = Tileset.FLOOR;
            if (world[room.x + 1][currHeight] == Tileset.NOTHING) {
                world[room.x + 1][currHeight] = Tileset.WALL;
            }
            if (world[room.x - 1][currHeight] == Tileset.NOTHING) {
                world[room.x - 1][currHeight] = Tileset.WALL;
            }
            if (currHeight + 1 != height - 1 && world[room.x][currHeight + 1] == Tileset.WALL) {
                world[room.x][currHeight + 1] = Tileset.FLOOR;
                if (world[room.x - 1][currHeight + 1] == Tileset.NOTHING) {
                    world[room.x - 1][currHeight + 1] = Tileset.WALL;
                }
                if (world[room.x + 1][currHeight + 1] == Tileset.NOTHING) {
                    world[room.x + 1][currHeight + 1] = Tileset.WALL;
                }
                if (world[room.x][currHeight + 2] == Tileset.NOTHING) {
                    world[room.x][currHeight + 2] = Tileset.WALL;
                }
            }
        }
        if (currHeight >= higherRoom.y + 1) {
            world[room.x][currHeight] = Tileset.WALL;
            if (world[room.x - 1][currHeight] == Tileset.NOTHING) {
                world[room.x - 1][currHeight] = Tileset.WALL;
            }
            if (world[room.x + 1][currHeight] == Tileset.NOTHING) {
                world[room.x + 1][currHeight] = Tileset.WALL;
            }
        }
        if (currHeight == height) {
            world[room.x][currHeight] = Tileset.WALL;
        }
    }

    public void goDown(TETile[][] world, Point room, Point lowerRoom) {
        int bottomSide = room.y;
        while (world[room.x][bottomSide] == Tileset.FLOOR) {
            bottomSide--;
        }
        world[room.x][bottomSide] = Tileset.FLOOR;
        if (world[room.x - 1][bottomSide] == Tileset.NOTHING) {
            world[room.x - 1][bottomSide] = Tileset.WALL;
        }
        if (world[room.x + 1][bottomSide] == Tileset.NOTHING) {
            world[room.x + 1][bottomSide] = Tileset.WALL;
        }
        int currHeight = bottomSide - 1;
        if (currHeight < 0) {
            currHeight = bottomSide;
        }
        while (world[room.x][currHeight] == Tileset.NOTHING && currHeight != lowerRoom.y - 1
                && currHeight != 0) {
            world[room.x][currHeight] = Tileset.FLOOR;
            world[room.x + 1][currHeight] = Tileset.WALL;
            world[room.x - 1][currHeight] = Tileset.WALL;
            currHeight--;
        }
        if (world[room.x][currHeight] == Tileset.WALL) {
            world[room.x][currHeight] = Tileset.FLOOR;
            if (world[room.x + 1][currHeight] == Tileset.NOTHING) {
                world[room.x + 1][currHeight] = Tileset.WALL;
            }
            if (world[room.x - 1][currHeight] == Tileset.NOTHING) {
                world[room.x - 1][currHeight] = Tileset.WALL;
            }
            if (currHeight - 1 != 0 && world[room.x][currHeight - 1] == Tileset.WALL) {
                world[room.x][currHeight - 1] = Tileset.FLOOR;
                if (world[room.x - 1][currHeight - 1] == Tileset.NOTHING) {
                    world[room.x - 1][currHeight - 1] = Tileset.WALL;
                }
                if (world[room.x + 1][currHeight - 1] == Tileset.NOTHING) {
                    world[room.x + 1][currHeight - 1] = Tileset.WALL;
                }
                if (world[room.x][currHeight - 2] == Tileset.NOTHING) {
                    world[room.x][currHeight - 2] = Tileset.WALL;
                }
            }
        }
        if (currHeight <= lowerRoom.y - 1) {
            world[room.x][currHeight] = Tileset.WALL;
            if (world[room.x - 1][currHeight] == Tileset.NOTHING) {
                world[room.x - 1][currHeight] = Tileset.WALL;
            }
            if (world[room.x + 1][currHeight] == Tileset.NOTHING) {
                world[room.x + 1][currHeight] = Tileset.WALL;
            }
        }
        if (currHeight == 0) {
            world[room.x][currHeight] = Tileset.WALL;
        }
    }

    public void goLeft(TETile[][] world, Point room, Point lefterRoom) {
        int leftSide = room.x;
        while (world[leftSide][room.y] == Tileset.FLOOR) {
            leftSide--;
        }
        world[leftSide][room.y] = Tileset.FLOOR;
        if (world[leftSide][room.y + 1] == Tileset.NOTHING) {
            world[leftSide][room.y + 1] = Tileset.WALL;
        }
        if (world[leftSide][room.y - 1] == Tileset.NOTHING) {
            world[leftSide][room.y - 1] = Tileset.WALL;
        }
        int currSpot = leftSide - 1;
        if (currSpot < 0) {
            currSpot = leftSide;
        }
        while (world[currSpot][room.y] == Tileset.NOTHING && currSpot != lefterRoom.x - 1
                && currSpot != 0) {
            world[currSpot][room.y] = Tileset.FLOOR;
            world[currSpot][room.y + 1] = Tileset.WALL;
            world[currSpot][room.y - 1] = Tileset.WALL;
            currSpot--;
        }
        if (world[currSpot][room.y] == Tileset.WALL) {
            world[currSpot][room.y] = Tileset.FLOOR;
            if (world[currSpot][room.y + 1] == Tileset.NOTHING) {
                world[currSpot][room.y + 1] = Tileset.WALL;
            }
            if (world[currSpot][room.y - 1] == Tileset.NOTHING) {
                world[currSpot][room.y - 1] = Tileset.WALL;
            }
            if (currSpot - 1 != 0 && world[currSpot - 1][room.y] == Tileset.WALL) {
                world[currSpot - 1][room.y] = Tileset.FLOOR;
                if (world[currSpot - 1][room.y + 1] == Tileset.NOTHING) {
                    world[currSpot - 1][room.y + 1] = Tileset.WALL;
                }
                if (world[currSpot - 1][room.y - 1] == Tileset.NOTHING) {
                    world[currSpot - 1][room.y - 1] = Tileset.WALL;
                }
                if (world[currSpot - 2][room.y] == Tileset.NOTHING) {
                    world[currSpot - 2][room.y] = Tileset.WALL;
                }
            }
        }
        if (currSpot <= lefterRoom.x - 1) {
            world[currSpot][room.y] = Tileset.WALL;
            if (world[currSpot][room.y + 1] == Tileset.NOTHING) {
                world[currSpot][room.y + 1] = Tileset.WALL;
            }
            if (world[currSpot][room.y - 1] == Tileset.NOTHING) {
                world[currSpot][room.y - 1] = Tileset.WALL;
            }
        }
        if (currSpot == 0) {
            world[currSpot][room.y] = Tileset.WALL;
        }
    }

    public void goRight(TETile[][] world, Point room, Point righterRoom) {
        int rightSide = room.x;
        while (world[rightSide][room.y] == Tileset.FLOOR) {
            rightSide++;
        }
        world[rightSide][room.y] = Tileset.FLOOR;
        if (world[rightSide][room.y + 1] == Tileset.NOTHING) {
            world[rightSide][room.y + 1] = Tileset.WALL;
        }
        if (world[rightSide][room.y - 1] == Tileset.NOTHING) {
            world[rightSide][room.y - 1] = Tileset.WALL;
        }
        int currSpot = rightSide + 1;
        if (currSpot == width) {
            currSpot = rightSide;
        }
        while (world[currSpot][room.y] == Tileset.NOTHING && currSpot != righterRoom.x + 1
                && currSpot != width - 1) {
            world[currSpot][room.y] = Tileset.FLOOR;
            world[currSpot][room.y + 1] = Tileset.WALL;
            world[currSpot][room.y - 1] = Tileset.WALL;
            currSpot++;
        }
        if (world[currSpot][room.y] == Tileset.WALL) {
            world[currSpot][room.y] = Tileset.FLOOR;
            if (world[currSpot][room.y + 1] == Tileset.NOTHING) {
                world[currSpot][room.y + 1] = Tileset.WALL;
            }
            if (world[currSpot][room.y - 1] == Tileset.NOTHING) {
                world[currSpot][room.y - 1] = Tileset.WALL;
            }
            if (currSpot + 1 != width - 1 && world[currSpot + 1][room.y] == Tileset.WALL) {
                world[currSpot + 1][room.y] = Tileset.FLOOR;
                if (world[currSpot + 1][room.y + 1] == Tileset.NOTHING) {
                    world[currSpot + 1][room.y + 1] = Tileset.WALL;
                }
                if (world[currSpot + 1][room.y - 1] == Tileset.NOTHING) {
                    world[currSpot + 1][room.y - 1] = Tileset.WALL;
                }
                if (world[currSpot + 2][room.y] == Tileset.NOTHING) {
                    world[currSpot + 2][room.y] = Tileset.WALL;
                }
            }
        }
        if (currSpot >= righterRoom.x + 1) {
            world[currSpot][room.y] = Tileset.WALL;
            if (world[currSpot][room.y + 1] == Tileset.NOTHING) {
                world[currSpot][room.y + 1] = Tileset.WALL;
            }
            if (world[currSpot][room.y - 1] == Tileset.NOTHING) {
                world[currSpot][room.y - 1] = Tileset.WALL;
            }
        }
        if (currSpot == width) {
            world[currSpot][room.y] = Tileset.WALL;
        }
    }

    public void hallways(TETile[][] world, LinkedList<Point> midPointList) {
        for (Point room : midPointList) {
            Point otherRoom = midPointList.get(uniform(RANDOM, 0, midPointList.size()));
            if (room.y - otherRoom.y < 0) {
                if (room.x - otherRoom.x < 0) {
                    // otherRoom is higher and to right
                    if (Math.abs(room.y - otherRoom.y) > Math.abs(room.x - otherRoom.x)) {
                        //go up
                        goUp(world, room, otherRoom);
                    } else {
                        // go right
                        goRight(world, room, otherRoom);
                    }
                } else if (room.x - otherRoom.x > 0) {
                    // otherRoom is higher and to left
                    if (Math.abs(room.y - otherRoom.y) > Math.abs(room.x - otherRoom.x)) {
                        // go up
                        goUp(world, room, otherRoom);
                    } else {
                        // go left
                        goLeft(world, room, otherRoom);
                    }
                } else {
                    // go up
                    goUp(world, room, otherRoom);
                }
            } else if (room.y - otherRoom.y > 0) {
                if (room.x - otherRoom.x < 0) {
                    // otherRoom is lower and to right
                    if (Math.abs(room.y - otherRoom.y) > Math.abs(room.x - otherRoom.x)) {
                        // go down
                        goDown(world, room, otherRoom);
                    } else {
                        // go right
                        goRight(world, room, otherRoom);
                    }
                } else if (room.x - otherRoom.x > 0) {
                    // otherRoom is lower to left
                    if (Math.abs(room.y - otherRoom.y) > Math.abs(room.x - otherRoom.x)) {
                        // go down
                        goDown(world, room, otherRoom);
                    } else {
                        // go left
                        goLeft(world, room, otherRoom);
                    }
                } else {
                    //built down
                    goDown(world, room, otherRoom);
                }
            } else {
                if (room.x - otherRoom.x < 0) {
                    // go right
                    goRight(world, room, otherRoom);
                } else {
                    // go left
                    goLeft(world, room, otherRoom);
                }
            }
        }
    }

    public void playerMove(int fromX, int fromY, int toX, int toY, World world) {
        if (toX == width - 1 || toX == -1 || toY == height - 1 || toY == -1) {
            return;
        }
        if (!(world.map[toX][toY].equals(floor))) {
            if (world.map[toX][toY].equals(wall)) {
                return;
            } else {
                // IF STONE COLLECTED
                Stone saveStone = null;
                for (int i = 0; i < infinityStones.size(); i++) {
                    if (infinityStones.get(i).position.x == toX && infinityStones.get(i).position.y == toY) {
                        saveStone = infinityStones.get(i);
                        StoneEffects.runEffects("/Users/bryceszarzynski/cs61bl/su22-p214/proj3/byow/Res/Infinity Stone Sound.wav");
                        if (world.enemy.preferredStone.position.x == toX
                                && world.enemy.preferredStone.position.y == toY) {
                            if (infinityStones.contains(saveStone)) {
                                infinityStones.remove(saveStone);
                            }
                            enemy.changePreferredStone(RANDOM, world, saveStone);
                            break;
                        }
                    }
                }
                if (infinityStones.contains(saveStone)) {
                    infinityStones.remove(saveStone);
                }
                world.map[toX][toY] = floor;
                player.stoneCount++;
            }
        }
        Point enemyNextMove = enemy.path.poll();
        if (toX == enemyNextMove.x && toY == enemyNextMove.y) {
            int newEnemyPosX = enemy.position.x;
            int newEnemyPosY = enemy.position.y;
            if (fromY - toY > 0) {
                int count = 0;
                while (count != 3
                        && world.map[newEnemyPosX][newEnemyPosY - 1] != wall) {
                    newEnemyPosY--;
                    count++;
                }
            } else if (fromX - toX > 0) {
                int count = 0;
                while (count != 3
                        && world.map[newEnemyPosX - 1][newEnemyPosY] != wall) {
                    newEnemyPosX--;
                    count++;
                }
            } else if (fromX - toX < 0) {
                int count = 0;
                while (count != 3
                        && world.map[newEnemyPosX + 1][newEnemyPosY] != wall) {
                    newEnemyPosX++;
                    count++;
                }
            } else {
                int count = 0;
                while (count != 3
                        && world.map[newEnemyPosX][newEnemyPosY + 1] != wall) {
                    newEnemyPosY++;
                    count++;
                }
            }
            world.map[enemy.position.x][enemy.position.y] = floor;
            enemy.position = new Point(newEnemyPosX, newEnemyPosY);
            world.map[newEnemyPosX][newEnemyPosY] = avatar;
            enemy.pathToStone();
            return;
        }
        for (int i = 0; i < infinityStones.size(); i++) {
            if (infinityStones.get(i).position.x == enemyNextMove.x
                    && infinityStones.get(i).position.y == enemyNextMove.y
                    && (!(enemyNextMove.x == enemy.preferredStone.position.x))
                    && (!(enemyNextMove.y == enemy.preferredStone.position.y))) {
                ironManmusic.imMusic("/Users/bryceszarzynski/cs61bl/su22-p214/proj3/byow/Res/proj3_byow_Res_iamIM.wav");
                TERenderer ter = new TERenderer();
                ter.initialize(width, height);
                StdDraw.clear(Color.RED);
                StdDraw.picture(width/2,height/2,
                        "/Users/bryceszarzynski/cs61bl/su22-p214/proj3/byow/Core/images/yeyeye.png");
                StdDraw.show();
                StdDraw.pause(13000);
            }
        }
        if (enemyNextMove.x == enemy.preferredStone.position.x
                && enemyNextMove.y == enemy.preferredStone.position.y) {
            TERenderer ter = new TERenderer();
            ter.initialize(width, height);
            StdDraw.clear(Color.BLACK);
            StdDraw.setPenColor(Color.WHITE);
            Font fontBig = new Font("Baskerville", Font.BOLD, 20);
            StdDraw.setFont(fontBig);
            StdDraw.text(width / 2, height / 2, "Game Over :(");
            StdDraw.show();
            StdDraw.pause(5000);
            System.exit(0);
        }
        world.map[fromX][fromY] = floor;
        world.map[enemyNextMove.x][enemyNextMove.y] = avatar;
        world.map[enemy.position.x][enemy.position.y] = floor;
        enemy.position = new Point(enemyNextMove.x, enemyNextMove.y);
        world.map[toX][toY] = door;
        player.position = new Point(toX, toY);
    }

    public void spawnStones(TETile[][] world) {
        //1
        Stone stoneOne = new Stone(RANDOM, width, height, world);
        infinityStones.add(stoneOne);
        world[stoneOne.position.x][stoneOne.position.y] = flower;
        //2
        Stone stoneTwo = new Stone(RANDOM, width, height, world);
        infinityStones.add(stoneTwo);
        world[stoneTwo.position.x][stoneTwo.position.y] = tree;
        //3
        Stone stoneThree = new Stone(RANDOM, width, height, world);
        infinityStones.add(stoneThree);
        world[stoneThree.position.x][stoneThree.position.y] = mountain;
        //4
        Stone stoneFour = new Stone(RANDOM, width, height, world);
        infinityStones.add(stoneFour);
        world[stoneFour.position.x][stoneFour.position.y] = water;
        //5
        Stone stoneFive = new Stone(RANDOM, width, height, world);
        infinityStones.add(stoneFive);
        world[stoneFive.position.x][stoneFive.position.y] = sand;
        //6
        Stone stoneSix = new Stone(RANDOM, width, height, world);
        infinityStones.add(stoneSix);
        world[stoneSix.position.x][stoneSix.position.y] = grass;
    }
}