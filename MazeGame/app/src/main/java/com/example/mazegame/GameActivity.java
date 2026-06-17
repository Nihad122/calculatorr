package com.example.mazegame;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    private int currentRow;
    private int currentCol;

    private Button btnLeft, btnRight, btnUp, btnDown;
    private TextView tvCoords, tvRoomValue;

    private static final int COLOR_AVAILABLE   = Color.parseColor("#4CAF50"); // green
    private static final int COLOR_UNAVAILABLE = Color.parseColor("#9E9E9E"); // gray

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        btnLeft  = findViewById(R.id.btnLeft);
        btnRight = findViewById(R.id.btnRight);
        btnUp    = findViewById(R.id.btnUp);
        btnDown  = findViewById(R.id.btnDown);
        tvCoords    = findViewById(R.id.tvCoords);
        tvRoomValue = findViewById(R.id.tvRoomValue);

        if (savedInstanceState != null) {
            currentRow = savedInstanceState.getInt("row");
            currentCol = savedInstanceState.getInt("col");
        } else {
            boolean newGame = getIntent().getBooleanExtra("NEW_GAME", true);
            if (newGame) {
                int[] start = MazeData.findStart();
                currentRow = start[0];
                currentCol = start[1];
            } else {
                currentRow = getIntent().getIntExtra("row", MazeData.findStart()[0]);
                currentCol = getIntent().getIntExtra("col", MazeData.findStart()[1]);
            }
        }

        btnLeft.setOnClickListener(v  -> tryMove(MazeData.LEFT));
        btnRight.setOnClickListener(v -> tryMove(MazeData.RIGHT));
        btnUp.setOnClickListener(v    -> tryMove(MazeData.UP));
        btnDown.setOnClickListener(v  -> tryMove(MazeData.DOWN));

        renderRoom();
    }

    private void tryMove(int direction) {
        if (MazeData.canMove(currentRow, currentCol, direction)) {
            int[] next = MazeData.move(currentRow, currentCol, direction);
            currentRow = next[0];
            currentCol = next[1];

            if (MazeData.isExit(currentRow, currentCol)) {
                goToResult();
                return;
            }
            renderRoom();
        }
    }

    @SuppressLint("SetTextI18n")
    private void renderRoom() {
        int doors = MazeData.getDoors(currentRow, currentCol);
        int rawValue = MazeData.MAZE[currentRow][currentCol];

        tvCoords.setText("Room: [" + currentRow + ", " + currentCol + "]");
        tvRoomValue.setText("Bitmask: " + rawValue + "  (doors: " + doors + ")");

        setButtonState(btnLeft,  MazeData.canMove(currentRow, currentCol, MazeData.LEFT));
        setButtonState(btnRight, MazeData.canMove(currentRow, currentCol, MazeData.RIGHT));
        setButtonState(btnUp,    MazeData.canMove(currentRow, currentCol, MazeData.UP));
        setButtonState(btnDown,  MazeData.canMove(currentRow, currentCol, MazeData.DOWN));
    }

    private void setButtonState(Button btn, boolean available) {
        btn.setEnabled(available);
        btn.setBackgroundColor(available ? COLOR_AVAILABLE : COLOR_UNAVAILABLE);
    }

    private void goToResult() {
        Intent intent = new Intent(this, ResultActivity.class);
        // Pass start position so Resume works
        int[] start = MazeData.findStart();
        intent.putExtra("startRow", start[0]);
        intent.putExtra("startCol", start[1]);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("row", currentRow);
        outState.putInt("col", currentCol);
    }
}