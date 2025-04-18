package com.example.catsandmazes;

import java.util.Objects;
import java.util.LinkedList;

// This class represents one node of the maze
class Node {

    public int x;
    public int y;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != this.getClass()) {
            return false;
        }

        Node other = (Node) obj;
        return other.x == this.x && other.y == this.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Node{x=" + x + ", y=" + y + "}";
    }
}

public class FloodFill {
    private final int[][] maze;
    private LinkedList<Node> best_path;
    // private Scanner scanner; // This attribute is only for debugging purposes

    public FloodFill(int[][] maze) {
        // Ensures that the maze is square
        assert maze[0].length == maze.length : "Mazes must be square";

        this.maze = maze;
    }

    public int[][] getSolution() {
        int[][] solution = new int[maze.length][maze[0].length];

        for (int row = 0; row < maze.length; row++) {
            for (int col = 0; col < maze[row].length; col++) {
                if (best_path.contains(new Node(col, row)) && maze[row][col] == 1) {
                    solution[row][col] = 4;
                } else {
                    solution[row][col] = maze[row][col];
                }
            }
        }

        return solution;
    }

    /*
     * This method finds the most optimal solution to a the maze.
     */
    public int solve_maze() {
        // create the start node and path list and add the start node to the path list
        Node start = new Node(-1, -1);
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if (maze[i][j] == 2) {
                    start = new Node(j, i);
                    break;
                }
            }
            if (start.x != -1 || start.y != -1) {
                break;
            }
        }

        LinkedList<Node> path = new LinkedList<Node>();
        path.add(start);

        // get the number of nodes for the optimal route
        return solve(start, path);
    }

    /* This method is the recursive method that actually solves the maze
     *
     * For the given node and the given previous path, it checks all four adjacent nodes for paths to the end.
     */
    private int solve(Node node, LinkedList<Node> path) {
        // System.out.print("Node: ");
        // System.out.print(node.x);
        // System.out.print(",");
        // System.out.println(node.y);
        // System.out.print("Current path length: ");
        // System.out.println(path.size());
        // printMaze();

        // scanner.nextLine();

        // base case: if the current node is 3 (the end) return the size of the path
        if (maze[node.y][node.x] == 3) {
            if (best_path == null) {
                best_path = (LinkedList<Node>) path.clone();
            } else if (path.size() < best_path.size()) {
                best_path = (LinkedList<Node>) path.clone();
            }
            return path.size();
        }

        // this array is used to hold the path lengths for each path coming from the adjacent nodes
        int[] lengths = new int[4];
        // North node
        if (node.y - 1 >= 0) { // check bounds
            // System.out.println();
            Node north_node = new Node(node.x, node.y - 1);

            // if the new node is not in the previously traced path and it is not a wall (0) then add it to the path and call solve on it
            if (!path.contains(north_node) && maze[north_node.y][north_node.x] != 0) {
                path.add(north_node);
                lengths[0] = solve(north_node, path);
                // remove the node after the path has been found. This is necessary so the new paths that we find are the correct size
                path.remove(path.size() - 1);
            } else {
                lengths[0] = 0;
            }
        }

        // South node
        if (node.y + 1 < maze.length) {
            // System.out.println();
            Node south_node = new Node(node.x, node.y + 1);
            if (!path.contains(south_node) && maze[south_node.y][south_node.x] != 0) {
                path.add(south_node);
                lengths[1] = solve(south_node, path);
                path.remove(path.size() - 1);
            } else {
                lengths[1] = 0;
            }
        }

        // West node
        if (node.x + 1 < maze.length) {
            // System.out.println();
            Node west_node = new Node(node.x + 1, node.y);
            if (!path.contains(west_node) && maze[west_node.y][west_node.x] != 0) {
                path.add(west_node);
                lengths[2] = solve(west_node, path);
                // System.out.println(lengths[2]);
                path.remove(path.size() - 1);
            } else {
                lengths[2] = 0;
            }
        }

        // East node
        if (node.x - 1 >= 0) {
            // System.out.println();
            Node east_node = new Node(node.x - 1, node.y);
            if (!path.contains(east_node) && maze[east_node.y][east_node.x] != 0) {
                path.add(east_node);
                lengths[3] = solve(east_node, path);
                path.remove(path.size() - 1);
            } else {
                lengths[3] = 0;
            }
        }

        // get the minimum, nonzero value of the lengths array
        int min = 0;
        for (int len : lengths) {
            if (min == 0) {
                min = len;
            } else if (len < min && len != 0) {
                min = len;
            }
        }

        // return the minimum path from the current node to the end
        return min;
    }
}
