package hr.foi.air.fitfusion

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import hr.foi.air.fitfusion.adapters.MainPagerAdapter
import hr.foi.air.fitfusion.data_classes.LoggedInUser
import hr.foi.air.fitfusion.databinding.ActivityAdminWelcomeBinding
import hr.foi.air.fitfusion.entities.ProfileMenu
import hr.foi.air.fitfusion.fragments.AdminFragment


class WelcomeAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminWelcomeBinding
    private lateinit var tabLayout: TabLayout
    private lateinit var loggedInUser: LoggedInUser
    private lateinit var viewPager2: ViewPager2
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        binding = ActivityAdminWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loggedInUser = LoggedInUser(this)
        tabLayout = findViewById(R.id.tabs2)
        viewPager2 = findViewById(R.id.viewpager3)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        val imgButton = findViewById<ImageButton>(R.id.imageButtonUser)

        imgButton.setOnClickListener(){ button ->
            ProfileMenu.showMenu(
                context = this,
                anchor = button,
                menuRes = R.menu.account_menu,
                actionHandler = { itemId ->
                    when (itemId) {
                        R.id.settings_option -> {
                            val intent = Intent(this, UserProfile::class.java)
                            startActivity(intent)
                        }
                        R.id.logout_option -> {

                            ProfileMenu.handleLogout(this, loggedInUser, googleSignInClient)
                        }
                        else -> {
                        }
                    }
                }
            )

        }

        val mainPagerAdapter = MainPagerAdapter(supportFragmentManager, lifecycle)

        mainPagerAdapter.addFragment(
            MainPagerAdapter.FragmentItem(
                R.string.home,
                R.drawable.baseline_home_24,
                AdminFragment::class
            )
        )

        viewPager2.adapter = mainPagerAdapter
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.setText(mainPagerAdapter.fragmentItems[position].titleRes)
            tab.setIcon(mainPagerAdapter.fragmentItems[position].iconRes)
        }.attach()

        val firstName = loggedInUser.getFirstName()
        val welcomeMessage = "Welcome $firstName!"

        binding.txtWelcomeMessage.text = welcomeMessage

    }
}