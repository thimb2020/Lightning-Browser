package acr.browser.lightning

import acr.browser.lightning.database.bookmark.BookmarkRepository
import acr.browser.lightning.database.dao.AppDatabase
import acr.browser.lightning.database.dao.model.Category
import acr.browser.lightning.di.injector
import acr.browser.lightning.settings.activity.SettingsActivity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
import java.text.Collator
import java.util.*
import javax.inject.Inject
import kotlin.Comparator

class HomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var db: AppDatabase;
    lateinit var bookFragment: LinkFragment
    @Inject
    internal lateinit var bookmarkModel: BookmarkRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        db = AppDatabase(this);
        val newsDao = db.newsDao;

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
        bookmarkModel
                .deleteAllBookmarks()
                .subscribe()
        bookFragment = LinkFragment()
        supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, bookFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
        val startCategory = newsDao?.getStartCategory();
        if (startCategory != null) {
            supportActionBar?.title = startCategory?.name
        }
        navView.setNavigationItemSelectedListener {
            bookmarkModel
                    .deleteAllBookmarks()
                    .subscribe()
            supportActionBar?.title = it?.title
            newsDao?.clearStartCategory();
            newsDao?.updateStartCategory(it.itemId)
            bookFragment = LinkFragment()
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


        var listCategory = newsDao?.findAllCategory();
       // listCategory = listCategory?.sortedWith(Comparator { t1, t2 -> Collator.getInstance(Locale.FRANCE).compare(t1.name,t2.name) })
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