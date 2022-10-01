package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.Serializable;
import java.util.Random;

import static byow.Core.RandomUtils.uniform;

public class Player implements Serializable {

    TETile playerSkin;

    Point position;

    TETile[][] worldTiles;

    int width;

    int height;

    int stoneCount = 0;

    public Player(Random random, int widthWorld,
                  int heightWorld, TETile[][] world, TETile skin) {
        width = widthWorld;
        height = heightWorld;
        worldTiles = world;
        Point spawnPoint = spawn(random);
        while (!(worldTiles[spawnPoint.x][spawnPoint.y] == Tileset.FLOOR)) {
            spawnPoint = spawn(random);
        }
        position = spawnPoint;
        playerSkin = skin;
    }

    public Point spawn(Random random) {
        int x = uniform(random, 1, width);
        int y = uniform(random, 1, height);
        return new Point(x, y);
    }
}