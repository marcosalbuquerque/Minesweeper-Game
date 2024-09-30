package com.albuquerque.minesweeper.model;


import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class Field {


    private boolean mine;
    private boolean open;
    private boolean flagged;

    private final List<Field> neighbors = new ArrayList<>();
    private final List<FieldObserver> observers = new ArrayList<>();

    private final int x, y;

    Field(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void registerObserver(FieldObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers(FieldEvent event) {
        observers.forEach(obs -> obs.eventOccurred(this, event));
    }

    boolean addNeighbor(Field neighbor) {
        int dx = Math.abs(this.x - neighbor.x);
        int dy = Math.abs(this.y - neighbor.y);
        boolean checkdistance = (dx == 1 && dy == 0) || (dx == 0 && dy == 1) || (dx == 1 && dy == 1);

        if (checkdistance)
            neighbors.add(neighbor);
        return checkdistance;


    }

    public void toggleFlag() {
        if (!open)
            flagged = !flagged;

        if(isFlagged()) notifyObservers(FieldEvent.FLAG);
        if(!isFlagged()) notifyObservers(FieldEvent.UNFLAG);
    }

    public boolean open() {
        if (!isOpen() && !isFlagged()) {
            if (hasMine()) {
                notifyObservers(FieldEvent.EXPLODE);
                return true;
            }

            setOpen(true);

            if (safeNeighbor())
                neighbors.forEach(Field::open);

            return true;
        }
        return false;
    }

    void placeMine() {
        mine = true;
    }

    public boolean safeNeighbor() {
        return neighbors.stream().noneMatch(n -> n.mine);
    }

    boolean isFlagged() {
        return flagged;
    }
    boolean isOpen() {
        return open;
    }

    void setOpen(boolean open) {
        this.open = open;

        if (open) notifyObservers(FieldEvent.OPEN);
    }

    public boolean hasMine() {
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
        boolean correctmark = !isOpen() && isFlagged() && hasMine();

        return correctopen || correctmark;
    }

    public long amountMineNeighbor() {
        return neighbors.stream().filter(Field::hasMine).count();
    }

    void restart() {
        mine = false;
        flagged = false;
        open = false;
        notifyObservers(FieldEvent.RESTART);
    }

}
