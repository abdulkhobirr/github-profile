package com.example.github_profile.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.github_profile.data.profile.model.GetUserProfileResponse
import com.example.github_profile.databinding.ActivityMainBinding
import com.example.github_profile.utils.showDefaultState
import com.example.github_profile.utils.showErrorState
import com.example.github_profile.utils.showLoadingState
import com.example.github_profile.utils.viewmodel.ResultWrapper
import com.example.github_profile.viewmodel.ProfileViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), UserAdapter.OnUserItemClicked {
    private lateinit var binding: ActivityMainBinding

    private val profileViewModel: ProfileViewModel by viewModel()
    private lateinit var userAdapter: UserAdapter

    private var lastId = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initActions()
        initRV()
        initObservable()
        profileViewModel.getUsers()
    }

    private fun initRV(){
        userAdapter = UserAdapter(listener = this)

        binding.rvUser.apply {
            layoutManager =
                LinearLayoutManager(
                    context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            setHasFixedSize(true)
            adapter = userAdapter
        }

        binding.rvUser.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    profileViewModel.updateSince(lastId)
                    profileViewModel.getUsers()
                }
            }
        })
    }

    private fun initActions(){
        binding.swipeRefresh.setOnRefreshListener {
            profileViewModel.resetSince()
            profileViewModel.getUsers()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun initObservable(){
        profileViewModel.listUsers.observe(this, {
            when (it) {
                is ResultWrapper.Loading -> {
                    if (profileViewModel.getSinceCount() == 1) {
                        binding.msvUser.showLoadingState()
                    } else {
                        binding.swipeRefresh.post {
                            binding.swipeRefresh.isRefreshing = true
                        }
                    }
                }
                is ResultWrapper.Success -> {
                    binding.msvUser.showDefaultState()
                    profileViewModel.getUserProfile(it.data)
                }
                is ResultWrapper.Failure -> {
                    binding.msvUser.showErrorState(
                        title = it.title,
                        errorMessage = it.message,
                        errorAction = {
                            profileViewModel.getUsers()
                        }
                    )
                }
            }
        })

        profileViewModel.listUserDetail.observe(this, {
            when (it) {
                is ResultWrapper.Loading -> {
                    if (profileViewModel.getSinceCount() == 1) {
                        binding.msvUser.showLoadingState()
                    } else {
                        binding.swipeRefresh.post {
                            binding.swipeRefresh.isRefreshing = true
                        }
                    }
                }
                is ResultWrapper.Success -> {
                    binding.msvUser.showDefaultState()

                    lastId = it.data.maxByOrNull { data -> data.userId }!!.userId
                    if (profileViewModel.getSinceCount() == 1){
                        userAdapter.setUserData(it.data)
                    } else {
                        binding.swipeRefresh.post {
                            binding.swipeRefresh.isRefreshing = false
                        }
                        userAdapter.loadMoreData(it.data)
                    }
                }
                is ResultWrapper.Failure -> {
                    binding.msvUser.showErrorState(
                        title = it.title,
                        errorMessage = it.message,
                        errorAction = {
                            profileViewModel.getUsers()
                        }
                    )
                }
            }
        })
    }

    override fun toastUserData(userData: GetUserProfileResponse) {
        Toast.makeText(this, userData.toString(), Toast.LENGTH_LONG).show()
    }
}