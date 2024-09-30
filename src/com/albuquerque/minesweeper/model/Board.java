package com.albuquerque.minesweeper.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Board implements FieldObserver {
    private final int rows;
    private final int columns;
    private final int mines;


    private final List<Field> fields = new ArrayList<>();
    private final List<Consumer<Boolean>> consumers = new ArrayList<>();

    public Board(int rows, int columns, int mines) {
        this.rows = rows;
        this.columns = columns;
        this.mines = mines;

        generateFields();
        connectNeighbors();
        randomlyPlaceMines();
    }

    public void forEachFields(Consumer<Field> function) {
        fields.forEach(function);
    }

    public void registerConsumer(Consumer<Boolean> consumer) {
        consumers.add(consumer);
    }

    private void notifyConsumers(Boolean b) {
        consumers.forEach(c -> c.accept(b));
    }

    public void open(int column, int row) {
        fields.parallelStream().filter(f -> f.getX() == column && f.getY() == row).findFirst().ifPresent(Field::open);

    }

    public void toggleFlag(int column, int row) {
        fields.parallelStream().filter(f -> f.getX() == column && f.getY() == row).findFirst()
                .ifPresent(Field::toggleFlag);
    }

    private void generateFields() {
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                Field field = new Field(x, y);
                field.registerObserver(this);
                fields.add(field);
            }
        }
    }

    private void connectNeighbors() {
        for (Field field01 : fields) {
            for (Field field02 : fields) {
                field01.addNeighbor(field02);
            }
        }
    }

    private void randomlyPlaceMines() {
        int placedMines = 0;
        do {
            int randomnum = (int) (Math.random() * fields.size());
            fields.get(randomnum).placeMine();
            placedMines = (int) fields.stream().filter(Field::hasMine).count();
        } while (placedMines < mines);

    }

    public boolean goalAchieved() {
        return fields.stream().allMatch(Field::goalAchieved);
    }

    public void restart() {
        fields.forEach(Field::restart);
        randomlyPlaceMines();
    }

    private void showMines() {
        fields.stream().filter(Field::hasMine).filter(f -> !f.isFlagged()).forEach(f -> f.setOpen(true));
    }

    @Override
    public void eventOccurred(Field f, FieldEvent e) {
        if (e == FieldEvent.EXPLODE) {
            showMines();
            notifyConsumers(false);
        }
        else if (goalAchieved()) {
            notifyConsumers(true);
        }
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }
}
