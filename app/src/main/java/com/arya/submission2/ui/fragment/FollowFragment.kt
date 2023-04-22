package com.arya.submission2.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.arya.submission2.ui.adapter.UserAdapter
import com.arya.submission2.databinding.FragmentFollowBinding
import com.arya.submission2.data.remote.response.Item
import com.arya.submission2.ui.viewmodel.UserDetailViewModel


private const val ARG_POSITION = "position"
private const val ARG_USERNAME = "username"


class FollowFragment : Fragment() {
    private var position: Int? = null
    private var username: String? = null

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding

    private val userDetailViewModel: UserDetailViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.rvFollow?.layoutManager = LinearLayoutManager(requireActivity())
        if (position == 1) {
            userDetailViewModel.userFollowers.observe(viewLifecycleOwner) {
                showUsers(it)
            }
            userDetailViewModel.isLoadingFollowers.observe(viewLifecycleOwner) {
                showLoading(it)
            }
        } else {
            userDetailViewModel.userFollowing.observe(viewLifecycleOwner) {
                showUsers(it)
            }
            userDetailViewModel.isLoadingFollowing.observe(viewLifecycleOwner) {
                showLoading(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showUsers(users: ArrayList<Item>) {
        binding?.rvFollow?.adapter = UserAdapter(users) {
            //
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        @JvmStatic
        fun newInstance(position: Int, username: String) =
            FollowFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_POSITION, position)
                    putString(ARG_USERNAME, username)
                }
            }
    }
}