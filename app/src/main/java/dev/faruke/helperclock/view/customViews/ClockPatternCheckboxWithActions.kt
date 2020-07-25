package dev.faruke.helperclock.view.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import dev.faruke.helperclock.R
import kotlinx.android.synthetic.main.view_clock_pattern_checkbox_w_delete_replace.view.*

class ClockPatternCheckboxWithActions : FrameLayout {

    var clockPatternCheckbox: ClockPatternCheckbox? = null
    var replaceIcon: ImageView? = null
    var deleteIcon: ImageView? = null

    constructor(context: Context) : super(context) {
        init()
        clockPatternCheckbox!!.setChecked(false)
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.view_clock_pattern_checkbox_w_delete_replace, this)
        clockPatternCheckbox = viewClockPatternCheckboxWithActions_clockPatternCheckbox
        replaceIcon = viewClockPatternCheckboxWithActions_replaceIcon
        deleteIcon = viewClockPatternCheckboxWithActions_deleteIcon
    }

    fun setReplaceClickListener(l: OnClickListener?) {
        if (l != null) replaceIcon?.visibility = View.VISIBLE
        else replaceIcon?.visibility = View.INVISIBLE
        replaceIcon?.setOnClickListener(l)
    }

    fun setDeleteClickListener(l: OnClickListener?) {
        if (l != null) deleteIcon?.visibility = View.VISIBLE
        else deleteIcon?.visibility = View.INVISIBLE
        deleteIcon?.setOnClickListener(l)
    }

    fun getPatternId() : Int? {
        return clockPatternCheckbox?.pattern?.id
    }
}
