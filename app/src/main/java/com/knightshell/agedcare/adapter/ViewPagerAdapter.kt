package com.knightshell.agedcare.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.knightshell.agedcare.fragment.BlogFragment
import com.knightshell.agedcare.fragment.DashboardFragment
import com.knightshell.agedcare.fragment.HealthDataFragment

class ViewPagerAdapter (fm: FragmentManager) : FragmentPagerAdapter(fm){
    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> {
                return DashboardFragment()
            }
            1 -> {
                return HealthDataFragment()
            }
            else -> {
                return BlogFragment()
            }
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title: String? = null

        if (position == 0){
            title = "Dashboard"
        }

        else if(position == 1){
            title = "Health Data"
        }

        else if (position == 2){
            title = "Blog"
        }
        return title

    }

}