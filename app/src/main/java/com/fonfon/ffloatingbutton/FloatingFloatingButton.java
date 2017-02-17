package com.fonfon.ffloatingbutton;

import android.content.Context;
import android.graphics.Rect;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class FloatingFloatingButton extends FloatingActionButton {

    private static final int MOVE_THRESHOLD = 0;
    private View aboveView;
    private onAboveViewListener listener;

    public void setListener(onAboveViewListener listener) {
        this.listener = listener;
    }

    public FloatingFloatingButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.setOnTouchListener(new View.OnTouchListener() {
            private float dx;
            private float dy;
            private int moveToY;
            private int moveToX;
            private int distY;
            private int distX;
            private Rect inScreenCoordinates;
            private boolean handleTouched = false;

            private Rect keepInScreen(int topMargin, int leftMargin) {
                int top = topMargin;
                int left = leftMargin;
                int height = getHeight();
                int width = getWidth();

                int rightCorrection = ((View) getParent()).getPaddingRight() + ((View) getParent()).getPaddingLeft();
                int botomCorrection = ((View) getParent()).getPaddingBottom() + ((View) getParent()).getPaddingTop();

                Rect rootBounds = new Rect();
                ((View) getParent()).getHitRect(rootBounds);
                rootBounds.set(rootBounds.left, rootBounds.top, rootBounds.right - rightCorrection, rootBounds.bottom - botomCorrection);

                if (top <= rootBounds.top)
                    top = rootBounds.top;
                else if (top + height > rootBounds.bottom)
                    top = rootBounds.bottom - height;

                if (left <= rootBounds.left)
                    left = rootBounds.left;
                else if (left + width > rootBounds.right)
                    left = rootBounds.right - width;

                return new Rect(left, top, left + width, top + height);
            }

            private View getAboveView(ViewGroup root) {
                for (int i = 0; i < root.getChildCount(); i++) {
                    View view = root.getChildAt(i);

                    if (!(view instanceof FloatingFloatingButton)) {
                        View child = view;
                        if (view instanceof ViewGroup) {
                            child = getAboveView((ViewGroup) view);
                        }

                        Rect rect = new Rect();
                        child.getHitRect(rect);

                        if (rect.contains((int) getX(), (int) getY())) {

                            return child;
                        }
                    }
                }
                return root;
            }

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                // Use ViewGroup.MarginLayoutParams so as to work inside any layout
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                boolean performClick = false;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        if (handleTouched) {
                            moveToY = (int) (event.getRawY() - dy);
                            moveToX = (int) (event.getRawX() - dx);
                            distY = moveToY - params.topMargin;
                            distX = moveToX - params.leftMargin;

                            if (Math.abs(distY) > MOVE_THRESHOLD || Math.abs(distX) > MOVE_THRESHOLD) {
                                moveToY = moveToY - Integer.signum(distY) * Math.min(MOVE_THRESHOLD, Math.abs(distY));
                                moveToX = moveToX - Integer.signum(distX) * Math.min(MOVE_THRESHOLD, Math.abs(distX));

                                inScreenCoordinates = keepInScreen(moveToY, moveToX);
                                params.topMargin = inScreenCoordinates.top;
                                params.leftMargin = inScreenCoordinates.left;
                                view.setLayoutParams(params);
                            }

                            if (listener != null)
                                listener.onAboveView(getAboveView(((ViewGroup) getRootView())));

                            performClick = false;
                        } else {
                            performClick = true;
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        performClick = !handleTouched;
                        break;

                    case MotionEvent.ACTION_DOWN:
                        handleTouched = event.getY() <= getPaddingTop(); // Allow move only wher touch on top padding
                        dy = event.getRawY() - params.topMargin;
                        dx = event.getRawX() - params.leftMargin;

                        performClick = !handleTouched;
                        break;
                }
                return !performClick;
            }
        });
    }

    public interface onAboveViewListener {
        void onAboveView(View view);
    }
}