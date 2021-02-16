package acr.browser.lightning

import acr.browser.lightning.database.dao.AppDatabase
import acr.browser.lightning.database.dao.dao.CategoryDao
import acr.browser.lightning.settings.activity.SettingsActivity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.InitializationStatus
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener
import java.text.Collator
import java.util.*
import kotlin.Comparator

class HomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var db: AppDatabase;
    lateinit var bookFragment: BookFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        MobileAds.initialize(this) {}
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
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
            supportActionBar?.title = it?.title
            bookFragment = BookFragment()
            val bundle = Bundle()
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
        db = AppDatabase(this);
        val categoryDao = db.categoryDao;
        var listCategory = categoryDao?.getAll();
        listCategory = listCategory?.sortedWith(Comparator { t1, t2 -> Collator.getInstance(Locale.FRANCE).compare(t1.name,t2.name) })
        listCategory?.forEach {
            navView.menu.add(Menu.NONE, it.id, Menu.NONE, it.name);
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_home -> {
                var intent = Intent(this, HomeActivity::class.java)
                ContextCompat.startActivity(this, intent, null)
            }
            R.id.action_settings -> {
                var intent = Intent(this, SettingsActivity::class.java)
                ContextCompat.startActivity(this, intent, null)
            }
            R.id.action_share -> CommonUtils.shareApp(this)
            R.id.action_rating -> CommonUtils.ratingApp(this)
            R.id.action_more -> CommonUtils.goToMyStore(this)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}