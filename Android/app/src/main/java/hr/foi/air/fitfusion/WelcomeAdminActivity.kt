package hr.foi.air.fitfusion

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import hr.foi.air.fitfusion.adapters.MainPagerAdapter
import hr.foi.air.fitfusion.databinding.ActivityAdminWelcomeBinding
import hr.foi.air.fitfusion.fragments.AdminFragment


class WelcomeAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminWelcomeBinding
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        binding = ActivityAdminWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tabLayout = findViewById(R.id.tabs2)
        viewPager2 = findViewById(R.id.viewpager3)

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
        val welcomeMessage = "Welcome!"
        binding.txtWelcomeMessage.text = welcomeMessage
    }
}