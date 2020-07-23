package dev.faruke.helperclock.view.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import dev.faruke.helperclock.R
import kotlinx.android.synthetic.main.view_clock_viewer.view.*

open class ClockViewer : ConstraintLayout {

    var clockTextView: TextView? = null

    var valueHour : Int = 0
        set(value) {
            field = value
            clockTextView?.text = clockString()
        }

    var valueMinute : Int = 0
        set(value) {
            field = value
            clockTextView?.text = clockString()
        }

    constructor(context: Context?) : super(context) {
        init()
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
        setAttrs(attrs)
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
        setAttrs(attrs)
    }

    open fun init() {
        View.inflate(context, R.layout.view_clock_viewer, this)
        clockTextView = viewClockViewer_text
    }

    open fun setAttrs(attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.ClockViewer, 0, 0)
        try {
            val attrValueHour = ta.getInt(R.styleable.ClockViewer_valueHour, 0)
            val attrValueMinute = ta.getInt(R.styleable.ClockViewer_valueMinute, 0)
            valueHour = attrValueHour
            valueMinute = attrValueMinute
        } finally {
            ta.recycle()
        }
    }


    private fun clockString() : String {
        var result = ""

        if (valueHour < 10) result +="0"
        result += "$valueHour:"

        if (valueMinute < 10) result += "0"
        result += "$valueMinute"

        return result
    }



}