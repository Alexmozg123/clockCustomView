package com.example.clockcustomview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import java.util.*
import kotlin.math.min

class ClockView(
    context: Context,
    attrs: AttributeSet,
) : View(context, attrs) {

    private var clockArrowsColor: Int
    private var clockCenterColor: Int
    private var clockCircleColor: Int
    private var clockNumbersColor: Int
    private var mShowClock: Boolean
    private val mArrowsPointWidth = 8f
    private val mPointRange = 20F
    private val mNumberSpace = 10f
    private val mCircleWidth = 4.0F
    private val scaleMax = 50
    private val scaleMin = 25
    private var mWidth = 0
    private var mHeight = 0
    private var radius = 300.0F

    private val mPaint: Paint by lazy { Paint() }
    private val mRect: Rect by lazy { Rect() }

    init {
        mPaint.textSize = 35F
        mPaint.typeface = Typeface.DEFAULT_BOLD
        mPaint.isAntiAlias = true

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ClockView,
            0, 0
        ).apply {
            try {
                mShowClock = getBoolean(R.styleable.ClockView_showClock, false)
                clockNumbersColor = getColor(R.styleable.ClockView_clockNumbersColor, Color.BLACK)
                clockCircleColor = getColor(R.styleable.ClockView_clockCircleColor, Color.BLACK)
                clockCenterColor = getColor(R.styleable.ClockView_clockCenterColor, Color.BLACK)
                clockArrowsColor = getColor(R.styleable.ClockView_clockArrowsColor, Color.BLACK)
            } finally {
                recycle()
            }
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        mWidth = onMeasuredSpec(widthMeasureSpec) + (mCircleWidth * 2).toInt()
        mHeight = onMeasuredSpec(heightMeasureSpec) + (mCircleWidth * 2).toInt()

        radius = (mWidth - mCircleWidth * 2) / 2
        setMeasuredDimension(mWidth, mHeight)
    }

    private fun onMeasuredSpec(measureSpec: Int): Int {

        var specViewSize = 0
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)

        when (specMode) {
            MeasureSpec.EXACTLY -> {
                specViewSize = specSize
            }
            MeasureSpec.AT_MOST -> {
                specViewSize = min((radius * 2).toInt(), specSize)
            }
        }
        return specViewSize
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX: Float = (mWidth / 2).toFloat()
        val centerY: Float = (mHeight / 2).toFloat()

        canvas.translate(centerX, centerY)
        drawClockScale(canvas)
        drawPointer(canvas)

        postInvalidateDelayed(500)
    }

    private fun drawClockScale(canvas: Canvas) {
        for (index in 1..60) {
            canvas.rotate(6F, 0F, 0F)
            if (index % 5 == 0) {
                mPaint.strokeWidth = 4.0F
                canvas.drawLine(0F, -radius, 0F, -radius + scaleMax, mPaint)
                canvas.save()
                mPaint.strokeWidth = 1.0F
                mPaint.style = Paint.Style.FILL
                mPaint.getTextBounds(
                    (index / 5).toString(),
                    0,
                    (index / 5).toString().length,
                    mRect
                )
                canvas.translate(0F, -radius + mNumberSpace + scaleMax + (mRect.height() / 2))
                canvas.rotate((index * -6).toFloat())
                canvas.drawText(
                    (index / 5).toString(), -mRect.width() / 2.toFloat(),
                    mRect.height().toFloat() / 2, mPaint
                )
                canvas.restore()
            } else {
                mPaint.strokeWidth = 2.0F
                canvas.drawLine(0F, -radius, 0F, -radius + scaleMin, mPaint)
            }
        }
    }

    private fun drawPointer(canvas: Canvas) {
        val calendar = Calendar.getInstance()
        val hour = calendar[Calendar.HOUR]
        val minute = calendar[Calendar.MINUTE]
        val second = calendar[Calendar.SECOND]
        val angleHour = (hour + minute.toFloat() / 60) * 360 / 12
        val angleMinute = (minute + second.toFloat() / 60) * 360 / 60
        val angleSecond = second * 360 / 60

        mPaint.strokeWidth = mArrowsPointWidth
        mPaint.color = clockArrowsColor

        canvas.save()
        canvas.rotate(angleHour, 0F, 0F)
        val rectHour = RectF(
            -mArrowsPointWidth / 2,
            -radius / 2,
            mArrowsPointWidth / 2,
            radius / 6
        )
        mPaint.style = Paint.Style.STROKE
        canvas.drawRoundRect(rectHour, mPointRange, mPointRange, mPaint)
        canvas.restore()

        canvas.save()
        canvas.rotate(angleMinute, 0F, 0F)
        val rectMinute = RectF(
            -mArrowsPointWidth / 2,
            -radius * 3.5f / 5,
            mArrowsPointWidth / 2,
            radius / 6
        )
        canvas.drawRoundRect(rectMinute, mPointRange, mPointRange, mPaint)
        canvas.restore()

        canvas.save()
        canvas.rotate(angleSecond.toFloat(), 0F, 0F)
        val rectSecond = RectF(
            -mArrowsPointWidth / 2,
            -radius / 1.3f,
            mArrowsPointWidth / 2,
            radius / 6
        )
        canvas.drawRoundRect(rectSecond, mPointRange, mPointRange, mPaint)
        canvas.restore()

        mPaint.color = clockCenterColor
        mPaint.style = Paint.Style.FILL
        canvas.drawCircle(
            0F,
            0F, mArrowsPointWidth * 3, mPaint
        )
    }
}