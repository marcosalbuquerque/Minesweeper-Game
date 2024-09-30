package com.albuquerque.minesweeper.view;

import com.albuquerque.minesweeper.model.Field;
import com.albuquerque.minesweeper.model.FieldEvent;
import com.albuquerque.minesweeper.model.FieldObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class FieldButton extends JButton implements FieldObserver, MouseListener {

    private Field field;

    private final Color BG_DEFAULT = new Color(184, 184, 184);
    private final Color BG_FLAG = new Color(8, 179, 247);
    private final Color BG_EXPLODE = new Color(189, 66, 68);
    private final Color GREEN_TEXT = new Color(0, 100, 0);

    public FieldButton(Field field) {
        this.field = field;
        setBackground(BG_DEFAULT);
        setBorder(BorderFactory.createBevelBorder(0));
        setOpaque(true);
        field.registerObserver(this);
        addMouseListener(this);

    }

    @Override
    public void eventOccurred(Field f, FieldEvent e) {
        switch (e) {
            case OPEN:
                applyOpenStyle();
                break;
            case FLAG:
                applyFlagStyle();
                break;
            case EXPLODE:
                applyExplodeStyle();
                break;
            default:
                applyDefaultStyle();
                break;
        }
    }

    private void applyOpenStyle() {
        setBorder(BorderFactory.createLineBorder(Color.GRAY));

        if(field.hasMine()) {
            setBackground(BG_EXPLODE);
            return;
        }

        switch ((int) field.amountMineNeighbor()) {
            case 1:
                setForeground(GREEN_TEXT);
                break;
            case 2:
                setForeground(Color.BLUE);
                break;
            case 3:
                setForeground(Color.YELLOW);
                break;
            case 4:
                setForeground(Color.RED);
                break;
            case 5:
                setForeground(Color.RED);
                break;
            case 6:
                setForeground(Color.RED);
                break;
            default:
                setForeground(Color.PINK);
        }
        String value = !field.safeNeighbor() ? field.amountMineNeighbor() + "" : "";

        setText(value);
    }

    private void applyDefaultStyle() {
        setText("");
        setBackground(BG_DEFAULT);
        setBorder(BorderFactory.createBevelBorder(0));
    }

    private void applyExplodeStyle() {
        setBackground(BG_EXPLODE);
        setForeground(Color.WHITE);
        setText("X");
    }
    private void applyFlagStyle() {
        setText("M");
        setForeground(Color.BLACK);
        setBackground(BG_FLAG);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == 1) {
            field.open();
        } else {
            field.toggleFlag();
        }
    }

    public void mouseClicked(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}
