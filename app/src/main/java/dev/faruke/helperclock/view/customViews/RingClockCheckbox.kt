package dev.faruke.helperclock.view.customViews

import android.content.Context
import android.util.AttributeSet
import android.widget.Checkable

class RingClockCheckbox : ClockEditView, Checkable {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun isChecked(): Boolean {
        return type == TYPE_RING_ENABLE
    }

    override fun toggle() {
        setChecked(type != TYPE_RING_ENABLE)
    }

    override fun setChecked(checked: Boolean) {
        type = if (checked) TYPE_RING_ENABLE else TYPE_RING_DISABLE
    }

}