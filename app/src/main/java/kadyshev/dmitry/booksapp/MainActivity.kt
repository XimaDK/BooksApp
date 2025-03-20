package kadyshev.dmitry.booksapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navController =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.findNavController()
        bottomNavigationView = findViewById(R.id.bottomNavigation)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_search -> {
                    if (navController?.currentDestination?.id != R.id.searchFragment) {
                        navController?.popBackStack(R.id.searchFragment, false)
                        navController?.navigate(R.id.searchFragment)
                    }
                    true
                }

                R.id.nav_favorite -> {
                    if (navController?.currentDestination?.id != R.id.favoriteFragment) {
                        navController?.popBackStack(R.id.favoriteFragment, false)
                        navController?.navigate(R.id.favoriteFragment)
                    }
                    true
                }

                else -> false
            }
        }

        navController?.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.searchFragment -> {
                    if (bottomNavigationView.selectedItemId != R.id.nav_search) {
                        bottomNavigationView.selectedItemId = R.id.nav_search
                    }
                }

                R.id.favoriteFragment -> {
                    if (bottomNavigationView.selectedItemId != R.id.nav_favorite) {
                        bottomNavigationView.selectedItemId = R.id.nav_favorite
                    }
                }
            }
        }
    }
}