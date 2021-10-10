package com.example.github_profile.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.github_profile.R
import com.example.github_profile.data.profile.model.GetUserProfileResponse
import com.example.github_profile.databinding.UserItemBinding

class UserAdapter(
    val data : MutableList<GetUserProfileResponse> = mutableListOf(),
    val listener: OnUserItemClicked
) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

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
        val binding = UserItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return UserViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userItem: GetUserProfileResponse = data[position]
        val userViewHolder = holder as UserViewHolder
        userViewHolder.bindUserItem(userItem)
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

    interface OnUserItemClicked {
        fun toastUserData(userData: GetUserProfileResponse)
    }
}