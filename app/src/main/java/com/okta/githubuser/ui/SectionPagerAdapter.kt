package com.okta.githubuser.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    private var username: String? = activity.intent.getStringExtra(DetailUserActivity.USERNAME)
    override fun createFragment(position: Int): Fragment {
        val fragment = FollowFragment()
        fragment.arguments = Bundle().apply {
            putInt(FollowFragment.ARG_POSITION, position)
            putString(FollowFragment.ARG_USERNAME, username)
        }
        return fragment
    }

    override fun getItemCount(): Int {
        return 2
    }
}