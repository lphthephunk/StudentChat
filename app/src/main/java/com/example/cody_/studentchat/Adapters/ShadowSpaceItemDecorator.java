package com.example.cody_.studentchat.Adapters;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Cody_ on 10/11/2017.
 */

public class ShadowSpaceItemDecorator extends RecyclerView.ItemDecoration {

    private Drawable divider;

    public ShadowSpaceItemDecorator(Context context, int resId){

        this.divider = ContextCompat.getDrawable(context, resId);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state){

        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        for (int index = 0; index < parent.getChildCount(); index++){

            View item = parent.getChildAt(index);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) item.getLayoutParams();
            int top = item.getBaseline() + params.bottomMargin;
            int bottom = top + divider.getIntrinsicHeight();

            this.divider.setBounds(left, top, right, bottom);

            this.divider.draw((c));
        }
    }
}
