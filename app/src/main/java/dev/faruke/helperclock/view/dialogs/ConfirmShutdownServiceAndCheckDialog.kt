package dev.faruke.helperclock.view.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.FragmentActivity
import dev.faruke.helperclock.R
import dev.faruke.helperclock.service.FakeTimeService.Companion.currentService
import dev.faruke.helperclock.service.FakeTimeService.Companion.mainFragmentViewModel
import dev.faruke.helperclock.view.MainFragment
import dev.faruke.helperclock.view.customViews.ClockPatternCheckbox
import kotlinx.android.synthetic.main.dialog_confirm_shutdown_and_check_service.*

class ConfirmShutdownServiceAndCheckDialog(
    context: Context,
    private val activity: FragmentActivity,
    private val mainFragment: MainFragment,
    private val clockPatternCheckbox: ClockPatternCheckbox
) : Dialog(context) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_confirm_shutdown_and_check_service)
        dialogConfirmShutdownAndCheckService_allowButton.setOnClickListener { allowClick() }
        dialogConfirmShutdownAndCheckService_cancelButton.setOnClickListener { cancelClick() }

        window?.let {
            val size = Point()
            val w: WindowManager = activity.windowManager
            w.defaultDisplay.getSize(size);
            val lp = it.attributes
            lp.width = (size.x * 0.85).toInt()
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        }
    }


    private fun cancelClick() {
        dismiss()
    }

    private fun allowClick() {
        mainFragmentViewModel?.terminateButtonClick(context)
        currentService = null
        mainFragment.selectedPatternView = clockPatternCheckbox
        dismiss()
    }


}