package piapo.project.model;

import java.util.ArrayList;
import java.util.Arrays;

public class Maze {
    private final int width;
    private final int height;
    private final CellType[][] grid;
    private final ArrayList<AlgorithmStep> generationSteps;
    private Position start;
    private Position finish;

    public Maze(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new  CellType[height][width];
        this.generationSteps = new ArrayList<>();
        this.start = new Position(0, 0);
        this.finish = new Position(width-1, height-1);

        for (int y = 0; y < height; y++) {
            Arrays.fill(this.grid[y], CellType.WALL);
        }
    }
}
