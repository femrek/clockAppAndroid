package dev.faruke.helperclock.view.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import dev.faruke.helperclock.R
import dev.faruke.helperclock.service.FakeTimeService.Companion.mainFragmentViewModel
import kotlinx.android.synthetic.main.dialog_confirm_shutdown_service.*

class ConfirmShutdownServiceDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_confirm_shutdown_service)
        dialogConfirmShutdownService_allowButton.setOnClickListener{ allowClick() }
        dialogConfirmShutdownService_cancelButton.setOnClickListener{ cancelClick() }
    }


    private fun cancelClick() {
        dismiss()
    }

    private fun allowClick() {
        mainFragmentViewModel?.terminateButtonClick(context)
        dismiss()
    }

}