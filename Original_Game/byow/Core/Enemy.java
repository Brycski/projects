package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.Serializable;
import java.util.*;

import static byow.Core.RandomUtils.uniform;

public class Enemy implements Serializable {

    TETile enemySkin;

    Stone preferredStone;

    Point position;

    TETile[][] worldTiles;

    int width;

    int height;

    LinkedList<Point> path;

    TETile floor = Tileset.FLOOR;

    TETile wall = Tileset.WALL;

    TETile nothing = Tileset.NOTHING;

    public Enemy(Random random, int widthWorld, int heightWorld,
                 TETile[][] world, Player player, TETile skin, Stone start) {
        width = widthWorld;
        height = heightWorld;
        worldTiles = world;
        preferredStone = start;
        position = spawn(random, player);
        enemySkin = skin;
    }

    public Point spawn(Random random, Player player) {
        int totalDis = Math.abs(preferredStone.position.x - player.position.x)
                + Math.abs(preferredStone.position.y - player.position.y);
        int currX = 0;
        int currY = 0;
        int currEnemyDis = 0;
        while (currEnemyDis < totalDis || worldTiles[currX][currY] != Tileset.FLOOR) {
            currX = uniform(random, 1, width);
            currY = uniform(random, 1, height);
            currEnemyDis = Math.abs(preferredStone.position.x - currX)
                    + Math.abs(preferredStone.position.y - currY);
        }
        return new Point(currX, currY);
    }

    public Point[] goodTiles(int length) {
        Point[] result = new Point[length + 8];
        Point start = position;
        Point curr = start;
        int index = 0;
        int otherIndex = 0;
        while (index < result.length) {
            if (curr.y + 1 != height) {
                boolean contains = false;
                for (int i = 0; i < index; i++) {
                    if (result[i].x == curr.x && result[i].y == curr.y + 1) {
                        contains = true;
                    }
                }
                if ((worldTiles[curr.x][curr.y + 1] != wall
                        && worldTiles[curr.x + 1][curr.y] != Tileset.NOTHING) && (!(contains))) {
                    result[index] = new Point(curr.x, curr.y + 1);
                    index++;
                }
            }
            //down
            if (curr.y - 1 != -1) {
                boolean contains = false;
                for (int i = 0; i < index; i++) {
                    if (result[i].x == curr.x && result[i].y == curr.y - 1) {
                        contains = true;
                    }
                }
                if ((worldTiles[curr.x][curr.y - 1] != wall
                        && worldTiles[curr.x + 1][curr.y] != Tileset.NOTHING) && (!(contains))) {
                    result[index] = new Point(curr.x, curr.y - 1);
                    index++;
                }
            }
            //left
            if (curr.x - 1 != -1) {
                boolean contains = false;
                for (int i = 0; i < index; i++) {
                    if (result[i].x == curr.x - 1 && result[i].y == curr.y) {
                        contains = true;
                    }
                }
                if ((worldTiles[curr.x - 1][curr.y] != wall
                        && worldTiles[curr.x + 1][curr.y] != Tileset.NOTHING) && (!(contains))) {
                    result[index] = new Point(curr.x - 1, curr.y);
                    index++;
                }
            }
            //right
            if (curr.x + 1 != width) {
                boolean contains = false;
                for (int i = 0; i < index; i++) {
                    if (result[i].x == curr.x + 1 && result[i].y == curr.y) {
                        contains = true;
                    }
                }
                if ((worldTiles[curr.x + 1][curr.y] != wall
                        && worldTiles[curr.x + 1][curr.y] != Tileset.NOTHING) && (!(contains))) {
                    result[index] = new Point(curr.x + 1, curr.y);
                    index++;
                }
            }
            if (index == length + 7) {
                result[index] = start;
                break;
            }
            otherIndex++;
            curr = result[otherIndex];
            if (curr == null) {
                result[otherIndex] = start;
                int count = 0;
                for (int i = otherIndex + 1; i < result.length; i++) {
                    if (result[i] == null) {
                        count++;
                    }
                }
                Point[] newResult = new Point[result.length - count];
                System.arraycopy(result, 0, newResult, 0, result.length - count);
                return newResult;
            }
        }
        return result;
    }

