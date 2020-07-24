package dev.faruke.helperclock.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import dev.faruke.helperclock.R
import dev.faruke.helperclock.service.FakeTimeService.Companion.mainActivity
import dev.faruke.helperclock.view.customViews.RingClockCheckbox
import kotlinx.android.synthetic.main.activity_main.*

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
                val intent = Intent(this, AddPatternActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        println("on restart")
        if (fragment is MainFragment){
            val mainFragment = fragment as MainFragment
            mainFragment.addPatterns()
        } else println("fragment is not main fragment")
    }

}
