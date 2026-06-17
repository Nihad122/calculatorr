package com.example.mazegame;

public class MazeData {

    // Bitmask constants
    public static final int LEFT  = 1;  // bit 0 (2^0)
    public static final int RIGHT = 2;  // bit 1 (2^1)
    public static final int UP    = 4;  // bit 2 (2^2)
    public static final int DOWN  = 8;  // bit 3 (2^3)
    public static final int START_MARKER = 16; // bit 4 (2^4)

    public static final int[][] MAZE = {
            {26,  8, 10,  9},
            {28,  1,  0, 12},
            {12, 10,  9, 13},
            { 6,  5,  6,  5}
    };

    public static final int ROWS = MAZE.length;
    public static final int COLS = MAZE[0].length;

    public static int[] findStart() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if ((MAZE[r][c] & START_MARKER) != 0) {
                    return new int[]{r, c};
                }
            }
        }
        // Fallback: top-left
        return new int[]{0, 0};
    }

    public static int getDoors(int row, int col) {
        return MAZE[row][col] & 0b1111; // only bits 0-3 (LEFT/RIGHT/UP/DOWN)
    }

    public static boolean isExit(int row, int col) {
        return (MAZE[row][col] & 0b1111) == 0 && (MAZE[row][col] & START_MARKER) == 0;
    }

    public static boolean canMove(int row, int col, int direction) {
        int doors = getDoors(row, col);
        if ((doors & direction) == 0) return false; // bitmask doesn't allow it

        // Boundary check
        int newRow = row, newCol = col;
        switch (direction) {
            case LEFT:  newCol--; break;
            case RIGHT: newCol++; break;
            case UP:    newRow--; break;
            case DOWN:  newRow++; break;
        }
        return newRow >= 0 && newRow < ROWS && newCol >= 0 && newCol < COLS;
    }

    public static int[] move(int row, int col, int direction) {
        int newRow = row, newCol = col;
        switch (direction) {
            case LEFT:  newCol--; break;
            case RIGHT: newCol++; break;
            case UP:    newRow--; break;
            case DOWN:  newRow++; break;
        }
        return new int[]{newRow, newCol};
    }
}