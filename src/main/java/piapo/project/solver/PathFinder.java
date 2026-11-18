package piapo.project.solver;

import piapo.project.model.Maze;
import piapo.project.model.PathResult;
import piapo.project.model.Position;

public interface PathFinder {
    PathResult findPath(Maze maze, Position start, Position finish);
}