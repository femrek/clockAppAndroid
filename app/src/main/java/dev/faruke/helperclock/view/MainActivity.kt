package dev.faruke.helperclock.view

import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import dev.faruke.helperclock.R
import dev.faruke.helperclock.service.FakeTimeService.Companion.mainActivity
import dev.faruke.helperclock.service.FakeTimeService.Companion.mainFragmentViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.reflect.Method


class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var listener: NavController.OnDestinationChangedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = this
        setContentView(R.layout.activity_main)
        setSupportActionBar(mainActivity_toolbar)
        requestedOrientation = (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when(id) {
            R.id.menu_linkedin -> {
                val browserIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/femrek/"))
                startActivity(browserIntent)
                return true
            }
            R.id.menu_google_play -> {
                val browserIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/dev?id=7986243585950801287"))
                startActivity(browserIntent)
                return true
            }
        }
        return false
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

    override fun onBackPressed() {
        if (mainActivity_drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mainActivity_drawerLayout.close()
        } else {
            super.onBackPressed()
        }
    }

    override fun onRestart() {
        super.onRestart()
        mainFragmentViewModel.let {
            it!!.refreshPatternsCheckboxes.value = true
        }
    }
}
