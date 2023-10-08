package com.okta.githubuser.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.okta.githubuser.data.remote.response.ItemsItem
import com.okta.githubuser.databinding.ActivityFavoriteBinding
import com.okta.githubuser.viewmodels.FavUserViewModel
import com.okta.githubuser.viewmodels.FavUserViewModelFactory


class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var favUserViewModel: FavUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvFav.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvFav.addItemDecoration(itemDecoration)

        favUserViewModel = ViewModelProvider(
            this,
            FavUserViewModelFactory.getInstance(this)
        )[FavUserViewModel::class.java]

        favUserViewModel.getFavUsers().observe(this) { users ->
            val items = arrayListOf<ItemsItem>()
            if (users.isNotEmpty()) {
                binding?.rvFav?.visibility = View.VISIBLE
                binding?.tvNoFav?.visibility = View.GONE
            } else {
                binding?.rvFav?.visibility = View.GONE
                binding?.tvNoFav?.visibility = View.VISIBLE
            }
            users.map {
                val item = ItemsItem(login = it.username, avatarUrl = it.avatarUrl)
                items.add(item)
            }
            setFavUserData(items)
        }
    }

    private fun setFavUserData(users: List<ItemsItem>) {
        val adapter = UserAdapter()
        adapter.submitList(users)
        binding.rvFav.adapter = adapter
    }
}