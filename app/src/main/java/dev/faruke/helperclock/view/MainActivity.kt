package dev.faruke.helperclock.view

import android.app.SearchManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import dev.faruke.helperclock.R
import dev.faruke.helperclock.model.PatternModel
import dev.faruke.helperclock.model.TimeModel
import dev.faruke.helperclock.service.FakeTimeService.Companion.endClock
import dev.faruke.helperclock.service.FakeTimeService.Companion.mainActivity
import dev.faruke.helperclock.service.FakeTimeService.Companion.mainFragmentViewModel
import dev.faruke.helperclock.service.FakeTimeService.Companion.ringClocks
import dev.faruke.helperclock.service.FakeTimeService.Companion.startClock
import dev.faruke.helperclock.util.UtilFuns
import dev.faruke.helperclock.view.customViews.ClockPatternCheckbox
import dev.faruke.helperclock.view.customViews.RingClockCheckbox
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer.*

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var listener: NavController.OnDestinationChangedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = this
        setContentView(R.layout.activity_main)
        setSupportActionBar(mainActivity_toolbar)

        navController = findNavController(R.id.fragment)
        navigationView.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(navController.graph, mainActivity_drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragment)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    fun addClickListener(view: View?) {
        if (view == null) return
        when (view.id) {
            R.id.drawer_addPattern -> {
                //val dialog = AddPatternDialog(this, this)
                //dialog.show()
                val intent = Intent(this, AddPatternActivity::class.java)
                startActivity(intent)
            }
            R.id.drawer_addRing -> {

            }
        }
    }

    fun ringClockClick(view: View?) {
        (view as RingClockCheckbox).toggle()
    }

}
