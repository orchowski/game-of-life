package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class Field implements Runnable {
    private final int col;
    private final int row;
    private final Semaphore semaphore;
    private boolean alive = new Random().nextBoolean();
    private boolean nextState;

    public AtomicBoolean getLock() {
        return lock;
    }

    private AtomicBoolean lock = new AtomicBoolean(true);
    private List<Field> neighbours = new ArrayList<>();

    public Field(int row, int col, Semaphore semaphore) {
        this.row = row;
        this.col = col;
        this.semaphore = semaphore;
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
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1);
                if (lock.get()) {
                    continue;
                }
                semaphore.acquire();
                nextPhase();
                block();
                semaphore.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void unblock() {
        lock.set(false);
    }

    public void block() {
        lock.set(true);
    }
}
