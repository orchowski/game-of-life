package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Field implements Runnable {
    private final int col;
    private final int row;
    private boolean alive = new Random().nextBoolean();
    private boolean nextState;

    private List<Field> neighbours = new ArrayList<>();

    public Field(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.nextState = alive;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public void switchToNewState() {
        alive = nextState;
    }

    public void setNeighboursFromTheList(List<Field> fullFieldList, int gridSize) {
        int minCol = col > 0 ? col - 1 : 0;
        int maxCol = col + 1 == gridSize ? gridSize : col + 1;

        int minRow = row > 0 ? row - 1 : 0;
        int maxRow = row + 1 == gridSize ? gridSize : row + 1;

        var neighbours = fullFieldList.stream().filter(
                field -> field.getRow() >= minRow && field.getRow() <= maxRow
                        && field.getCol() >= minCol && field.getCol() <= maxCol
        ).toList();
        this.neighbours.addAll(neighbours);
        this.neighbours.remove(this);
    }

    public void nextPhase() {
        long aliveNeighbours = neighbours.stream().filter(Field::isAlive).count();
        if (isAlive() && aliveNeighbours < 2) {
            setAlive(false);
        }

        if (isAlive() && aliveNeighbours > 3) {
            setAlive(false);
        }

        if (!isAlive() && aliveNeighbours == 3) {
            setAlive(true);
        }
        printFibonacci(100);
        System.out.println(x);
        x = 0;
    }

    int n1 = 0, n2 = 1, n3 = 0;
    long x = 0;

    void printFibonacci(int count) {
        if (count > 0) {
            n3 = n1 + n2;
            n1 = n2;
            n2 = n3;
            x += n3;
            printFibonacci(count - 1);
        }
    }

    @Override
    public void run() {
        nextPhase();
    }
}
