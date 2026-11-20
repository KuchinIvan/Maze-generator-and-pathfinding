package piapo.project.model;

import java.util.List;

public class PathResult {
    private final String algorithmName;
    private final List<Position> path;
    private final List<AlgorithmStep> steps;
    private final long executionTimeMs;
    private final int visitedCount;

    public PathResult(String algorithmName, List<Position> path, List<AlgorithmStep> steps, long executionTimeMs, int visitedCount) {
        this.algorithmName = algorithmName;
        this.path = path;
        this.steps = steps;
        this.executionTimeMs = executionTimeMs;
        this.visitedCount = visitedCount;
    }

    public boolean isFound() {
        return path != null && !path.isEmpty();
    }

    public int getPathLength() {
        return isFound() ? path.size() : 0;
    }

    // Геттеры
    public String getAlgorithmName() {
        return algorithmName;
    }

    public List<Position> getPath() {
        return path;
    }

    public List<AlgorithmStep> getSteps() {
        return steps;
    }

    public long getExecutionTimeMs() {
        return executionTimeMs;
    }

    public int getVisitedCount() {
        return visitedCount;
    }
}
