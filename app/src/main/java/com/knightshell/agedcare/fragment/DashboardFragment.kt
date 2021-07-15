package com.knightshell.agedcare.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.knightshell.agedcare.R
import com.knightshell.agedcare.adapter.GraphViewPager


class DashboardFragment : Fragment()  {

    private lateinit var viewPager: ViewPager
    private lateinit var tabs: TabLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.fragment_dashboard, container, false)

        viewPager = view.findViewById<ViewPager>(R.id.viewPager)
        tabs = view.findViewById(R.id.tab_layout)



        val fragmentAdapter = GraphViewPager(childFragmentManager)
        viewPager.adapter = fragmentAdapter
        tabs.setupWithViewPager(viewPager)



        return view
    }

}
