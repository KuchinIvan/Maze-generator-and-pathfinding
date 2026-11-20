package piapo.project.solver;

import piapo.project.model.AlgorithmStep;
import piapo.project.model.CellType;
import piapo.project.model.Maze;
import piapo.project.model.PathResult;
import piapo.project.model.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class AStarPathFinder implements PathFinder {

    private record Node(Position position, int fScore) {
    }

    @Override
    public PathResult findPath(Maze maze) {
        long startTime = System.nanoTime();

        Position start = maze.getStart();
        Position finish = maze.getFinish();
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(Node::fScore));
        Set<Position> visited = new HashSet<>();
        Map<Position, Position> parentMap = new HashMap<>();
        Map<Position, Integer> gScore = new HashMap<>();
        List<AlgorithmStep> steps = new ArrayList<>();

        gScore.put(start, 0);
        openSet.add(new Node(start, heuristic(start, finish)));

        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        while (!openSet.isEmpty()) {
            Position current = openSet.poll().position();

            if (visited.contains(current)) {
                continue;
            }
            visited.add(current);

            if (!current.equals(start) && !current.equals(finish)) {
                steps.add(new AlgorithmStep(current, CellType.VISITED));
            }

            if (current.equals(finish)) {
                break;
            }

            int currentG = gScore.get(current);

            for (int[] dir : directions) {
                int nextX = current.x() + dir[0];
                int nextY = current.y() + dir[1];
                Position neighbor = new Position(nextX, nextY);

                if (!maze.isPassable(nextX, nextY) || visited.contains(neighbor)) {
                    continue;
                }

                int tentativeG = currentG + 1;

                if (tentativeG < gScore.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    parentMap.put(neighbor, current);
                    gScore.put(neighbor, tentativeG);
                    int fScore = tentativeG + heuristic(neighbor, finish);
                    openSet.add(new Node(neighbor, fScore));
                }
            }
        }

        List<Position> path = reconstructPath(parentMap, start, finish, visited.contains(finish));
        long executionTimeMs = (System.nanoTime() - startTime) / 1_000_000;

        return new PathResult("A*", path, steps, executionTimeMs, visited.size());
    }

    // Манхэттенское расстояние
    private int heuristic(Position a, Position b) {
        return Math.abs(a.x() - b.x()) + Math.abs(a.y() - b.y());
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