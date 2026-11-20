package piapo.project;

import piapo.project.generator.MazeGenerator;
import piapo.project.generator.DfsMazeGenerator;
import piapo.project.generator.KruskalMazeGenerator;
import piapo.project.solver.PathFinder;
import piapo.project.solver.BfsPathFinder;
import piapo.project.solver.AStarPathFinder;
import piapo.project.model.CellType;
import piapo.project.model.Maze;
import piapo.project.model.PathResult;
import piapo.project.model.Position;

import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        Maze maze = null;
        while (true) {
            try{
                System.out.println("доступный функционал текущей версии приложения:");
                System.out.println("1. генерация лабиринта алгоритмом DFS");
                System.out.println("2. генерация лабиринта алгоритмом Крускала");
                System.out.println("3. поиск пути алгоритмом BFS");
                System.out.println("4. поиск пути алгоритмом А*");
                System.out.println("введите номер желаемого сценария");

                int scenary = sc.nextInt();

                switch (scenary) {
                    case 1:
                        MazeGenerator dfs = new DfsMazeGenerator();
                        maze = createMaze(dfs);
                        printMaze(maze, null);
                        break;
                    case 2:
                        MazeGenerator kruskal = new KruskalMazeGenerator();
                        maze = createMaze(kruskal);
                        printMaze(maze, null);
                    case 3:
                        if (maze == null) {
                            System.out.println("сначала сгенерируйте лабиринт");
                            break;
                        }
                        PathFinder bfs = new BfsPathFinder();
                        PathResult bfsResult = bfs.findPath(maze);
                        printPathInfo(bfsResult);
                        printMaze(maze, bfsResult);
                        break;
                    case 4:
                        if (maze == null) {
                            System.out.println("сначала сгенерируйте лабиринт");
                            break;
                        }
                        PathFinder aStaer = new BfsPathFinder();
                        PathResult aStarResult = aStaer.findPath(maze);
                        printPathInfo(aStarResult);
                        printMaze(maze, aStarResult);
                        break;
                }
            } catch (Exception e) {
                System.out.println("возникла ошибка: "+ e.getMessage());
            }
        }
     }
    private static Maze createMaze(MazeGenerator generator) {
        System.out.println("введите ширину лабиринта от 1 до 256:");
        int width = sc.nextInt();
        System.out.println("введите высоту лабиринта от 1 до 256:");

        int height = sc.nextInt();
        return generator.generateMaze(width, height);
    }

    private static void printPathInfo(PathResult result) {
        System.out.println("алгоритм: " + result.getAlgorithmName());
        System.out.println("путь найден: " + result.isFound());
        System.out.println("длина пути: " + result.getPathLength());
        System.out.println("посещено клеток: " + result.getVisitedCount());
        System.out.println("время выполнения (мс): " + result.getExecutionTimeMs());
    }

    private static void printMaze(Maze maze, PathResult pathResult) {
        System.out.println("размер: " + maze.getWidth() + "x" + maze.getHeight() + " | шагов генерации: " + maze.getGenerationSteps().size());

        for (int y = 0; y < maze.getHeight(); y++) {
            StringBuilder row = new StringBuilder();
            for (int x = 0; x < maze.getWidth(); x++) {
                Position pos = new Position(x, y);
                CellType cell = maze.getCellType(x, y);

                char symbol;
                if (cell == CellType.START) {
                    symbol = 'S';
                } else if (cell == CellType.FINISH) {
                    symbol = 'F';
                } else if (pathResult != null && pathResult.getPath().contains(pos)) {
                    symbol = '*';
                } else {
                    symbol = switch (cell) {
                        case WALL -> 'O';
                        case PASSAGE -> ' ';
                        case VISITED -> '·';
                        case PATH -> '*';
                        default -> ' ';
                    };
                }
                row.append(symbol).append(symbol);
            }
            System.out.println(row);
        }
    }
}