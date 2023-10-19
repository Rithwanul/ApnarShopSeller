package com.moktar.zwebview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

/**
 * WebView progress bar, original author: cenxiaozhong, modified and optimized on this basis:
 * 1. The progress bar appears twice when progress returns to 100 at the same time
 * 2. When a progress is not finished and another link is clicked to start the second progress, the second progress does not appear
 * 3. Modify the duration of the disappearing animation so that when it disappears, you can see that the progress can be completed
 * 4. [2021.02.22] Fix the problem that the progress bar is not displayed when the progress returns to 0 or exceeds 10 for the first time
 * 5. Can display gradient colors
 * 6. The progress bar transparency problem starts again when the progress is 95-100
 *
 * @author moktar
 * Link to https://github.com/youlookwhat/WebProgress
 */
public class WebProgress extends FrameLayout {

    /**
     * The maximum duration of the default uniform animation
     */
    public static final int MAX_UNIFORM_SPEED_DURATION = 8 * 1000;
    /**
     * Maximum duration of deceleration animation after default acceleration
     */
    public static final int MAX_DECELERATE_SPEED_DURATION = 450;
    /**
     * When 95f-100f, the transparency is 1f-0f duration
     */
    public static final int DO_END_ALPHA_DURATION = 630;
    /**
     * 95f-100f animation duration
     */
    public static final int DO_END_PROGRESS_DURATION = 500;
    public static final int UN_START = 0;
    public static final int STARTED = 1;
    public static final int FINISH = 2;
    /**
     * Default height (dp)
     */
    public static int WEB_PROGRESS_DEFAULT_HEIGHT = 3;
    /**
     * Default color of progress bar
     */
    public static String WEB_PROGRESS_COLOR = "#32B848";
    /**
     * Maximum duration of current uniform animation
     */
    private static int CURRENT_MAX_UNIFORM_SPEED_DURATION = MAX_UNIFORM_SPEED_DURATION;
    /**
     * Maximum duration of deceleration animation after current acceleration
     */
    private static int CURRENT_MAX_DECELERATE_SPEED_DURATION = MAX_DECELERATE_SPEED_DURATION;
    /**
     * Progress bar color
     */
    private int mColor;
    /**
     * Progress bar brush
     */
    private Paint mPaint;
    /**
     * Progress bar animation
     */
    private Animator mAnimator;
    /**
     * The width of the control
     */
    private int mTargetWidth = 0;
    /**
     * Height of the control
     */
    private int mTargetHeight;
    /**
     * Mark the status of the current progress bar
     */
    private int TAG = 0;
    /**
     * It’s the first time to come to the progress show, followed by setProgress
     */
    private boolean isShow = false;
    private float mCurrentProgress = 0F;
    private final ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener = animation -> {
                float t = (float) animation.getAnimatedValue();
                WebProgress.this.mCurrentProgress = t;
                WebProgress.this.invalidate();
            };
    private final AnimatorListenerAdapter mAnimatorListenerAdapter = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            doEnd();
        }
    };

    public WebProgress(Context context) {
        this(context, null);
    }

    public WebProgress(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WebProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mPaint = new Paint();
        mColor = Color.parseColor(WEB_PROGRESS_COLOR);
        mPaint.setAntiAlias(true);
        mPaint.setColor(mColor);
        mPaint.setDither(true);
        mPaint.setStrokeCap(Paint.Cap.SQUARE);

        mTargetWidth = context.getResources().getDisplayMetrics().widthPixels;
        mTargetHeight = dip2px(WEB_PROGRESS_DEFAULT_HEIGHT);
    }

    /**
     * Set monochrome progress bar
     */
    public void setColor(int color) {
        this.mColor = color;
        mPaint.setColor(color);
    }

    public void setColor(String color) {
        this.setColor(Color.parseColor(color));
    }

    public void setColor(int startColor, int endColor) {
        LinearGradient linearGradient = new LinearGradient(0, 0, mTargetWidth, mTargetHeight, startColor, endColor, Shader.TileMode.CLAMP);
        mPaint.setShader(linearGradient);
    }

    /**
     * Set gradient color progress bar
     *
     * @param startColor start color
     * @param endColor end color
     */
    public void setColor(String startColor, String endColor) {
        this.setColor(Color.parseColor(startColor), Color.parseColor(endColor));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int w = MeasureSpec.getSize(widthMeasureSpec);

        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);

        if (wMode == MeasureSpec.AT_MOST) {
            w = Math.min(w, getContext().getResources().getDisplayMetrics().widthPixels);
        }
        if (hMode == MeasureSpec.AT_MOST) {
            h = mTargetHeight;
        }
        this.setMeasuredDimension(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.drawRect(0, 0, mCurrentProgress / 100 * (float) this.getWidth(), this.getHeight(), mPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mTargetWidth = getMeasuredWidth();
        int screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        if (mTargetWidth >= screenWidth) {
            CURRENT_MAX_DECELERATE_SPEED_DURATION = MAX_DECELERATE_SPEED_DURATION;
            CURRENT_MAX_UNIFORM_SPEED_DURATION = MAX_UNIFORM_SPEED_DURATION;
        } else {
            //Take the ratio
            float rate = this.mTargetWidth / (float) screenWidth;
            CURRENT_MAX_UNIFORM_SPEED_DURATION = (int) (MAX_UNIFORM_SPEED_DURATION * rate);
            CURRENT_MAX_DECELERATE_SPEED_DURATION = (int) (MAX_DECELERATE_SPEED_DURATION * rate);
        }
    }

    private void setFinish() {
        isShow = false;
        TAG = FINISH;
    }

    private void startAnim(boolean isFinished) {

        float v = isFinished ? 100 : 95;

        if (mAnimator != null && mAnimator.isStarted()) {
            mAnimator.cancel();
        }
        mCurrentProgress = mCurrentProgress == 0 ? 0.00000001f : mCurrentProgress;
        // Sudden problems may arise due to transparency
        setAlpha(1);

        if (!isFinished) {
            ValueAnimator mAnimator = ValueAnimator.ofFloat(mCurrentProgress, v);
            float residue = 1f - mCurrentProgress / 100 - 0.05f;
            mAnimator.setInterpolator(new LinearInterpolator());
            mAnimator.setDuration((long) (residue * CURRENT_MAX_UNIFORM_SPEED_DURATION));
            mAnimator.addUpdateListener(mAnimatorUpdateListener);
            mAnimator.start();
            this.mAnimator = mAnimator;
        } else {

            ValueAnimator segment95Animator = null;
            if (mCurrentProgress < 95) {
                segment95Animator = ValueAnimator.ofFloat(mCurrentProgress, 95);
                float residue = 1f - mCurrentProgress / 100f - 0.05f;
                segment95Animator.setInterpolator(new LinearInterpolator());
                segment95Animator.setDuration((long) (residue * CURRENT_MAX_DECELERATE_SPEED_DURATION));
                segment95Animator.setInterpolator(new DecelerateInterpolator());
                segment95Animator.addUpdateListener(mAnimatorUpdateListener);
            }

            ObjectAnimator mObjectAnimator = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f);
            mObjectAnimator.setDuration(DO_END_ALPHA_DURATION);
            ValueAnimator mValueAnimatorEnd = ValueAnimator.ofFloat(95f, 100f);
            mValueAnimatorEnd.setDuration(DO_END_PROGRESS_DURATION);
            mValueAnimatorEnd.addUpdateListener(mAnimatorUpdateListener);

            AnimatorSet mAnimatorSet = new AnimatorSet();
            mAnimatorSet.playTogether(mObjectAnimator, mValueAnimatorEnd);

            if (segment95Animator != null) {
                AnimatorSet mAnimatorSet1 = new AnimatorSet();
                mAnimatorSet1.play(mAnimatorSet).after(segment95Animator);
                mAnimatorSet = mAnimatorSet1;
            }
            mAnimatorSet.addListener(mAnimatorListenerAdapter);
            mAnimatorSet.start();
            mAnimator = mAnimatorSet;
        }

        TAG = STARTED;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        /**
         * animator cause leak , if not cancel;
         */
        if (mAnimator != null && mAnimator.isStarted()) {
            mAnimator.cancel();
            mAnimator = null;
        }
    }

    private void doEnd() {
        if (TAG == FINISH && mCurrentProgress == 100) {
            setVisibility(GONE);
            mCurrentProgress = 0f;
            this.setAlpha(1f);
        }
        TAG = UN_START;
    }

    public void reset() {
        mCurrentProgress = 0;
        if (mAnimator != null && mAnimator.isStarted()) {
            mAnimator.cancel();
        }
    }

    public void setProgress(int newProgress) {
        setProgress(Float.valueOf(newProgress));
    }


    public LayoutParams offerLayoutParams() {
        return new LayoutParams(mTargetWidth, mTargetHeight);
    }

    private int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public WebProgress setHeight(int heightDp) {
        this.mTargetHeight = dip2px(heightDp);
        return this;
    }

    public void setProgress(float progress) {
        // fix returns two 100s at the same time, resulting in two problems with the progress bar;
        if (TAG == UN_START && progress == 100) {
            setVisibility(View.GONE);
            return;
        }

        if (getVisibility() == View.GONE) {
            setVisibility(View.VISIBLE);
        }
        if (progress < 95) {
            return;
        }
        if (TAG != FINISH) {
            startAnim(true);
        }
    }

    /**
     * Show progress bar
     */
    public void show() {
        isShow = true;
        setVisibility(View.VISIBLE);
        mCurrentProgress = 0f;
        startAnim(false);
    }

    /**
     * Disappear after the progress is completed
     */
    public void hide() {
        setWebProgress(100);
    }

    /**
     * To process the WebView progress bar separately
     */
    public void setWebProgress(int newProgress) {
        if (newProgress >= 0 && newProgress < 95) {
            if (!isShow) {
                show();
            } else {
                setProgress(newProgress);
            }
        } else {
            setProgress(newProgress);
            setFinish();
        }
    }
}

