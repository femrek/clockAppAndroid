package dev.faruke.helperclock.view.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Checkable
import android.widget.FrameLayout
import dev.faruke.helperclock.R
import dev.faruke.helperclock.model.PatternModel
import kotlinx.android.synthetic.main.view_clock_pattern_checkbox.view.*

class ClockPatternCheckbox : FrameLayout, Checkable{

    private var checked = false
    var pattern: PatternModel? = null
        set(value) {
            field = value
            if (value != null) {
                viewClock_patternCheckbox_text.text = value.title
            }
        }

    constructor(context: Context) : super(context) {
        init()
        setChecked(false)
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
        View.inflate(context, R.layout.view_clock_pattern_checkbox, this)
    }

    private fun init(attrs: AttributeSet?) {
        init()
        val ta = context.obtainStyledAttributes(attrs, R.styleable.ClockPatternCheckbox, 0, 0)
        try {
            val attrChecked = ta.getBoolean(R.styleable.ClockPatternCheckbox_checked, false)
            setChecked(attrChecked)
            if (attrChecked) { //replace
                checkedSettings()
            } else { //delete
                uncheckedSettings()
            }

        } finally {
            ta.recycle()
        }
    }


    fun checkedSettings() {
        viewClock_patternCheckbox_icon.setImageResource(R.drawable.ic_icon_done_white)
        viewClock_patternCheckbox_iconBg.setBackgroundResource(R.drawable.custom_add_button_view_bg)
    }

    fun uncheckedSettings() {
        viewClock_patternCheckbox_icon.setImageResource(R.drawable.ic_icon_done_disabled)
        viewClock_patternCheckbox_iconBg.setBackgroundResource(R.drawable.custom_clock_view_bg)
    }


    override fun isChecked(): Boolean {
        return checked
    }

    override fun toggle() {
        setChecked(!checked)
    }

    override fun setChecked(p0: Boolean) {
        checked = p0
        if (p0) {
            checkedSettings()
        } else {
            uncheckedSettings()
        }
    }
}