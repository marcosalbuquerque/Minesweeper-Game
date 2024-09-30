package com.albuquerque.minesweeper.view;

import com.albuquerque.minesweeper.model.Board;

import javax.swing.*;
import java.awt.*;

public class BoardPane extends JPanel {

    public BoardPane(Board board) {
        setLayout(new GridLayout(board.getRows(), board.getColumns()));
        board.forEachFields(f -> add(new FieldButton(f)));

        board.registerConsumer(e -> {
            SwingUtilities.invokeLater(() -> {
                if (e.booleanValue() == true) {
                    JOptionPane.showMessageDialog(null, "Você ganhou!");
                } else {
                    JOptionPane.showMessageDialog(null, "Você perdeu :(");
                }

                board.restart();
            });
        });
    }
}
