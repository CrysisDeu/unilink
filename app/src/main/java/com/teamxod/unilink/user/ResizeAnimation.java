package com.teamxod.unilink.user;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

class ResizeAnimation extends Animation {
    private final int targetHeight;
    private View view;
    private int startHeight;

    public ResizeAnimation(View view, int targetHeight, int startHeight) {
        this.view = view;
        this.targetHeight = targetHeight;
        this.startHeight = startHeight;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        //int newHeight = (int) (startHeight + targetHeight * interpolatedTime);
        //to support decent animation, change new heigt as Nico S. recommended in comments
        int newHeight = (int) (startHeight+(targetHeight - startHeight) * interpolatedTime);
        view.getLayoutParams().height = newHeight;
        view.requestLayout();
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}
