package hr.foi.air.fitfusion.adapters

import android.graphics.drawable.AdaptiveIconDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import hr.foi.air.fitfusion.R
import hr.foi.air.fitfusion.fragments.CalendarFragment
import hr.foi.air.fitfusion.fragments.ForumFragment
import hr.foi.air.fitfusion.fragments.HomeFragment
import kotlin.reflect.KClass

class MainPagerAdapter(fragmentManager : FragmentManager, lifecycle : Lifecycle)
    : FragmentStateAdapter(fragmentManager, lifecycle) {

    data class FragmentItem(val  titleRes: Int, val  iconRes: Int, val fragmentClass: KClass<*>)

    val fragmentItems = ArrayList<FragmentItem>()

        fun addFragment(fragment: FragmentItem){
          fragmentItems.add(fragment)
        }


    override fun getItemCount(): Int = fragmentItems.size

    override fun createFragment(position: Int): Fragment {
        return fragmentItems[position].fragmentClass.java.newInstance() as Fragment
        }
    }

