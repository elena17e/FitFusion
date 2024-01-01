package hr.foi.air.fitfusion

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuInflater
import android.widget.ImageButton
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import hr.foi.air.fitfusion.adapters.MainPagerAdapter
import hr.foi.air.fitfusion.data_classes.LoggedInUser
import hr.foi.air.fitfusion.databinding.ActivityWelcomeBinding
import hr.foi.air.fitfusion.entities.ProfileMenu
import hr.foi.air.fitfusion.fragments.CalendarFragment
import hr.foi.air.fitfusion.fragments.ForumFragment
import hr.foi.air.fitfusion.fragments.HomeFragment

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var loggedInUser: LoggedInUser
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loggedInUser = LoggedInUser(this)
        tabLayout = findViewById(R.id.tabs)
        viewPager2 = findViewById(R.id.viewpager)

        val imgButton = findViewById<ImageButton>(R.id.imageButtonUser)

        imgButton.setOnClickListener(){ button ->
            ProfileMenu.showMenu(
                context = this,
                anchor = button,
                menuRes = R.menu.account_menu,
                actionHandler = { itemId ->
                    when (itemId) {
                        R.id.settings_option -> {
                            //take user to settings
                        }
                        R.id.logout_option -> {
                            //handle logout action
                            ProfileMenu.handleLogout(this, loggedInUser)
                        }
                        else -> {
                            //something else
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
                HomeFragment::class
            )
        )
        mainPagerAdapter.addFragment(
            MainPagerAdapter.FragmentItem(
                R.string.calendar,
                R.drawable.baseline_calendar_month_24,
                CalendarFragment::class
            )
        )
        mainPagerAdapter.addFragment(
            MainPagerAdapter.FragmentItem(
                R.string.forum,
                R.drawable.baseline_forum_24,
                ForumFragment::class
            )
        )

        viewPager2.adapter = mainPagerAdapter
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.setText(mainPagerAdapter.fragmentItems[position].titleRes)
            tab.setIcon(mainPagerAdapter.fragmentItems[position].iconRes)
        }.attach()

        val firstName = loggedInUser.getFirstName()
        val welcomeMessage = "Welcome $firstName!"

        binding.welcomeMessageTextView.text = welcomeMessage

        /*binding.logoutButton.setOnClickListener {
            loggedInUser.clearUserData()
            val intent = Intent(this, LoginActivity2::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }*/
    }
}



