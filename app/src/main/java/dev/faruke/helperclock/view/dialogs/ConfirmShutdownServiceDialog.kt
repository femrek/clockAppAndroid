package dev.faruke.helperclock.view.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.FragmentActivity
import dev.faruke.helperclock.R
import dev.faruke.helperclock.service.FakeTimeService.Companion.mainFragmentViewModel
import kotlinx.android.synthetic.main.dialog_confirm_shutdown_service.*

class ConfirmShutdownServiceDialog(context: Context, private val activity: FragmentActivity) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_confirm_shutdown_service)
        dialogConfirmShutdownService_allowButton.setOnClickListener{ allowClick() }
        dialogConfirmShutdownService_cancelButton.setOnClickListener{ cancelClick() }

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
        dismiss()
    }

}