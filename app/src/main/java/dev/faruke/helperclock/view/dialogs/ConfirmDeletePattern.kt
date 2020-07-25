package dev.faruke.helperclock.view.dialogs

import android.app.Dialog
import android.graphics.Point
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import dev.faruke.helperclock.R
import dev.faruke.helperclock.service.FakeTimeService.Companion.mainFragmentViewModel
import dev.faruke.helperclock.view.MainFragment
import kotlinx.android.synthetic.main.dialog_confirm_delete_pattern.*

class ConfirmDeletePattern(
    private val patternId: Int,
    private val mainFragment: MainFragment
) :
    Dialog(mainFragment.requireContext()) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_confirm_delete_pattern)
        dialogConfirmDeletePattern_allowButton.setOnClickListener { allowClick() }
        dialogConfirmDeletePattern_cancelButton.setOnClickListener { cancelClick() }

        window?.let {
            val size = Point()
            val w: WindowManager = mainFragment.requireActivity().windowManager
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
        mainFragmentViewModel?.deletePattern(patternId, mainFragment)
        dismiss()
    }

}