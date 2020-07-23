package dev.faruke.helperclock.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.navigation.NavigationView
import dev.faruke.helperclock.R
import dev.faruke.helperclock.model.PatternModel
import dev.faruke.helperclock.model.TimeModel
import dev.faruke.helperclock.service.FakeTimeService.Companion.endClock
import dev.faruke.helperclock.service.FakeTimeService.Companion.ringClocks
import dev.faruke.helperclock.service.FakeTimeService.Companion.startClock
import dev.faruke.helperclock.util.UtilFuns
import dev.faruke.helperclock.view.customViews.ClockPatternCheckbox
import dev.faruke.helperclock.view.dialogs.ConfirmShutdownServiceDialog
import dev.faruke.helperclock.service.FakeTimeService.Companion.mainFragmentViewModel as viewModel
import dev.faruke.helperclock.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel == null) {
            viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
            mainFragment_pause.isEnabled = false
            mainFragment_terminateButton.isEnabled = false
        }

        mainFragment_start.setOnClickListener {
            viewModel!!.startButtonClick(context, this)
        }

        mainFragment_pause.setOnClickListener {
            viewModel!!.pauseButtonClick()
        }

        mainFragment_terminateButton.setOnClickListener {
            val dialog = ConfirmShutdownServiceDialog(requireContext(), requireActivity())
            dialog.show()
        }

        addDefaultPatterns(view)

        observeLiveData()
    }

    var selectedPatternView: ClockPatternCheckbox? = null
        set(value) {
            field?.setChecked(false)
            field = value
            if (value != null) {
                value.setChecked(true)
                if (value.pattern != null) {
                    startClock =
                        TimeModel(value.pattern!!.startHour, value.pattern!!.startMinute, 0)
                    viewModel?.let { it.time.value = startClock }
                    endClock = TimeModel(value.pattern!!.endHour, value.pattern!!.endMinute, 0)
                    ringClocks =
                        UtilFuns.convertRingsStringToTimeModelArrayList(value.pattern!!.ringsList)
                }
            }
        }

    private fun updateCurrentPatternOnTheDrawer(drawerHeader: View) {

    }


    private val patternCheckboxClickListener = View.OnClickListener {
        val checkboxView = it as ClockPatternCheckbox
        selectedPatternView = checkboxView
    }


    private fun observeLiveData() {
        viewModel!!.time.observe(viewLifecycleOwner, Observer {time ->
            time?.let {
                mainFragment_clock.clock = time
            }
        })

        viewModel!!.startButtonEnable.observe(viewLifecycleOwner, Observer {enable ->
            mainFragment_start.isEnabled = enable
        })

        viewModel!!.pauseButtonEnable.observe(viewLifecycleOwner, Observer {enable ->
            mainFragment_pause.isEnabled = enable
        })
        viewModel!!.cancelButtonEnable.observe(viewLifecycleOwner, Observer {enable ->
            mainFragment_terminateButton.isEnabled = enable
        })
    }

    private fun addDefaultPatterns(mainFragmentView: View) {
        val header = mainFragmentView.rootView.findViewById<NavigationView>(R.id.navigationView).getHeaderView(0)
        val patternsLayout = header.findViewById<LinearLayout>(R.id.drawer_tile1_patternsLayout)
        val tytCheckbox = ClockPatternCheckbox(requireContext())
        tytCheckbox.pattern = PatternModel("TYT", 10, 15, 13, 0, "12,55;12,59;")
        val aytCheckbox = ClockPatternCheckbox(requireContext())
        aytCheckbox.pattern = PatternModel("AYT", 10, 15, 13, 15, "13,10;13,14;")
        tytCheckbox.setOnClickListener(patternCheckboxClickListener)
        aytCheckbox.setOnClickListener(patternCheckboxClickListener)
        patternsLayout.addView(tytCheckbox)
        patternsLayout.addView(aytCheckbox)
        selectedPatternView = tytCheckbox
    }
}
