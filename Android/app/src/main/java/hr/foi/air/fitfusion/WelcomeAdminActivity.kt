package hr.foi.air.fitfusion

import android.content.Intent
import android.os.Bundle
import android.view.MenuInflater
import android.widget.ImageButton
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import hr.foi.air.fitfusion.adapters.MainPagerAdapter
import hr.foi.air.fitfusion.data_classes.LoggedInUser
import hr.foi.air.fitfusion.databinding.ActivityAdminWelcomeBinding
import hr.foi.air.fitfusion.fragments.AdminFragment


class WelcomeAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminWelcomeBinding
    private lateinit var tabLayout: TabLayout
    private lateinit var loggedInUser: LoggedInUser
    private lateinit var viewPager2: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        binding = ActivityAdminWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loggedInUser = LoggedInUser(this)
        tabLayout = findViewById(R.id.tabs2)
        viewPager2 = findViewById(R.id.viewpager3)

        val imgButton = findViewById<ImageButton>(R.id.imageButtonUser)

        imgButton.setOnClickListener(){
            val popup = PopupMenu(this, it)
            val inflater: MenuInflater = popup.menuInflater
            inflater.inflate(R.menu.account_menu, popup.menu)
            popup.show()
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

        binding.btnLogout.setOnClickListener {
            loggedInUser.clearUserData()
            val intent = Intent(this, LoginActivity2::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}