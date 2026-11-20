package piapo.project.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Maze {
    private final int width;
    private final int height;
    private final CellType[][] grid;
    private final List<AlgorithmStep> generationSteps;
    private Position start;
    private Position finish;

    public Maze(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new CellType[height][width];
        this.generationSteps = new ArrayList<>();
        this.start = new Position(0, 0);
        this.finish = new Position(width-1, height-1);

        for (int y = 0; y < height; y++) {
            Arrays.fill(this.grid[y], CellType.WALL);
        }
    }

    // геттеры

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public CellType[][] getGrid() {
        return grid;
    }

    public List<AlgorithmStep> getGenerationSteps() {
        return generationSteps;
    }

    public Position getStart() {
        return start;
    }

    public Position getFinish() {
        return finish;
    }

    public CellType getCellType(int x, int y) {
        if (isInside(x, y)) {
            return grid[y][x];
        }
        return CellType.WALL;
    }

    // сеттеры

    public void setCellType(int x, int y, CellType type) {
        if (isInside(x, y)) {
            grid[y][x] = type;
        }
    }

    // прочие методы

    public boolean isInside(int x, int y) {
        if (x >= 0 && y >= 0 && x < this.width && y < this.height) {
            return true;
        } else {
            return false;
        }
    }

//    public boolean isInside(Position p) {
//        if (p.x() >= 0 && p.x() < this.width && p.y() >= 0 && p.y() < this.height) {
//            return true;
//        } else {
//            return false;
//        }
//    }


    public void addGenerationStep(Position position, CellType cellType) {
        AlgorithmStep step = new AlgorithmStep(position, cellType);
        generationSteps.add(step);
    }

}
