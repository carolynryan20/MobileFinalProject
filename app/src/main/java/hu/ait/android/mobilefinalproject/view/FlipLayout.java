package hu.ait.android.mobilefinalproject.view;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;
import android.widget.FrameLayout;

import hu.ait.android.mobilefinalproject.R;

public class FlipLayout extends FrameLayout
        implements Animation.AnimationListener, View.OnClickListener {

    public static final int ANIM_DURATION_MILLIS = 500;
    private static final Interpolator fDefaultInterpolator = new DecelerateInterpolator();
    private OnFlipListener listener;
    private FlipAnimator animator;
    private boolean isFlipped;
    private View frontView, backView;
    private Direction direction;

    public FlipLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public FlipLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FlipLayout(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        animator = new FlipAnimator();
        animator.setAnimationListener(this);
        animator.setInterpolator(fDefaultInterpolator);
        animator.setDuration(ANIM_DURATION_MILLIS);
        direction = Direction.DOWN;
        setSoundEffectsEnabled(true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (getChildCount() > 2) {
            throw new IllegalStateException(getContext().getString(R.string.flipOnlyHostsTwoChildren));
        }

        frontView = getChildAt(0);
        frontView.setOnClickListener(this);
        backView = getChildAt(1);
        backView.setOnClickListener(this);
        reset();
    }

    private void toggleView() {
        if (frontView == null || backView == null) {
            return;
        }

        if (isFlipped) {
            frontView.setVisibility(View.VISIBLE);
            backView.setVisibility(View.GONE);
        } else {
            frontView.setVisibility(View.GONE);
            backView.setVisibility(View.VISIBLE);
        }

        isFlipped = !isFlipped;
    }

    public void reset() {
        isFlipped = false;
        direction = Direction.DOWN;
        frontView.setVisibility(View.VISIBLE);
        backView.setVisibility(View.GONE);
    }

    public void toggleDown() {
        direction = Direction.DOWN;
        startAnimation();
    }

    public void startAnimation() {
        animator.setVisibilitySwapped();
        startAnimation(animator);
    }

    @Override
    public void onAnimationStart(Animation animation) {
        if (listener != null) {
            listener.onFlipStart(this);
        }
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (listener != null) {
            listener.onFlipEnd(this);
        }
        direction = direction == Direction.UP ? Direction.DOWN : Direction.UP;
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }

    @Override
    public void onClick(View view) {
        toggleDown();
    }

    private enum Direction {
        UP, DOWN
    }

    public interface OnFlipListener {
        void onFlipStart(FlipLayout view);
        void onFlipEnd(FlipLayout view);
    }

    public class FlipAnimator extends Animation {

        private static final float EXPERIMENTAL_VALUE = 50.f;
        private Camera camera;
        private float centerX;
        private float centerY;
        private boolean visibilitySwapped;

        public FlipAnimator() {
            setFillAfter(true);
        }

        public void setVisibilitySwapped() {
            visibilitySwapped = false;
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            camera = new Camera();
            this.centerX = width / 2;
            this.centerY = height / 2;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            final double radians = Math.PI * interpolatedTime;
            float degrees = (float) (180.0 * radians / Math.PI);
            if (direction == Direction.UP) {
                degrees = -degrees;
            }
            if (interpolatedTime >= 0.5f) {
                degrees = getDegrees(degrees);
            }

            final Matrix matrix = t.getMatrix();

            setCamera(radians, degrees, matrix);

            matrix.preTranslate(-centerX, -centerY);
            matrix.postTranslate(centerX, centerY);
        }

        private void setCamera(double radians, float degrees, Matrix matrix) {
            camera.save();
            camera.translate(0.0f, 0.0f, (float) (EXPERIMENTAL_VALUE * Math.sin(radians)));
            camera.rotateX(degrees);
            camera.rotateY(0);
            camera.rotateZ(0);
            camera.getMatrix(matrix);
            camera.restore();
        }

        private float getDegrees(float degrees) {
            if (direction == Direction.UP) {
                degrees += 180.f;
            }

            if (direction == Direction.DOWN) {
                degrees -= 180.f;
            }

            if (!visibilitySwapped) {
                toggleView();
                visibilitySwapped = true;
            }
            return degrees;
        }
    }
}