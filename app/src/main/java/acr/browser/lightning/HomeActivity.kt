package acr.browser.lightning

import acr.browser.lightning.database.dao.AppDatabase
import acr.browser.lightning.database.dao.dao.CategoryDao
import android.os.Bundle
import android.view.Menu
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentTransaction

class HomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var db: AppDatabase;
    lateinit var bookFragment: BookFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        bookFragment = BookFragment()
        supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, bookFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    // handle click
                    true
                }
                else -> {
                    bookFragment = BookFragment()
                    // creating the instance of the bundle
                    val bundle = Bundle()
                    // storing the string value in the bundle
                    // which is mapped to key
                    bundle.putInt("CategoryId", it.itemId)
                    bookFragment.arguments = bundle
                    supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.nav_host_fragment, bookFragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .commit()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
            }
        }
        db = AppDatabase(this);
        val categoryDao = db.categoryDao;
        val listCategory = categoryDao?.getAll();
        listCategory?.forEach {
            navView.menu.add(Menu.NONE, it.id, Menu.NONE, it.name);
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}