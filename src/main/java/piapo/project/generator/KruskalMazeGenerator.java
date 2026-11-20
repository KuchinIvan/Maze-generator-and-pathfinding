package piapo.project.generator;

import piapo.project.model.CellType;
import piapo.project.model.Maze;
import piapo.project.model.Position;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static piapo.project.model.CellType.*;
import static piapo.project.model.CellType.FINISH;
import static piapo.project.model.CellType.START;

public class KruskalMazeGenerator implements MazeGenerator{

    private final SecureRandom secureRandom = new SecureRandom();

    private record Edge(Position A, Position B, Position Wall){};

    private static class DSU {

        private final int[] parent;
        private final int[] rank;

        private DSU(int size) {
            this.parent = new int[size];
            this.rank = new int[size];

            for (int i = 0; i < size; i++) {
                parent[i] = i;
            }
        }

        private int findParent(int index) {
            if (parent[index] == index) {
                return index;
            }
            parent[index] = findParent(parent[index]);
            return parent[index];
        }

        private boolean union(int el1, int el2) {

            int p1 = findParent(el1);
            int p2 = findParent(el2);

            if (p1 == p2) {
                return false;
            }
            if (rank[p1] < rank[p2]) {
                parent[p1] = p2;
            } else if (rank[p2] < rank[p1]) {
                parent[p2] = p1;
            } else {
                parent[p2] = p1;
                rank[p1]++;
            }
            return true;
        }
    }

    @Override
    public Maze generateMaze(int width, int height) {

        Maze maze = new Maze(width, height);
        DSU dsu = new DSU(width*height);
        List<Edge> walls = new ArrayList<>();
        maze.addGenerationStep(maze.getStart(), START);

        // в будущем заменить на мап функцию через стрим апи, пока так
        for (int x = 0; x < width; x+=2) {
            for (int y = 0; y < height; y+=2) {
                if (maze.isInside(x, y)) {
                    maze.setCellType(x, y, PASSAGE);
                    maze.addGenerationStep(new Position(x, y), PASSAGE);
                }
                if (maze.isInside(x+2, y)) {
                    Edge e = new Edge(new Position(x,y), new Position(x+2, y), new Position(x+1, y));
                    walls.add(e);
                }
                if (maze.isInside(x, y+2)) {
                    Edge e = new Edge(new Position(x,y), new Position(x, y+2), new Position(x, y+1));
                    walls.add(e);
                }
            }
        }

        Collections.shuffle(walls, secureRandom);

        for (Edge el: walls) {
            int indexA = el.A.y() * width + el.A.x();
            int indexB = el.B.y() * width + el.B.x();
            if (dsu.findParent(indexA) != dsu.findParent(indexB)) {
                maze.setCellType(el.Wall.x(), el.Wall.y(), PASSAGE);
                maze.addGenerationStep(el.Wall, PASSAGE);
                dsu.union(indexA, indexB);
            }
        }


        int finishLeftX = maze.getFinish().x()-1;
        int finishLeftY = maze.getFinish().y();
        int finishUpX = maze.getFinish().x();
        int finishUpY = maze.getFinish().y()-1;

        List<Position> finalCells = new ArrayList<>();
        Position finishLeftCell = new Position(finishLeftX, finishLeftY);
        Position finishUpperCell = new Position(finishUpX, finishUpY);
        boolean hasPathToFinish = false;

        if (maze.isInside(finishLeftX, finishLeftY)) {
            if (maze.getCellType(finishLeftX, finishLeftY) == WALL) {
                finalCells.add(finishLeftCell);
            } else {
                hasPathToFinish = true;
            }
        }
        if (maze.isInside(finishUpX, finishUpY)) {
            if (maze.getCellType(finishUpX, finishUpY) == WALL) {
                finalCells.add(finishUpperCell);
            } else {
                hasPathToFinish = true;
            }
        }

        if (!hasPathToFinish && finalCells.size() != 0) {
            Position finishWall = finalCells.get(secureRandom.nextInt(finalCells.size()));
            maze.setCellType(finishWall.x(), finishWall.y(), PASSAGE);
            maze.addGenerationStep(finishWall, PASSAGE);
        }


        maze.addGenerationStep(maze.getFinish(), FINISH);
        maze.setCellType(0,0, START);
        maze.setCellType(width-1, height-1, FINISH);

        return maze;
    }
}
