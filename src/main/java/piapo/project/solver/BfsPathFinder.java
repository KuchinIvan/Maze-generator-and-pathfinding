package piapo.project.solver;

import piapo.project.model.AlgorithmStep;
import piapo.project.model.CellType;
import piapo.project.model.Maze;
import piapo.project.model.PathResult;
import piapo.project.model.Position;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class BfsPathFinder implements PathFinder {

    @Override
    public PathResult findPath(Maze maze) {
        long startTime = System.nanoTime();

        Position start = maze.getStart();
        Position finish = maze.getFinish();
        Queue<Position> queue = new ArrayDeque<>();
        Set<Position> visited = new HashSet<>();
        Map<Position, Position> parentMap = new HashMap<>();
        List<AlgorithmStep> steps = new ArrayList<>();

        queue.add(start);
        visited.add(start);

        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        while (!queue.isEmpty()) {
            Position current = queue.poll();

            if (!current.equals(start) && !current.equals(finish)) {
                steps.add(new AlgorithmStep(current, CellType.VISITED));
            }

            if (current.equals(finish)) {
                break;
            }

            for (int[] dir : directions) {
                int nextX = current.x() + dir[0];
                int nextY = current.y() + dir[1];
                Position neighbor = new Position(nextX, nextY);

                if (maze.isPassable(nextX, nextY) && !visited.contains(neighbor)) {
                    visited.add(neighbor);
                    parentMap.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        List<Position> path = reconstructPath(parentMap, start, finish, visited.contains(finish));

        long executionTimeMs = (System.nanoTime() - startTime) / 1_000_000;

        return new PathResult("BFS", path, steps, executionTimeMs, visited.size());
    }

    private List<Position> reconstructPath(Map<Position, Position> parentMap, Position start, Position finish, boolean found) {
        List<Position> path = new ArrayList<>();
        if (!found) {
            return path;
        }

        Position current = finish;
        while (current != null) {
            path.add(current);
            if (current.equals(start)) {
                break;
            }
            current = parentMap.get(current);
        }

        Collections.reverse(path);
        return path;
    }
}