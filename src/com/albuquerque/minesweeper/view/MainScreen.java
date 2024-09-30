package com.albuquerque.minesweeper.view;

import com.albuquerque.minesweeper.model.Board;

import javax.swing.*;

public class MainScreen extends JFrame {

    public MainScreen() {
        Board board = new Board(16, 30, 50);

        add(new BoardPane(board));

        setTitle("Campo Minado");
        setSize(690, 438);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new MainScreen();
    }
}
