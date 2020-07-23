package dev.faruke.helperclock.view.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.View
import dev.faruke.helperclock.R
import kotlinx.android.synthetic.main.view_clock_edittext.view.*


open class ClockEditView : ClockViewer {

    var type : Int = 0x00
    set(value) {
        field = value
        when(field) {
            TYPE_NONE -> noneSettings()
            TYPE_REPLACE -> replaceSettings()
            TYPE_DELETE -> deleteSettings()
            TYPE_RING_ENABLE -> ringEnableSettings()
            TYPE_RING_DISABLE -> ringDisableSettings()
        }
    }

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    override fun init() {
        View.inflate(context, R.layout.view_clock_edittext, this)
        clockTextView = viewClockEdittext_text
    }

    override fun setAttrs(attrs: AttributeSet?) {
        super.setAttrs(attrs)
        val ta = context.obtainStyledAttributes(attrs, R.styleable.ClockEditView, 0, 0)
        try {
            val attrClickAction = ta.getInt(R.styleable.ClockEditView_actionType, 0x00)
            type = attrClickAction
        } finally {
            ta.recycle()
        }
    }


    private fun noneSettings() {
        viewClockEdittext_icon.setImageResource(android.R.color.transparent)
        viewClockEdittext_icon.visibility = View.INVISIBLE
    }

    private fun replaceSettings() {
        viewClockEdittext_icon.setImageResource(R.drawable.ic_icon_replace_black)
        viewClockEdittext_icon.visibility = View.VISIBLE
    }

    private fun deleteSettings() {
        viewClockEdittext_icon.setImageResource(R.drawable.ic_icon_delete_red)
        viewClockEdittext_icon.visibility = View.VISIBLE
    }

    private fun ringEnableSettings() {
        viewClockEdittext_icon.setImageResource(R.drawable.ic_icon_ring_enabled)
        viewClockEdittext_icon.visibility = View.VISIBLE
    }

    private fun ringDisableSettings() {
        viewClockEdittext_icon.setImageResource(R.drawable.ic_icon_ring_disabled)
        viewClockEdittext_icon.visibility = View.VISIBLE
    }


    companion object {
        const val TYPE_NONE: Int = 0x00
        const val TYPE_REPLACE: Int = 0x01
        const val TYPE_DELETE: Int = 0x02
        const val TYPE_RING_ENABLE = 0x03
        const val TYPE_RING_DISABLE = 0x04
    }
}
