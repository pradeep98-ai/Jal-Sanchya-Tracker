package com.mindmatrix.jalsanchaytracker.ui

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.mindmatrix.jalsanchaytracker.R

class WaterTankView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val tankPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.TRANSPARENT
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }
    private val waterPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.success)
        style = Paint.Style.FILL
    }
    private val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.ink)
        textAlign = Paint.Align.CENTER
        textSize = 36f
        isFakeBoldText = true
    }
    private val bounds = RectF()
    private var animatedFill = 0f
    private var targetFill = 0f

    fun setFill(fill: Float) {
        targetFill = fill.coerceIn(0f, 1f)
        ValueAnimator.ofFloat(animatedFill, targetFill).apply {
            duration = 550
            addUpdateListener {
                animatedFill = it.animatedValue as Float
                invalidate()
            }
        }.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val padding = 18f
        bounds.set(padding, padding, width - padding, height - padding)

        val waterHeight = bounds.height() * animatedFill
        val waterTop = bounds.bottom - waterHeight
        canvas.drawRoundRect(RectF(bounds.left, waterTop, bounds.right, bounds.bottom), 22f, 22f, waterPaint)

        tankPaint.color = ContextCompat.getColor(context, R.color.primary)
        canvas.drawRoundRect(bounds, 22f, 22f, tankPaint)

        canvas.drawText("${(targetFill * 100).toInt()}%", width / 2f, height / 2f + 12f, labelPaint)
    }
}
