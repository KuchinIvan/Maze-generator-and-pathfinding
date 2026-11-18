package piapo.project.generator;

import piapo.project.model.AlgorithmStep;
import piapo.project.model.Maze;
import piapo.project.model.Position;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static piapo.project.model.CellType.*;

public class DfsMazeGenerator implements MazeGenerator {

    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public Maze generateMaze(int width, int height) {

        // тут хранятся клетки которые прошел алгоритм и имеют возможных соседей
        Stack <Position> stack = new Stack<>();


        Maze maze = new Maze(width, height);
        Position start = maze.getStart();
        stack.push(start);
        maze.addGenerationStep(start, START);
        maze.setCellType(0,0,PASSAGE);

        while (!stack.isEmpty()) {
            Position current = stack.peek();

            List<Position> neighbors = new ArrayList<>();
            if (maze.isInside(current.x()-2, current.y()) && maze.getCellType(current.x()-2, current.y()) == WALL) {
                neighbors.add(new Position(current.x()-2, current.y()));
            }
            if (maze.isInside(current.x(), current.y()-2) && maze.getCellType(current.x(), current.y()-2) == WALL) {
                neighbors.add(new Position(current.x(), current.y()-2));
            }
            if (maze.isInside(current.x()+2, current.y()) && maze.getCellType(current.x()+2, current.y()) == WALL) {
                neighbors.add(new Position(current.x()+2, current.y()));
            }
            if (maze.isInside(current.x(), current.y()+2) && maze.getCellType(current.x(), current.y()+2) == WALL) {
                neighbors.add(new Position(current.x(), current.y()+2));
            }

            if (!neighbors.isEmpty()) {

                Position next = neighbors.get(secureRandom.nextInt(neighbors.size()));
                maze.setCellType(next.x(), next.y(), PASSAGE);

                Position between = new Position((current.x() + next.x())/2, (current.y()+next.y())/2);
                maze.setCellType((current.x() + next.x())/2, (current.y()+next.y())/2, PASSAGE);
                stack.push(next);

                maze.addGenerationStep(between, PASSAGE);
                maze.addGenerationStep(next, PASSAGE);


            } else {
                stack.pop();
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
