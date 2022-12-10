package com.planitse2022.planit.util.rdecoration;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PaddingDecoration extends RecyclerView.ItemDecoration {
    public static final int LEFT = 0, RIGHT = 1, TOP = 2, BOTTOM = 3;
    private int padding;
    private int direction;

    public PaddingDecoration(int padding, int direction) {
        this.padding = padding;
        this.direction = direction;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if(direction == LEFT) {
            outRect.left = padding;
        }
        else if(direction == RIGHT) {
            outRect.right = padding;
        }
        else if(direction == TOP) {
            outRect.top = padding;
        }
        else if(direction == BOTTOM) {
            outRect.bottom = padding;
        }
    }
}
