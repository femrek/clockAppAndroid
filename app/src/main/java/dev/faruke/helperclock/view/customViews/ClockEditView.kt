package dev.faruke.helperclock.view.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import dev.faruke.helperclock.R
import kotlinx.android.synthetic.main.view_clock_edittext.view.*


class ClockEditView : ConstraintLayout {

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
            val clickAction = ta.getInt(R.styleable.ClockEditView_onClickAction, 0x00)
            if (clickAction != 0x00) {
                if (clickAction == 0x01) { //replace
                    replaceSettings()
                } else { //delete
                    deleteSettings()
                }
            }
        } finally {
            ta.recycle()
        }
    }

    private fun replaceSettings() {
        viewClockEdittext_icon.setImageResource(R.drawable.ic_icon_replace_black)
        setOnClickListener {
            //todo show dialog for change clock
        }
    }

    private fun deleteSettings() {
        viewClockEdittext_icon.setImageResource(R.drawable.ic_icon_delete_red)
        setOnClickListener {
            //todo show dialog for delete view
        }
    }
}
