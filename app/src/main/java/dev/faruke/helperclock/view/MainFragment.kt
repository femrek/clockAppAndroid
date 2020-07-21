package dev.faruke.helperclock.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import dev.faruke.helperclock.R
import dev.faruke.helperclock.util.UtilFuns
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
            viewModel!!.startButtonClick(context)
        }

        mainFragment_pause.setOnClickListener {
            viewModel!!.pauseButtonClick()
        }

        mainFragment_terminateButton.setOnClickListener {
            val dialog = ConfirmShutdownServiceDialog(requireContext())
            dialog.show()

        }

        /*val list: ArrayList<ArrayList<Int>> = ArrayList()
        for (i in 0..4) {
            val rowlist: ArrayList<Int> = ArrayList()
            for (j in 0..1) {
                rowlist.add(12+i+j)
            }
            list.add(rowlist)
        }
        println(UtilFuns.convertRingsListToString(list))*/

        //println(UtilFuns.convertRingsStringToArrayList("12,13;13,14;14,15;15,16;16,17;").toString())

        observeLiveData()
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
}
