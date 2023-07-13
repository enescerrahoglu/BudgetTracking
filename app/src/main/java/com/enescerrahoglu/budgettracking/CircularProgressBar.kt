package com.enescerrahoglu.budgettracking

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat

class CircularProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val progressPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = ContextCompat.getColor(context, R.color.primary)
        strokeWidth = 40f
    }

    private val backgroundPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.GRAY
        strokeWidth = 40f
    }

    private var progressValue: Int = 0

    fun setProgress(value: Int) {
        progressValue = value
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = (width / 2f) - 20f

        canvas.drawCircle(centerX, centerY, radius, backgroundPaint)

        val oval = RectF(20f, 20f, width.toFloat() - 20f, height.toFloat() - 20f)
        val sweepAngle = (progressValue / 100f) * 360f
        canvas.drawArc(oval, -90f, sweepAngle, false, progressPaint)
    }
}