package dev.faruke.helperclock.view.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Checkable
import androidx.constraintlayout.widget.ConstraintLayout
import dev.faruke.helperclock.R
import kotlinx.android.synthetic.main.view_clock_pattern_checkbox.view.*

class ClockPatternCheckbox : ConstraintLayout, Checkable{

    private var checked = false

    constructor(context: Context?) : super(context) {
        init()
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    fun init() {
        View.inflate(context, R.layout.view_clock_pattern_checkbox, this)
        setOnClickListener {
            toggle()
        }
    }


    fun checkedSettings() {
        viewClock_patternCheckbox_icon.setImageResource(R.drawable.ic_icon_done_white)
        viewClock_patternCheckbox_iconBg.visibility = View.VISIBLE
    }

    fun uncheckedSettings() {
        viewClock_patternCheckbox_icon.setImageResource(R.drawable.ic_icon_done_disabled)
        viewClock_patternCheckbox_iconBg.visibility = View.INVISIBLE
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