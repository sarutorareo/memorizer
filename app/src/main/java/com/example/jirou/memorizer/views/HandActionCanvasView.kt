package com.example.jirou.memorizer.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.widget.TextView
import com.example.jirou.memorizer.R
import com.example.jirou.memorizer.models.*

class HandActionCanvasView : TextView {
    private val mPaint = Paint()
    private val mPaintStroke = Paint()
    private var mActionVal : Int = AV_FOLD_100
    private var mCompared : Int = 0

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, R.attr.autoCompleteTextViewStyle)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    {
        mPaint.textSize = this.textSize //28f
        mPaint.isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        val height = this.measuredHeight.toFloat()-10
        val width = this.measuredWidth.toFloat()-10

        // 背景（上半分）
        mPaint.color = getActionColorUpper()
        canvas.drawRect(
                0f, 0f, width, height / 2, mPaint)

        // 背景（下半分）
        mPaint.color = getActionColorLower()
        canvas.drawRect(
                0f, height / 2f,  width, height, mPaint)

        // 枠線
        if (isDrawStroke()) {
            val margin = 8f
            mPaintStroke.color = getComparedColor()
            mPaintStroke.strokeWidth = 5f
            mPaintStroke.style = Paint.Style.STROKE
            canvas.drawRect(
                    margin, margin, width - margin, height - margin, mPaintStroke)
        }

        // 文字
        mPaint.color = Color.argb(255, 0, 0, 0)
        canvas.drawText(this.text.toString(), 0f, 30f, mPaint)
    }

    fun setActionVal(value: Int) {
        mActionVal = value
    }

    fun setCompared(value: Int?) {
        mCompared = value ?: 0
    }

    private fun isDrawStroke() : Boolean {
        return (mCompared != 0)
    }

    private fun getActionColorUpper() : Int
    {
        return when (mActionVal) {
            AV_FOLD_100 -> {
                Color.argb(255, 255, 255, 255)
            }
            AV_CALL_100 -> {
                Color.argb(255, 255, 255, 0)
            }
            else -> {
                Color.argb(255, 0, 255, 0)
            }
        }
    }

    private fun getActionColorLower() : Int
    {
        return when {
            ((mActionVal == AV_FOLD_100) || (mActionVal == AV_CALL_100) || (mActionVal == AV_RAISE_100)) -> {
                getActionColorUpper()
            }
            (mActionVal == AV_RAISE_OR_FOLD_50) -> {
                Color.argb(255, 255, 255, 255)
            }
            else -> {
                Color.argb(255, 255, 255, 0)
            }
        }
    }

    private fun getComparedColor() : Int
    {
        assert(mCompared != 0)

        return when {
            (mCompared > 0) -> {
                Color.argb(255, 255, 0, 0)
            }
            else -> {
                Color.argb(255, 0, 0, 255)
            }
        }
    }
}