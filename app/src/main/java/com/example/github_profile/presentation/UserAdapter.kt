package com.example.github_profile.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.github_profile.R
import com.example.github_profile.data.profile.model.GetUserProfileResponse
import com.example.github_profile.databinding.ItemLoadingBinding
import com.example.github_profile.databinding.UserItemBinding
import java.lang.IllegalArgumentException

class UserAdapter(
    val data : MutableList<Any> = mutableListOf(),
    val listener: OnUserItemClicked,
    var isLoading: Boolean = false
) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    companion object {
        private const val TYPE_LOADING = 0
        private const val TYPE_ITEM = 1
    }

    fun setLoadingState(state: Boolean){
        isLoading = state
        if (!isLoading) {
            data.removeLast()
        } else {
            if (data.last() != "loading"){
                data.add("loading")
            }
        }
        notifyDataSetChanged()
    }

    fun setUserData(userData: List<GetUserProfileResponse>) {
        if (data.size > 0) {
            data.clear()
        }
        data.addAll(userData)
        notifyDataSetChanged()
    }

    fun clearData(){
        data.clear()
        notifyDataSetChanged()
    }

    fun loadMoreData(moreData: List<GetUserProfileResponse>){
        data.addAll(moreData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
//        val binding = UserItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
//        return UserViewHolder(binding)
        return when (viewType) {
            TYPE_ITEM -> {
                val view = UserItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
                UserViewHolder(view)
            }
            TYPE_LOADING -> {
                val view = ItemLoadingBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
                LoadingViewHolder(view)
            }
            else -> {
                throw IllegalArgumentException("invalid view type")
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val compare = data[position]
        return when(compare) {
            is GetUserProfileResponse  -> TYPE_ITEM
            else -> TYPE_LOADING
        }
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is UserViewHolder -> {
                val userItem: GetUserProfileResponse = data[position] as GetUserProfileResponse
                holder.bindUserItem(userItem)
            }
            is LoadingViewHolder -> {

            }
        }
    }

    open inner class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class UserViewHolder(private val binding: UserItemBinding) : ViewHolder(binding.root) {
        fun bindUserItem(item: GetUserProfileResponse) {
            with(itemView) {
                val email = if (item.email.isNullOrEmpty()) "Not Available" else item.email
                val location = if (item.location.isNullOrEmpty()) "Not Available" else item.location
                val name = if (item.name.isNullOrEmpty()) "Not Available" else item.name

                binding.tvUsername.text = String.format("Username: ${item.username}")
                binding.tvName.text = String.format("Name: $name")
                binding.tvId.text = String.format("Id: ${item.userId}")
                binding.tvCreatedAt.text = String.format("Created At: ${item.createdAt}")
                binding.tvEmail.text = String.format("Email: $email")
                binding.tvLocation.text = String.format("Location: $location")

                Glide.with(this)
                    .load(item.avatarUrl)
                    .placeholder(R.drawable.ic_baseline_account_circle_24)
                    .into(binding.ivUser)

                binding.mcvUserItem.setOnClickListener {
                    listener.toastUserData(item)
                }
            }
        }
    }

    inner class LoadingViewHolder(private val binding: ItemLoadingBinding): ViewHolder(binding.root) {
        fun bindItemLoading(){

        }
    }

    interface OnUserItemClicked {
        fun toastUserData(userData: GetUserProfileResponse)
    }
}