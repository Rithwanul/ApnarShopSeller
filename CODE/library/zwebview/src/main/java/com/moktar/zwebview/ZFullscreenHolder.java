package com.moktar.zwebview;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class ZFullscreenHolder extends FrameLayout {

    public ZFullscreenHolder(Context context) {
        super(context);
        setBackgroundColor(context.getResources().getColor(android.R.color.black));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }
}
