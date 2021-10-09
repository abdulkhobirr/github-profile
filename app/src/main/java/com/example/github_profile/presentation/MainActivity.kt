package com.example.github_profile.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.github_profile.R
import com.example.github_profile.databinding.ActivityMainBinding
import com.example.github_profile.utils.showDefaultState
import com.example.github_profile.utils.showErrorState
import com.example.github_profile.utils.showLoadingState
import com.example.github_profile.utils.viewmodel.ResultWrapper
import com.example.github_profile.viewmodel.ProfileViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val profileViewModel: ProfileViewModel by viewModel()

    var timeStart = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initActions()
        initObservable()
        profileViewModel.getUsers()
    }

    private fun initActions(){
        binding.swipeRefresh.setOnRefreshListener {
            profileViewModel.getUsers()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun initObservable(){
        profileViewModel.listUser.observe(this, {
            when (it) {
                is ResultWrapper.Loading -> {
                    timeStart = System.currentTimeMillis()
                    Log.d("GetUserTimeStart", System.currentTimeMillis().toString())
                    binding.msvUser.showLoadingState()
                }
                is ResultWrapper.Success -> {
                    binding.msvUser.showDefaultState()
                    val timeEnd = System.currentTimeMillis()-timeStart
                    Log.d("GetUserTimeEnd", timeEnd.toString())
                    Log.d("GetUserGithub", it.data.toString())
                }
                is ResultWrapper.Failure -> {
                    val timeEnd = System.currentTimeMillis()-timeStart
                    Log.d("GetUserTimeEnd", timeEnd.toString())
                    binding.msvUser.showErrorState(
                        errorMessage = it.message,
                        errorAction = {
                            profileViewModel.getUsers()
                        }
                    )
                }
            }
        })
    }
}