    public int floorTiles() {
        int count = 0;
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                if (worldTiles[x][y] == floor) {
                    count++;
                }
            }
        }
        return count;
    }

    public LinkedList<Point> neighbors(Point point, Point[] nodes) {
        Point[] tiles = nodes;
        LinkedList<Point> result = new LinkedList<Point>();
        //up
        if (point.y + 1 != height) {
            for (Point spot : tiles) {
                if (spot.x == point.x && spot.y == point.y + 1) {
                    result.add(new Point(point.x, point.y + 1));
                }
            }
        }
        //down
        if (point.y - 1 != -1) {
            for (Point spot : tiles) {
                if (spot.x == point.x && spot.y == point.y - 1) {
                    result.add(new Point(point.x, point.y - 1));
                }
            }
        }
        //left
        if (point.x - 1 != -1) {
            for (Point spot : tiles) {
                if (spot.x == point.x - 1 && spot.y == point.y) {
                    result.add(new Point(point.x - 1, point.y));
                }
            }
        }
        //right
        if (point.x + 1 != width) {
            for (Point spot : tiles) {
                if (spot.x == point.x + 1 && spot.y == point.y) {
                    result.add(new Point(point.x + 1, point.y));
                }
            }
        }
        return result;
    }

    public LinkedList<Edge>[] edgeList(Point[] spots) {
        LinkedList<Edge>[] result = (LinkedList<Edge>[]) new LinkedList[spots.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = new LinkedList<Edge>();
        }
        for (int i = 0; i < spots.length; i++) {
            for (Point neighbor : neighbors(spots[i], spots)) {
                for (int v = 0; v < spots.length; v++) {
                    if (spots[v].x == neighbor.x && spots[v].y == neighbor.y) {
                        if (i != v) {
                            result[i].add(new Edge(i, v,
                                    (Math.abs(preferredStone.position.x - spots[v].x)
                                            + Math.abs(preferredStone.position.y - spots[v].y))));
                        }
                    }
                }
            }
        }
        return result;
    }


    public void pathToStone() {
        MinHeapPQ<Integer> fringe = new MinHeapPQ<Integer>();
        LinkedList<Point> result = new LinkedList<Point>();
        Point[] nodes = goodTiles(floorTiles());
        int[] distTo = new int[nodes.length];
        int[] edgeTo = new int[nodes.length];
        LinkedList<Edge>[] edges = edgeList(nodes);
        int start = 0;
        int stop = nodes.length - 1;
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i].x == position.x && nodes[i].y == position.y) {
                fringe.insert(i, 0);
                distTo[i] = 0;
                start = i;
            } else if (nodes[i].x == preferredStone.position.x && nodes[i].y == preferredStone.position.y) {
                stop = i;
                fringe.insert(i, Integer.MAX_VALUE);
                distTo[i] = Integer.MAX_VALUE;
            } else {
                fringe.insert(i, Integer.MAX_VALUE);
                distTo[i] = Integer.MAX_VALUE;
            }
        }
        while (fringe.size() != 0) {
            int v = fringe.poll();
            if (v == stop) {
                int next = stop;
                while (next != 0) {
                    result.add(nodes[next]);
                    next = edgeTo[next];
                }
                result.add(nodes[next]);
                Collections.reverse(result);
                path = result;
                return;
            }
            for (Point neighbor : neighbors(nodes[v], nodes)) {
                int neigh = 0;
                for (int i = 0; i < nodes.length; i++) {
                    if (nodes[i].x == neighbor.x && nodes[i].y == neighbor.y) {
                        neigh = i;
                        break;
                    }
                }
                int neighWeight = 0;
                for (Edge edge : edges[v]) {
                    if (edge.to == neigh) {
                        neighWeight = edge.weight;
                        break;
                    }
                }
                if (distTo[v] + neighWeight < distTo[neigh]) {
                    distTo[neigh] = distTo[v] + neighWeight;
                    edgeTo[neigh] = v;
                    fringe.changePriority(neigh, distTo[neigh]
                            + ((Math.abs(nodes[neigh].x - preferredStone.position.x)
                            + Math.abs(nodes[neigh].y - preferredStone.position.y)) / 2));
                }
            }
        }
    }

    public void changePreferredStone(Random random, World world, Stone collected) {
        while (preferredStone.position.x == collected.position.x
                && preferredStone.position.y == collected.position.y) {
            preferredStone = world.infinityStones.get(uniform(random,
                    0, world.infinityStones.size()));
        }
        pathToStone();
    }

    private class Edge {

        private int from;
        private int to;
        private int weight;

        Edge(int from, int to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }

}