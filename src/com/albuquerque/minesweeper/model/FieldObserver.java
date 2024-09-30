package com.albuquerque.minesweeper.model;

@FunctionalInterface
public interface FieldObserver {
    public void eventOccurred(Field f, FieldEvent e);
}
