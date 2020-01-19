package com.mocan.autoreflex.ui.main

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.mocan.autoreflex.R
import com.mocan.autoreflex.UserMenuFactory
import com.mocan.autoreflex.ui.car.CarFragment
import com.mocan.autoreflex.ui.car.dummy.DummyContent
import com.mocan.autoreflex.ui.folder.FolderViewModel
import com.mocan.autoreflex.ui.learning.CategoryFragment
import com.mocan.autoreflex.ui.learning.PracticeFragment
import com.mocan.autoreflex.ui.login.LoginActivity
import com.mocan.autoreflex.ui.login.LoginViewModel
import com.mocan.autoreflex.ui.login.LoginViewModelFactory
import kotlinx.android.synthetic.main.activity_main_menu.*


class MainMenuActivity : AppCompatActivity(), CarFragment.OnListFragmentInteractionListener,
                        CategoryFragment.OnListFragmentInteractionListener{

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var loginViewModel: LoginViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        loginViewModel = ViewModelProviders.of(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)


        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            val i = Intent(Intent.ACTION_SEND)
            i.type = "message/rfc822"
            i.putExtra(Intent.EXTRA_EMAIL, arrayOf("office@autoreflex.ro"))
            i.putExtra(Intent.EXTRA_SUBJECT, "Aplicatie scoala")
            try {
                startActivity(Intent.createChooser(i, "Send mail..."))
            } catch (ex: ActivityNotFoundException) {
                Snackbar.make(view, "\"There are no email clients installed.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }

        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val toggle = object : ActionBarDrawerToggle(this,
            drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                val user = loginViewModel.alreadyLogged()


            findViewById<TextView>(R.id.textView).text = user!!.email
            findViewById<TextView>(R.id.displayName).text = user.displayName
            if (user.photoUrl != null)
                findViewById<ImageView>(R.id.imageView).setImageURI(user.photoUrl)
            }
        }

        drawerLayout.addDrawerListener(toggle)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.


        val bundle: Bundle? = intent.extras
        val message: String? = bundle?.getString("type")
        if (message != "scoala") {
            navController.popBackStack()
            navController.navigate(R.id.nav_tasks)
        }
        val factory = UserMenuFactory(message!!)
        val navGraph = factory.selectView(navView)

        Log.e("container", "message was null")
        ViewModelProviders.of(this).get(FolderViewModel::class.java).setAdmin(message)

        appBarConfiguration = AppBarConfiguration(
            navGraph,
            drawerLayout
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.nav_gallery)
                fab.hide()
            else
                fab.show()
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.sign_out)
            logout()
        else if (item.itemId == R.id.action_settings) {
            val navController = findNavController(R.id.nav_host_fragment)
            navController.navigate(R.id.user_profile)
        }

        return super.onOptionsItemSelected(item)
    }



    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun logout() {
        loginViewModel.logout()

        val myIntent = Intent(this, LoginActivity::class.java)
        startActivity(myIntent)

        finish()
    }

    override fun onListFragmentInteraction(item: DummyContent.CarItem?) {
        Log.e("interact", "view pressed")
//
    }

    override fun onListFragmentInteraction(item: String?, index: Int?) {
        val navController = findNavController(R.id.nav_host_fragment)
        PracticeFragment.index = index ?: 0
        PracticeFragment.category = item ?: ""
        navController.navigate(R.id.practice_fragment)
    }
}
