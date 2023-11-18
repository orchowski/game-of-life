package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Field {
    private final int col;
    private final int row;
    private boolean alive = new Random().nextBoolean();
    private boolean nextState;

    private List<Field> neighbours = new ArrayList<>();

    public Field(int row, int col){
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

    public void switchToNewState(){
        alive = nextState;
    }

    public void setNeighboursFromTheList(List<Field> fullFieldList, int gridSize){
        int minCol = col > 0 ? col - 1 : 0;
        int maxCol = col + 1 == gridSize ? gridSize : col + 1;

        int minRow = row > 0 ? row - 1 : 0;
        int maxRow = row + 1 == gridSize ? gridSize : row + 1;

        var neighbours = fullFieldList.stream().filter(
                field -> field.getRow() >= minRow && field.getRow() <= maxRow
                &&  field.getCol() >= minCol && field.getCol() <= maxCol
        ).toList();
        this.neighbours.addAll(neighbours);
        this.neighbours.remove(this);
    }

    public List<Field> getNeighbours() {
        return neighbours;
    }
}
