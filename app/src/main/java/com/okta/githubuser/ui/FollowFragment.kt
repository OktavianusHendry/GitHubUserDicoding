package com.okta.githubuser.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.okta.githubuser.data.response.ItemsItem
import com.okta.githubuser.databinding.FragmentFollowBinding

class FollowFragment : Fragment() {

    companion object{
        var ARG_POSITION = "arg_position"
        var ARG_USERNAME = "arg_username"
    }

    private lateinit var binding: FragmentFollowBinding
    private val detailUserViewModel: DetailUserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollow.layoutManager= layoutManager
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        binding.rvFollow.addItemDecoration(itemDecoration)

        if (arguments != null) {
            val argPosition = arguments?.getInt(ARG_POSITION)
            val argUsername = arguments?.getString(ARG_USERNAME)
            
            //Mengecek hasil data
            Log.d("TAG", "argUsername: $argUsername")
            Log.d("TAG", "argPosition: $argPosition")
            if (argPosition == 0){
                detailUserViewModel.findUserFollower(argUsername!!)
                detailUserViewModel.listUserFollower.observe(viewLifecycleOwner, {
                    setUserFollowerData(it)
                })
                detailUserViewModel.isLoadingFollower.observe(viewLifecycleOwner, {
                    showLoading(it)
                })
            }else{
                detailUserViewModel.findUserFollowing(argUsername!!)
                detailUserViewModel.listUserFollowing.observe(viewLifecycleOwner, {
                    setUserFollowingData(it)
                })
                detailUserViewModel.isLoadingFollowing.observe(viewLifecycleOwner, {
                    showLoading(it)
                })
            }
        }


        super.onViewCreated(view, savedInstanceState)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarFollow.visibility = View.VISIBLE
        } else {
            binding.progressBarFollow.visibility = View.GONE
        }
    }

    private fun setUserFollowerData(follower: List<ItemsItem>) {
        binding.rvFollow.apply {
            binding.rvFollow.layoutManager = LinearLayoutManager(context)
            val adapter = UserAdapter()
            adapter.submitList(follower)
            binding.rvFollow.adapter = adapter
        }
    }
    private fun setUserFollowingData(following: List<ItemsItem>) {
        binding.rvFollow.apply {
            binding.rvFollow.layoutManager = LinearLayoutManager(context)
            val adapter = UserAdapter()
            adapter.submitList(following)
            binding.rvFollow.adapter = adapter
        }
    }
}