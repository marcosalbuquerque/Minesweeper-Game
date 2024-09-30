package com.albuquerque.minesweeper.model;

import com.albuquerque.minesweeper.exception.ExplosionException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FieldTest {

    Field centerfield = new Field(1, 1);

    @Test
    void isNeighbor() {
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                if (x == 1 && y == 1) continue;
                assertTrue(centerfield.addNeighbor(new Field(x, y)));
            }
        }
    }

    @Test
    void isNotNeighbor() {
        for (int x = 2; x < 10; x++) {
            for (int y = 2; y < 10; y++) {
                if (x == 2 && y == 2) continue;
                assertFalse(centerfield.addNeighbor(new Field(x, y)));
            }
        }
    }

    @Test
    void isMarked() {
        assertFalse(centerfield.isMarked());
        centerfield.toggleMark();
        assertTrue(centerfield.isMarked());
    }

    @Test
    void OpenNotMarkedAndNotHasMine() {
        assertTrue(centerfield.open());
    }

    @Test
    void OpenMarkedAndNotHasMine() {
        centerfield.toggleMark();
        assertFalse(centerfield.open());
    }

    @Test
    void OpenMarkedAndHasMine() {
        centerfield.toggleMark();
        centerfield.placeMine();
        assertFalse(centerfield.open());
    }

    @Test
    void OpenNotMarkedAndHasMine() {
        centerfield.placeMine();
        assertThrows(ExplosionException.class, () -> {
            centerfield.open();
        });
    }

    @Test
    void OpenWithNeighbors() {
        Field field22 = new Field(2, 2);
        Field field33 = new Field(3, 3);

        centerfield.addNeighbor(field22);
        field22.addNeighbor(field33);

        centerfield.open();

        assertTrue(field22.isOpen() && field33.isOpen());
    }

    @Test
    void OpenWithNeighbors2() {
        Field field22 = new Field(2, 2);
        Field field12 = new Field(3, 3);

        centerfield.addNeighbor(field12);
        centerfield.addNeighbor(field22);
        field12.placeMine();
        centerfield.open();

        assertFalse(field12.isOpen());
    }
}