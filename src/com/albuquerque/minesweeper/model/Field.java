package com.albuquerque.minesweeper.model;

import com.albuquerque.minesweeper.exception.ExplosionException;

import java.util.ArrayList;
import java.util.List;

public class Field {


    private boolean mine;
    private boolean open;
    private boolean marked;

    private List<Field> neighbors = new ArrayList<>();
    private final int x, y;

    Field(int x, int y) {
        this.x = x;
        this.y = y;
    }

    boolean addNeighbor(Field neighbor) {
        int dx = Math.abs(this.x - neighbor.x);
        int dy = Math.abs(this.y - neighbor.y);
        boolean checkdistance = (dx == 1 && dy == 0) || (dx == 0 && dy == 1) || (dx == 1 && dy == 1);

        if (checkdistance)
            neighbors.add(neighbor);
        return checkdistance;


    }

    void toggleMark() {
        if (!open)
            marked = !marked;
    }

    boolean open() {
        if (!isOpen() && !isMarked()) {
            open = true;

            if (hasMine()) {
                throw new ExplosionException();
            }

            if (safeNeighbor())
                neighbors.forEach(Field::open);

            return true;
        }
        return false;
    }

    void placeMine() {
        mine = true;
    }
    boolean safeNeighbor() {
        return neighbors.stream().noneMatch(n -> n.mine);
    }

    boolean isMarked() {
        return marked;
    }
    boolean isOpen() {
        return open;
    }

    boolean hasMine() {
        return mine;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    boolean goalAchieved() {
        boolean correctopen = isOpen() && !hasMine();
        boolean correctmark = !isOpen() && isMarked() && hasMine();

        return correctopen || correctmark;
    }

    long amountMineNeighbor() {
        return neighbors.stream().filter(Field::hasMine).count();
    }

    void restart() {
        mine = false;
        marked = false;
        open = false;
    }

    @Override
    public String toString() {
        if (isMarked())
            return "x";
        else if (isOpen() && hasMine())
            return "*";
        else if (isOpen() && amountMineNeighbor() > 0)
            return Long.toString(amountMineNeighbor());
        else if (isOpen())
            return " ";
        else
            return "?";
    }
}
