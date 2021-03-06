package com.sd.lib.cloading;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Color;

public abstract class FColorLoading implements IColorLoading
{
    private int[] mColors = new int[]{
            Color.parseColor("#03DAC5"),
            Color.parseColor("#6200EE"),
            Color.parseColor("#F06292")
    };

    private int mColorIndex = 0;
    private long mDuration = 1000;

    private ValueAnimator mAnimator;

    @Override
    public void setColors(int[] colors)
    {
        mColorIndex = 0;
        mColors = colors;
        invalidateView();
    }

    @Override
    public void setDuration(long duration)
    {
        mDuration = duration;
        if (mAnimator != null)
            mAnimator.setDuration(duration);
    }

    @Override
    public void start()
    {
        if (mColors == null || mColors.length <= 0)
            return;

        if (!getAnimator().isStarted())
            getAnimator().start();
    }

    @Override
    public void stop()
    {
        if (mAnimator != null)
        {
            mAnimator.cancel();
            mColorIndex = 0;
        }
    }

    private int getColorCurrent()
    {
        if (mColors == null || mColors.length <= 0)
            return 0;

        return mColors[mColorIndex];
    }

    private boolean nextIndex()
    {
        if (mColors == null || mColors.length <= 0)
            return false;

        final int index = mColorIndex + 1;
        if (index >= mColors.length)
            mColorIndex = 0;
        else
            mColorIndex = index;
        return true;
    }

    private ValueAnimator getAnimator()
    {
        if (mAnimator == null)
        {
            mAnimator = new ValueAnimator();
            mAnimator.setFloatValues(0f, 1.0f);
            mAnimator.setRepeatCount(-1);
            mAnimator.setRepeatMode(ValueAnimator.RESTART);
            mAnimator.setDuration(mDuration);
            mAnimator.addListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationRepeat(Animator animation)
                {
                    super.onAnimationRepeat(animation);
                    nextIndex();
                }

                @Override
                public void onAnimationEnd(Animator animation)
                {
                    super.onAnimationEnd(animation);
                    FColorLoading.this.onStop();
                }
            });
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
            {
                @Override
                public void onAnimationUpdate(ValueAnimator animation)
                {
                    final int color = getColorCurrent();
                    final float value = (float) animation.getAnimatedValue();
                    FColorLoading.this.onAnimationUpdate(color, value);
                }
            });
        }
        return mAnimator;
    }

    protected abstract void invalidateView();

    protected abstract void onAnimationUpdate(int color, float progress);

    protected abstract void onStop();
}
