package com.arya.submission2.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.arya.submission2.R
import com.arya.submission2.databinding.ItemUserBinding
import com.arya.submission2.data.remote.response.Item
import com.bumptech.glide.Glide

class UserAdapter(private val users: ArrayList<Item>, private val callback: (Item) -> Unit) : Adapter<UserAdapter.UserViewHolder>() {
    inner class UserViewHolder(itemView: View) : ViewHolder(itemView) {
        private var binding: ItemUserBinding
        init {
            binding = ItemUserBinding.bind(itemView)
        }
        fun setData(user: Item) {
            Glide.with(itemView).load(user.avatarUrl).into(binding.image)
            binding.name.text = user.login
            binding.type.text = user.type
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder = UserViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false))

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.setData(users[position])
        holder.itemView.setOnClickListener { callback(users[position]) }
    }

    override fun getItemCount(): Int = users.size
}