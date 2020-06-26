package edu.hm.foodweek

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import edu.hm.foodweek.users.persistence.UserDao
import edu.hm.foodweek.users.persistence.model.User
import kotlinx.coroutines.*
import org.koin.android.ext.android.get
import java.util.logging.Logger

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_week, R.id.navigation_plan, R.id.navigation_shopping
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)
        lifecycleScope.launchWhenCreated {
            checkExistingUser()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                navController.popBackStack()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private suspend fun checkExistingUser() = withContext(Dispatchers.IO) {
        val userDao = get<UserDao>()
        val user = userDao.getUser()
        if (user == null) {
            userDao.insert(User("", ""))
            Logger.getLogger("MainActivity").fine("created user")
        } else {
            Logger.getLogger("MainActivity").fine("user exists already")
        }

    }

}
