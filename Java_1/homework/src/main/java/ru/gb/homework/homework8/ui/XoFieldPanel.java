package ru.gb.homework.homework8.ui;

import javax.swing.*;
import java.awt.*;

public class XoFieldPanel extends JPanel {

    public static final int GAME_MODE_HVAI = 0;
    public static final int GAME_MODE_HVH = 1;
    private int fieldSize;

    public XoFieldPanel() {
        setBackground(Color.LIGHT_GRAY);
    }

    public void startGame(int gameMode, int fieldSize, int winLength) {
        System.out.printf(" Game mode: %d%n Field size: %d%n Win length: %d%n", gameMode, fieldSize, winLength);

        this.fieldSize = fieldSize;
        isRefreshGameMap = true;
        repaint();
    }

    /**
     * Флаг указывает на необходимость перерисовки поля.
     */
    private boolean isRefreshGameMap = false;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(isRefreshGameMap) {
            int cellSizeY = (this.getHeight()) / fieldSize;
            int cellSizeX = (this.getWidth()) / fieldSize;
            int startX = 0;
            int startY = 0;

            int x1, x2, y1, y2;
            for (int i = 0; i <= fieldSize; i++) {

                x1 = startX;
                y1 = startY + i * cellSizeY;
                x2 = startX + fieldSize * cellSizeX;
                y2 = startY + i * cellSizeY;

                // горизонтальная линия
                g.drawLine(x1, y1, x2, y2);

                x1 = startX + i * cellSizeX;
                y1 = startY;
                x2 = startX + i * cellSizeX;
                y2 = startY + fieldSize * cellSizeY;

                // вертикальная линия
                g.drawLine(x1, y1, x2, y2);
            }

            isRefreshGameMap = false;
        }
    }
}
