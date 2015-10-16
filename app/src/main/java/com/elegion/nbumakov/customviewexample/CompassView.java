package com.elegion.nbumakov.customviewexample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.elegion.nbumakov.customviewexample.util.IBearingAware;

/**
 * @author Nikita Bumakov
 */
public class CompassView extends View implements IBearingAware {

    //region ---------------------------------------- Variables

    private float northDirection; // in degrees

    private final Paint bitmapPaint = new Paint();
    private final Paint needlePaint = new Paint();

    private Canvas mCanvas;

    protected Bitmap mNeedleBitmap, mScaledRoseBitmap, mCanvasBitmap;

    protected int mCenterX, mCenterY, mSize;
    protected int mNeedleWidth = 8;

    //endregion

    public CompassView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        needlePaint.setAntiAlias(true);
        needlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        needlePaint.setStrokeWidth(1);

        bitmapPaint.setFilterBitmap(true);
        bitmapPaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mSize = Math.min(h, w);

        mCenterX = w / 2;
        mCenterY = h / 2;
        mNeedleWidth = mSize / 30;

        recycleBitmaps();

        mCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mCanvasBitmap);

        mScaledRoseBitmap = getRouse();
        mNeedleBitmap = createNeedle();
    }

    @Override
    public void onDraw(Canvas drawCanvas) {
        mCanvasBitmap.eraseColor(Color.TRANSPARENT);

        mCanvas.save();
        mCanvas.translate(mCenterX, mCenterY);
        mCanvas.drawBitmap(mScaledRoseBitmap, -mCenterX, -mCenterY, bitmapPaint);

        drawNeedle(mCanvas, northDirection);
        mCanvas.restore();

        drawCanvas.drawBitmap(mCanvasBitmap, 0, 0, null);
    }

    private void drawNeedle(Canvas canvas, float direction) {
        canvas.rotate(direction);
        canvas.drawBitmap(mNeedleBitmap, -mNeedleBitmap.getWidth() / 2, -mNeedleBitmap.getHeight() / 2, bitmapPaint);
        canvas.rotate(-direction);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size = width > height ? height : width;
        setMeasuredDimension(size, size);
    }

    @Override
    public void updateBearing(float bearing) {
        northDirection = -bearing;
        post(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        });
    }

    //region ---------------------------------------- Helpers methods

    protected Bitmap getRouse() {
        Bitmap roseOrigin = BitmapFactory.decodeResource(getResources(), R.raw.compass_rose_pale);
        Bitmap scaled = Bitmap.createScaledBitmap(roseOrigin, mSize, mSize, true);
        roseOrigin.recycle();
        return scaled;
    }

    private Bitmap createNeedle() {
        Bitmap bitmap = Bitmap.createBitmap(mNeedleWidth * 3, mSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Path needlePath = createNeedlePath();

        canvas.translate(mNeedleWidth * 1.5f, mSize / 2);

        needlePaint.setARGB(200, 255, 0, 0);
        canvas.drawPath(needlePath, needlePaint);

        canvas.rotate(180);
        needlePaint.setARGB(200, 0, 0, 255);
        canvas.drawPath(needlePath, needlePaint);

        needlePaint.setColor(Color.argb(255, 255, 230, 110));
        canvas.drawCircle(0, 0, mNeedleWidth * 1.5f, needlePaint);

        return bitmap;
    }

    private Path createNeedlePath() {
        Path needlePath = new Path();
        needlePath.moveTo(-mNeedleWidth, 0);
        needlePath.lineTo(0, -mSize * 0.425f);
        needlePath.lineTo(mNeedleWidth, 0);
        needlePath.close();
        return needlePath;
    }

    //endregion

    private void recycleBitmaps() {
        if (mScaledRoseBitmap != null) {
            mScaledRoseBitmap.recycle();
        }
        if (mNeedleBitmap != null) {
            mNeedleBitmap.recycle();
        }
        if (mCanvasBitmap != null) {
            mCanvasBitmap.recycle();
        }
    }
}
