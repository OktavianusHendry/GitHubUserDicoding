package com.okta.githubuser.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayoutMediator
import com.okta.githubuser.R
import com.okta.githubuser.data.local.entity.FavUserEntity
import com.okta.githubuser.data.remote.response.DetailUserResponse
import com.okta.githubuser.databinding.ActivityDetailUserBinding
import com.okta.githubuser.viewmodels.DetailUserViewModel
import com.okta.githubuser.viewmodels.FavUserViewModelFactory
import kotlinx.coroutines.launch

class DetailUserActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var detailUserViewModel: DetailUserViewModel

    private var favUser: FavUserEntity? = null
    private var isFavUser: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory: FavUserViewModelFactory = FavUserViewModelFactory.getInstance(this)
        detailUserViewModel = ViewModelProvider(this, factory).get(DetailUserViewModel::class.java)

        val sectionPagerAdapter = SectionPagerAdapter(this)
        binding.viewPager.adapter = sectionPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f

        val username = intent.getStringExtra(USERNAME)
        detailUserViewModel.findUser(username.toString())
        detailUserViewModel.findUserFollower(username.toString())
        detailUserViewModel.findUserFollowing(username.toString())
        detailUserViewModel.detailUser.observe(this) { detailUser ->
            setDetailUserData(detailUser)

            favUser = FavUserEntity(detailUser.login!!, detailUser.name, detailUser.avatarUrl)
            lifecycleScope.launch {
                isFavUser = detailUserViewModel.isFavoriteUser(detailUser.login!!).await()
                if (isFavUser) {
                    binding.fabFavorite.setImageResource(R.drawable.baseline_favorite_24)
                } else {
                    binding.fabFavorite.setImageResource(R.drawable.baseline_favorite_border_24)
                }
            }
        }
        detailUserViewModel.isLoading.observe(this) {
            showLoading(it)
        }


        binding.fabFavorite.setOnClickListener(this)
    }

    override fun onClick(view: View) {

        if (view.id == R.id.fabFavorite) {
            if (isFavUser) {
                isFavUser = false
                binding.fabFavorite.setImageResource(R.drawable.baseline_favorite_border_24)
                detailUserViewModel.delete(favUser!!)
                Toast.makeText(this, "Removed", Toast.LENGTH_SHORT).show()

            } else {
                isFavUser = true
                detailUserViewModel.save(favUser!!)
                binding.fabFavorite.setImageResource(R.drawable.baseline_favorite_24)
                Toast.makeText(this, "Favorited", Toast.LENGTH_SHORT).show()
            }
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

    private fun showLoading(state: Boolean) { binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE }//revisi submission 1

    companion object {
        const val USERNAME = "username"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.follower,
            R.string.following
        )
    }
}