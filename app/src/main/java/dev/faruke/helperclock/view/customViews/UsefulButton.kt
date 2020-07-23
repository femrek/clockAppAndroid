package dev.faruke.helperclock.view.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import dev.faruke.helperclock.R
import dev.faruke.helperclock.util.UtilFuns
import kotlinx.android.synthetic.main.view_button.view.*

class UsefulButton : FrameLayout {

    var text: String = ""
    set(value) {
        field = value
        viewButton_text.text = field
    }

    var textColor: Int = 0x00000000
    set(value) {
        field = value
        viewButton_text.setTextColor(field)
    }

    var textSize: Float = 0f
    set(value) {
        field = value
        viewButton_text.textSize = value
    }

    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    private fun init() {
        View.inflate(context, R.layout.view_button, this)
    }

    private fun init(attrs: AttributeSet?) {
        init()
        val ta = context.obtainStyledAttributes(attrs, R.styleable.UsefulButton, 0, 0)
        try {
            val attrBackground = ta.getInt(R.styleable.UsefulButton_backgroundDrawable, R.drawable.custom_clock_view_bg)
            val attrText = ta.getString(R.styleable.UsefulButton_text)
            val attrTextColor = ta.getColor(R.styleable.UsefulButton_textColor, resources.getColor(R.color.bg2))
            val attrTextSize = ta.getDimensionPixelSize(R.styleable.UsefulButton_textSize,  20).toFloat()
            setBackgroundResource(attrBackground)
            text = attrText ?: ""
            textColor = attrTextColor
            textSize = attrTextSize
        } finally {
            ta.recycle()
        }
    }


    override fun setBackgroundResource(resid: Int) {
        viewButton_root.setBackgroundResource(resid)
    }

}