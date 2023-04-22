package com.arya.submission2.ui.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.arya.submission2.ui.fragment.FollowFragment

class FollowAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    var username: String = ""
    override fun createFragment(position: Int): Fragment {
        return FollowFragment.newInstance(position + 1, username)
    }

    override fun getItemCount(): Int {
        return 2
    }
}