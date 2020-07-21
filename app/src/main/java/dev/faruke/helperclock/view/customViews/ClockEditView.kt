package dev.faruke.helperclock.view.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import dev.faruke.helperclock.R
import kotlinx.android.synthetic.main.view_clock_edittext.view.*


class ClockEditView : ConstraintLayout {

    var valueHour : Int = 0
    set(value) {
        field = value
        viewClockEdittext_text.text = "$value:$valueMinute"
    }

    var valueMinute : Int = 0
    set(value) {
        field = value
        viewClockEdittext_text.text = "$valueHour:$value"
    }

    var type : Int = 0x00
    set(value) {
        field = value
        if (field == TYPE_REPLACE) {
            replaceSettings()
        } else if (field == TYPE_DELETE) { //delete
            deleteSettings()
        }
    }

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }


    private fun init() {
        View.inflate(context, R.layout.view_clock_edittext, this)
    }

    private fun init(attrs: AttributeSet?) {
        init()
        val ta = context.obtainStyledAttributes(attrs, R.styleable.ClockEditView, 0, 0)
        try {
            val attrClickAction = ta.getInt(R.styleable.ClockEditView_onClickAction, 0x00)
            val attrValueHour = ta.getInt(R.styleable.ClockEditView_valueHour, 0)
            val attrValueMinute = ta.getInt(R.styleable.ClockEditView_valueMinute, 0)
            type = attrClickAction
            valueHour = attrValueHour
            valueMinute = attrValueMinute
        } finally {
            ta.recycle()
        }
    }



    private fun replaceSettings() {
        viewClockEdittext_icon.setImageResource(R.drawable.ic_icon_replace_black)
    }

    private fun deleteSettings() {
        viewClockEdittext_icon.setImageResource(R.drawable.ic_icon_delete_red)
    }


    companion object {
        const val TYPE_REPLACE: Int = 0X01
        const val TYPE_DELETE: Int = 0x02
    }
}
