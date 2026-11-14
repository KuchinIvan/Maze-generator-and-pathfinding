package piapo.project.model;

import java.util.ArrayList;

public class PathResult {
    private final String algorithmName;
    private final ArrayList<Position> path;
    private final ArrayList<AlgorithmStep> steps;
    private final long execuitonTime;
    private final int visitedCount;

    public PathResult(String algorithmName, ArrayList<Position> path, ArrayList<AlgorithmStep> steps,  long execuitonTime, int visitedCount) {
        this.algorithmName = algorithmName;
        this.path = path;
        this.steps = steps;
        this.execuitonTime = execuitonTime;
        this.visitedCount = visitedCount;
    }
}
