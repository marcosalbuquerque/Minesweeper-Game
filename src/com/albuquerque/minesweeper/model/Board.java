package com.albuquerque.minesweeper.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {
    private int lines;
    private int columns;
    private int mines;


    private List<Field> fields = new ArrayList<>();

    public Board(int lines, int columns, int mines) {
        this.lines = lines;
        this.columns = columns;
        this.mines = mines;

        generateFields();
        connectNeighbors();
        randomlyPlaceMines();
    }

    private void generateFields() {
        for (int y = 0; y < lines; y++) {
            for (int x = 0; x < columns; x++) {
                fields.add(new Field(x, y));
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
            placedMines = (int) fields.stream().filter(Field::hasMine).count();
            int randomnum = (int) (Math.random() * fields.size());
            fields.get(randomnum).placeMine();
        } while (placedMines < mines);

    }
    boolean goalAchieved() {

        return fields.stream().allMatch(Field::goalAchieved);
    }
}
