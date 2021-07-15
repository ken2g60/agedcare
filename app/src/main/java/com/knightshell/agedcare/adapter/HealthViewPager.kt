package com.knightshell.agedcare.adapter


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.knightshell.agedcare.fragment.GlucoseFragment
import com.knightshell.agedcare.fragment.PressureFragment
import com.knightshell.agedcare.fragment.WeightFragment

class HealthViewPager (fm: FragmentManager) : FragmentPagerAdapter(fm){
    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> {
                return PressureFragment()
            }
            1 -> {
                return GlucoseFragment()
            }
            2 -> {
                return WeightFragment()
            }
            else -> {
                return PressureFragment()
            }
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title: String? = null

        if (position == 0){
            title = "Pressure"
        }

        else if(position == 1){
            title = "Glucose"
        }

        else if (position == 2){
            title = "Weight"
        }
        return title

    }

}