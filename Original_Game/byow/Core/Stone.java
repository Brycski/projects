package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.Serializable;
import java.util.Random;

import static byow.Core.RandomUtils.uniform;

public class Stone implements Serializable {

    Point position;

    private TETile[][] worldTiles;

    int width;

    int height;

    public Stone(Random random, int widthWorld,
                 int heightWorld, TETile[][] world) {
        width = widthWorld;
        height = heightWorld;
        worldTiles = world;
        Point stonePoint = spawn(random);
        while (!(worldTiles[stonePoint.x][stonePoint.y] == Tileset.FLOOR)) {
            stonePoint = spawn(random);
        }
        position = stonePoint;
    }

    public Point spawn(Random random) {
        int x = uniform(random, 1, width);
        int y = uniform(random, 1, height);
        return new Point(x, y);
    }
}
