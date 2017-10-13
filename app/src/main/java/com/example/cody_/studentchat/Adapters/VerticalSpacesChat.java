package com.example.cody_.studentchat.Adapters;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Cody_ on 10/11/2017.
 */

public class VerticalSpacesChat extends RecyclerView.ItemDecoration {

    private final int verticalSpaceHeight;

    public VerticalSpacesChat(int verticalSpaceHeight){

        this.verticalSpaceHeight = verticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state){

        if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1){

            outRect.bottom = verticalSpaceHeight;
        }
    }
}
