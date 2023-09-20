package com.okta.githubuser.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayoutMediator
import com.okta.githubuser.R
import com.okta.githubuser.data.response.DetailUserResponse
import com.okta.githubuser.databinding.ActivityDetailUserBinding

class DetailUserActivity : AppCompatActivity() {

    companion object {
        const val USERNAME = "username"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.follower,
            R.string.following
        )
    }

    private lateinit var binding: ActivityDetailUserBinding
    private val detailUserViewModel by viewModels<DetailUserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionPagerAdapter = SectionPagerAdapter(this)
        binding.viewPager.adapter = sectionPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f


        val username = intent.getStringExtra(USERNAME)
        detailUserViewModel.findUser(this, username.toString())
        detailUserViewModel.findUserFollower(username.toString())
        detailUserViewModel.findUserFollowing(username.toString())
        detailUserViewModel.detailUser.observe(this) { detailUser ->
            setDetailUserData(detailUser)
        }
        detailUserViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun setDetailUserData(detailUser: DetailUserResponse) {
        val requestOptions = RequestOptions()
            .transform(CircleCrop())

        Glide.with(this)
            .load(detailUser.avatarUrl)
            .apply(requestOptions)
            .into(binding.imgUser)
        binding.tvName.text = detailUser.name
        binding.tvUsername.text = detailUser.login
        binding.tvFollowers.text = detailUser.followers.toString() + " Followers"
        binding.tvFollowing.text = detailUser.following.toString() + " Following"
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}