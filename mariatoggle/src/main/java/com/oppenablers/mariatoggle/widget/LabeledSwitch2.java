package com.oppenablers.mariatoggle.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.ColorUtils;

import com.oppenablers.mariatoggle.R;
import com.oppenablers.mariatoggle.model.ToggleableView;

public class LabeledSwitch2 extends ToggleableView {

    private int width;
    private int height;

    private String textOn = "";
    private String textOff = "";
    private int colorOn;
    private int colorOff;
    private int borderWidth;
    private Typeface typeface;

    private float textSize;

    private int arcDiameter;
    private int arcRadius;

    private int handleRadius;
    private int handleCX;
    private int handleMinCX;
    private int handleMaxCX;
    private int handleTargetCX;
    private int handleTouchX;

    private boolean wasMoved;

    private Paint bgPaint;
    private Paint fgPaint;
    private Paint handlePaint;
    private Paint textPaint;
    private Paint debug;

    private int textHeight;
    private Rect textOnRect = new Rect();
    private Rect textOffRect = new Rect();

    private long touchStartTimeMillis;


    public LabeledSwitch2(Context context) {
        super(context);
    }

    public LabeledSwitch2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        loadAttributes(context, attrs);
        init();
    }

    public LabeledSwitch2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadAttributes(context, attrs);
        init();
    }

    public LabeledSwitch2(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        loadAttributes(context, attrs);
        init();
    }

    private void loadAttributes(Context context, AttributeSet attrs) {
        try (TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ToggleableView,
                0, 0
        )) {
            for (int i = 0; i < typedArray.getIndexCount(); i++) {
                int index = typedArray.getIndex(i);
                if (index == R.styleable.ToggleableView_isOn) {
                    isOn = typedArray.getBoolean(index, false);
                } else if (index == R.styleable.ToggleableView_textOn) {
                    textOn = typedArray.getString(index);
                } else if (index == R.styleable.ToggleableView_textOff) {
                    textOff = typedArray.getString(index);
                } else if (index == R.styleable.ToggleableView_colorOn) {
                    colorOn = typedArray.getColor(index, Color.parseColor("#A0A0A0"));
                } else if (index == R.styleable.ToggleableView_colorOff) {
                    colorOff = typedArray.getColor(index, Color.parseColor("#404040"));
                } else if (index == R.styleable.ToggleableView_borderWidth) {
                    borderWidth = typedArray.getDimensionPixelSize(index, 0);
                } else if (index == R.styleable.ToggleableView_android_fontFamily) {
                    int resourceId = typedArray.getResourceId(index, 0);
                    if (resourceId > 0 && !isInEditMode()) {
                        typeface = ResourcesCompat.getFont(context, resourceId);
                    }
                } else if (index == R.styleable.ToggleableView_android_textSize) {
                    textSize = typedArray.getDimension(index, 12);
                }
            }
        }
    }

    private void init() {
        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setColor(colorOn);

        fgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fgPaint.setColor(colorOff);

        debug = new Paint(Paint.ANTI_ALIAS_FLAG);
        debug.setColor(Color.rgb(255, 0, 255));
        handlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        handlePaint.setColor(colorOn);

        textPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(textSize);
        textPaint.setTypeface(typeface);
        textPaint.setColor(colorOn);
        textPaint.getTextBounds(textOn, 0, textOn.length(), textOnRect);
        textPaint.getTextBounds(textOff, 0, textOff.length(), textOffRect);

        Rect tempRect = new Rect();
        textPaint.getTextBounds("O", 0, 1, tempRect);
        textHeight = tempRect.height();
    }

    private boolean hitTestHandle(int touchX) {
        return touchX >= handleCX - arcRadius && touchX <= handleCX + arcRadius;
    }

    private int getTouchRelativeToHandle(int touchX) {
        return touchX - handleCX;
    }

    private static void drawBackground(Canvas canvas,
                                       int diameter,
                                       int width,
                                       int height,
                                       int shrink,
                                       Paint paint) {
        int radius = diameter / 2;
        canvas.drawArc(shrink, shrink, diameter, diameter - shrink, 90, 180, false, paint);
        canvas.drawArc(width - diameter, shrink, width - shrink, diameter - shrink, 270, 180, false, paint);
        canvas.drawRect(radius, shrink, width - radius, height - shrink, paint);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        float progress = (float) (handleCX - handleMinCX) / (handleMaxCX - handleMinCX);

        bgPaint.setColor(ColorUtils.blendARGB(colorOn, colorOff, progress));
        fgPaint.setColor(ColorUtils.blendARGB(colorOn, colorOff, 1 - progress));
        handlePaint.setColor(ColorUtils.blendARGB(colorOn, colorOff, progress));

        // background
//        canvas.drawRect(0, 0, width, height, debug);
//        canvas.drawArc(0, 0, arcDiameter, arcDiameter, 90, 180, false, bgPaint);
//        canvas.drawArc(width - arcDiameter, 0, width, arcDiameter, 270, 180, false, bgPaint);
//        canvas.drawRect(arcRadius, 0, width - arcRadius, height, bgPaint);
//
//        canvas.drawArc(4, 4, arcDiameter, arcDiameter - 4, 90, 180, false, fgPaint);
//        canvas.drawArc(width - arcDiameter, 4, width - 4, arcDiameter - 4, 270, 180, false, fgPaint);
//        canvas.drawRect(arcRadius, 4, width - arcRadius, height - 4, fgPaint);

        if (borderWidth > 0) {
            drawBackground(canvas, arcDiameter, width, height, 0, bgPaint);
            drawBackground(canvas, arcDiameter, width, height, borderWidth, fgPaint);
        } else {
            drawBackground(canvas, arcDiameter, width, height, 0, fgPaint);
        }

//        canvas.drawRect(handleCX - arcRadius, 0, handleCX + arcRadius, arcDiameter, debug);
        canvas.drawCircle(handleCX, arcRadius, handleRadius, handlePaint);
//        canvas.drawCircle(width - arcRadius, arcRadius, handleRadius, handlePaint);

        textPaint.setColor(ColorUtils.blendARGB(colorOn, colorOn ^ 0xFF000000, progress));
        canvas.drawText(textOff,
                (float) (((width - arcDiameter) / 2) - (textOffRect.width() / 2) + arcDiameter),
                (float) ((height / 2) + (textHeight / 2)),
                textPaint);
        textPaint.setColor(ColorUtils.blendARGB(colorOff, colorOff ^ 0xFF000000, 1 - progress));
        canvas.drawText(textOn,
                (float) (((width + arcDiameter) / 2) - (textOnRect.width() / 2) - arcDiameter),
                (float) ((height / 2) + (textHeight / 2)),
                textPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        int touchX = (int) event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                touchStartTimeMillis = System.currentTimeMillis();
                handleTouchX = touchX - handleCX;
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                wasMoved = true;

                handleCX = touchX - handleTouchX;

                // limit
                if (handleCX >= handleMaxCX) handleCX = handleMaxCX;
                else if (handleCX <= handleMinCX) handleCX = handleMinCX;

                invalidate();
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {

                long timeDifference = System.currentTimeMillis() - touchStartTimeMillis;
                if (timeDifference < 200) {
                    wasMoved = false;
                    performClick();
                } else {
                    wasMoved = true;
                    performClick();
                }

                break;
            }
            default:
                return true;
        }
        return true;
    }

    @Override
    public boolean performClick() {
        super.performClick();

        if (wasMoved) {
            setOn(handleCX > (width / 2));
            wasMoved = false;
        } else {
            setOn(!isOn);
        }

        handleTargetCX = isOn ? handleMaxCX : handleMinCX;

        ValueAnimator valueAnimator = ValueAnimator.ofInt(handleCX, handleTargetCX);
        valueAnimator.addUpdateListener(animation -> {
            handleCX = (int) animation.getAnimatedValue();
            invalidate();
        });
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.setDuration(250);
        valueAnimator.start();

        invalidate();

        if (onToggledListener != null) {
            onToggledListener.onToggled(this, isOn);
        }

        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            width = MeasureSpec.getSize(widthMeasureSpec);
            height = MeasureSpec.getSize(heightMeasureSpec);
            arcDiameter = height;
            arcRadius = arcDiameter / 2;
            handleRadius = arcDiameter / 3;
            handleMinCX = arcRadius;
            handleMaxCX = width - arcRadius;
            handleCX = isOn ? handleMaxCX : handleMinCX;
        }
    }
}
