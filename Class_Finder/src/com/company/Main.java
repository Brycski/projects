package com.company;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;

public class Main {

    File Edges;

    public static void main(String[] args) {
	// write your code here
    }

    public Point[] Nodes(File Edges, int length) {
        Point[] result = new Point[length];
        return result;
    }



    public void pathAlgorithm() {
        MinHeapPQ<Integer> fringe = new MinHeapPQ<Integer>();
        LinkedList<Point> result = new LinkedList<Point>();
        //change length
        Point[] nodes = Nodes(Edges, 100);
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

    public class Point {
        int x;
        int y;

        public Point(int xPoint, int yPoint) {
            x = xPoint;
            y = yPoint;
        }
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
