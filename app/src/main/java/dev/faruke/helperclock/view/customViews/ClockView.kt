package dev.faruke.helperclock.view.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import dev.faruke.helperclock.BuildConfig
import dev.faruke.helperclock.R
import dev.faruke.helperclock.model.TimeModel
import kotlinx.android.synthetic.main.view_clock.view.*

class ClockView : ConstraintLayout {

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

    var clock: TimeModel = TimeModel(0,0,0)
    set(value) {
        field = value
        setClock(field.hour, field.minute, field.second)
    }

    private fun init() {
        View.inflate(context, R.layout.view_clock, this)
    }

    private fun setClock(hour: Int, minute: Int, second: Int) {
        if (BuildConfig.DEBUG) {
            if (second >= 60) {
                error("second big than 60 or equal")
            }
            if (minute >= 60) {
                error("minute big than 60 or equal")
            }
            if (hour >= 24) {
                error("hour big than 24 or equal")
            }
        }

        val visibleHour = if (hour <= 12) hour else hour-12

        val secondRotation: Float = second * 6f
        val minuteRotation: Float = (minute * 6f) + (second / 10f)
        val hourRotation: Float = (visibleHour * 30f) + (minute / 2f);

        view_clock_stick_saniye.rotation = secondRotation
        view_clock_stick_yelkovan.rotation = minuteRotation
        view_clock_stick_akrep.rotation = hourRotation
    }
}