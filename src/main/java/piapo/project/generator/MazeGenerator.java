package piapo.project.generator;

import piapo.project.model.Maze;

public interface MazeGenerator {
    Maze generateMaze(int width, int height);
}
