package dev.faruke.helperclock.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.navigation.NavigationView
import dev.faruke.helperclock.R
import dev.faruke.helperclock.model.PatternModel
import dev.faruke.helperclock.model.TimeModel
import dev.faruke.helperclock.service.FakeTimeService.Companion.currentPatternTitle
import dev.faruke.helperclock.service.FakeTimeService.Companion.currentService
import dev.faruke.helperclock.service.FakeTimeService.Companion.endClock
import dev.faruke.helperclock.service.FakeTimeService.Companion.mutedRings
import dev.faruke.helperclock.service.FakeTimeService.Companion.ringClocks
import dev.faruke.helperclock.service.FakeTimeService.Companion.startClock
import dev.faruke.helperclock.util.LastUsedPattern
import dev.faruke.helperclock.util.UtilFuns
import dev.faruke.helperclock.view.customViews.*
import dev.faruke.helperclock.view.dialogs.ConfirmShutdownServiceAndCheckDialog
import dev.faruke.helperclock.view.dialogs.ConfirmShutdownServiceDialog
import dev.faruke.helperclock.service.FakeTimeService.Companion.mainFragmentViewModel as viewModel
import dev.faruke.helperclock.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {

    var header: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*if (viewModel == null) {
            viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
            mainFragment_pause.isEnabled = false
            mainFragment_terminateButton.isEnabled = false
        }*/

        if (currentService != null) {
            mainFragment_start.isEnabled = false
            mainFragment_pause.isEnabled = true
            mainFragment_terminateButton.isEnabled = true
        } else {
            mainFragment_start.isEnabled = true
            mainFragment_pause.isEnabled = false
            mainFragment_terminateButton.isEnabled = false
        }

        if (viewModel == null) viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

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

        header = view.rootView.findViewById<NavigationView>(R.id.navigationView).getHeaderView(0)
        viewModel!!.refreshPatternsCheckboxes.value = true

        observeLiveData()
    }

    var selectedPatternView: ClockPatternCheckbox? = null
        set(value) {
            if (currentService != null) {
                field = checkTheCheckboxForCurrentPattern()
                field?.setChecked(true)
                return
            }
            if (value == null) return
            field?.setChecked(false)
            field = value
            value.setChecked(true)
            if (value.pattern != null) {
                viewModel!!.titleText.value = value.pattern!!.title
                startClock =
                    TimeModel(value.pattern!!.startHour, value.pattern!!.startMinute, 0)
                viewModel?.let { it.time.value = startClock }
                endClock = TimeModel(value.pattern!!.endHour, value.pattern!!.endMinute, 0)
                ringClocks =
                    UtilFuns.convertRingsStringToTimeModelArrayList(value.pattern!!.ringsList)
                updateCurrentPatternOnTheDrawer()
                LastUsedPattern.saveLastPattern(requireContext(), value.pattern!!.id)
            }
        }

    private fun updateCurrentPatternOnTheDrawer() {
        if (header == null) return
        val ringsLayout = header!!.findViewById<LinearLayout>(R.id.drawer_tile0_ringsLayout)
        val startClockView = header!!.findViewById<ClockViewer>(R.id.drawer_tile0_startClock)
        val endClockView = header!!.findViewById<ClockViewer>(R.id.drawer_tile0_endClock)

        if (startClock != null && endClock != null) {
            startClockView.valueHour = startClock!!.hour
            startClockView.valueMinute = startClock!!.minute
            endClockView.valueHour = endClock!!.hour
            endClockView.valueMinute = endClock!!.minute
        }

        ringsLayout.removeAllViews()
        val lp = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        lp.setMargins(0, UtilFuns.dpToPx(requireContext(), 8f).toInt(), 0, 0)
        if (ringClocks != null) for (timeModel in ringClocks!!) {
            val child = RingClockCheckbox(context)
            child.valueHour = timeModel.hour
            child.valueMinute = timeModel.minute
            child.type = ClockEditView.TYPE_RING_ENABLE
            child.setOnClickListener(currentPatternRingsOnClickListener)
            child.layoutParams = lp
            ringsLayout.addView(child)
        }
    }


    val patternCheckboxClickListener = View.OnClickListener {
        val checkboxView = it as ClockPatternCheckbox
        if (selectedPatternView != null && selectedPatternView!!.pattern != null && checkboxView.pattern != null && selectedPatternView!!.pattern!!.isEqual(checkboxView.pattern!!))
        else if (currentService == null) {
            selectedPatternView = checkboxView
        } else {
            val dialog = ConfirmShutdownServiceAndCheckDialog(
                requireContext(),
                requireActivity(),
                this,
                checkboxView
            )
            dialog.show()
        }
    }


    private val currentPatternRingsOnClickListener = View.OnClickListener {
        val ringClockCheckbox = (it as RingClockCheckbox)
        ringClockCheckbox.toggle()
        if (ringClockCheckbox.isChecked) {
            for ((index, timeModel) in mutedRings.withIndex()) {
                if (timeModel.hour == ringClockCheckbox.valueHour && timeModel.minute == ringClockCheckbox.valueMinute) {
                    mutedRings.removeAt(index)
                    break
                }
            }
        } else {
            mutedRings.add(TimeModel(ringClockCheckbox.valueHour, ringClockCheckbox.valueMinute, 0))
        }
    }

    fun checkTheCheckboxForCurrentPattern() : ClockPatternCheckbox? {
        if (header == null) {
            println("header is null")
            return null
        }
        val patternsLayout =
            header!!.findViewById<LinearLayout>(R.id.drawer_tile1_patternsLayout)
        if (currentPatternTitle != null && startClock != null && endClock != null && ringClocks != null)
            for (view in patternsLayout.children) {
                var checkboxView: ClockPatternCheckbox? = null
                var checkboxPattern: PatternModel? = null
                if (view is ClockPatternCheckbox) {
                    checkboxView = view
                    checkboxPattern = checkboxView.pattern
                } else if (view is ClockPatternCheckboxWithActions) {
                    checkboxView = view.clockPatternCheckbox ?: break
                    checkboxPattern = checkboxView.pattern
                } else {
                    println("there is big problem")
                    break
                }
                if (
                    checkboxPattern != null &&
                    checkboxPattern.title == currentPatternTitle &&
                    checkboxPattern.startHour == startClock!!.hour &&
                    checkboxPattern.startMinute == startClock!!.minute &&
                    checkboxPattern.startHour == startClock!!.hour &&
                    checkboxPattern.startMinute == startClock!!.minute &&
                    checkboxPattern.ringsList == UtilFuns.convertRingTimeModelsListToString(ringClocks!!)
                ) {
                    return checkboxView
                }
            }
        return null
    }

    fun addPatterns() {
        if (header == null) return
        val patternsLayout =
            header!!.findViewById<LinearLayout>(R.id.drawer_tile1_patternsLayout)
        patternsLayout.removeAllViews()

        val lp = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        lp.setMargins(0,UtilFuns.dpToPx(requireContext(), 8f).toInt(),0,0)

        val tytCheckbox = ClockPatternCheckbox(requireContext())
        tytCheckbox.pattern = PatternModel("TYT", 10, 15, 13, 0, "12,55;12,59;")
        val aytCheckbox = ClockPatternCheckbox(requireContext())
        aytCheckbox.pattern = PatternModel("AYT", 10, 15, 13, 15, "13,10;13,14;")

        tytCheckbox.setOnClickListener(patternCheckboxClickListener)
        aytCheckbox.setOnClickListener(patternCheckboxClickListener)

        tytCheckbox.layoutParams = lp
        aytCheckbox.layoutParams = lp

        tytCheckbox.pattern!!.id = -1
        aytCheckbox.pattern!!.id = -2

        patternsLayout.addView(tytCheckbox)
        patternsLayout.addView(aytCheckbox)

        if (currentService != null) {
            updateCurrentPatternOnTheDrawer()
        } else {
            val lastPatternId = LastUsedPattern.getLastPattern(requireContext())
            selectedPatternView = when (lastPatternId) {
                -1 -> tytCheckbox
                -2 -> aytCheckbox
                else -> null
            }
        }

        viewModel?.readPatternsFromDBAndShowIn(patternsLayout, this)
    }


    private fun observeLiveData() {
        viewModel!!.time.observe(viewLifecycleOwner, Observer { time ->
            time?.let {
                mainFragment_clock.clock = time
            }
        })

        viewModel!!.startButtonEnable.observe(viewLifecycleOwner, Observer { enable ->
            mainFragment_start.isEnabled = enable
        })

        viewModel!!.pauseButtonEnable.observe(viewLifecycleOwner, Observer { enable ->
            mainFragment_pause.isEnabled = enable
        })
        viewModel!!.cancelButtonEnable.observe(viewLifecycleOwner, Observer { enable ->
            mainFragment_terminateButton.isEnabled = enable
        })

        viewModel!!.refreshPatternsCheckboxes.observe(viewLifecycleOwner, Observer { value ->
            addPatterns()
        })

        viewModel!!.titleText.observe(viewLifecycleOwner, Observer {
            currentPatternTitle = it
            mainFragment_title.text = it
        })
    }

}
