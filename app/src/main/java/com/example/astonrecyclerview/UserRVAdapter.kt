package com.example.astonrecyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.astonrecyclerview.databinding.ItemRecyclerViewBinding
import com.example.astonrecyclerview.databinding.ItemRvWithoutCheckBinding

class UserRVAdapter(
    private val clickListener: ClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var showCheck = false
    private var data: List<User> = emptyList()

    fun setData(listUser: List<User>) {
        val diffCallback = UsersDiffUtilCallback(data, listUser)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.data = listUser
        diffResult.dispatchUpdatesTo(this)
    }

    fun showCheck() {
        showCheck = !showCheck
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            SHOW_CHECK -> {
                val binding = ItemRecyclerViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                UserViewHolder(binding)
            }

            NO_SHOW_CHECK -> {
                val binding = ItemRvWithoutCheckBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                UserViewHolderWithoutCheck(binding)
            }

            else -> {
                val binding = ItemRecyclerViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                UserViewHolder(binding)
            }
        }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = data[position]
        when (getItemViewType(position)) {
            SHOW_CHECK -> {
                val holder1 = holder as UserViewHolder
                with(holder1.binding) {
                    tvId.text = item.id.toString()
                    tvName.text = item.name
                    tvPhoneNumber.text = item.phoneNumber
                    checkBox.isChecked = item.isCheckedForDelete
                    checkBox.setOnClickListener {
                        item.isCheckedForDelete = !item.isCheckedForDelete
                        clickListener.onCheckBoxClick(item)
                    }
                    root.setOnClickListener {
                        clickListener.onItemClick(item)
                    }
                }
            }

            NO_SHOW_CHECK -> {
                val holder2 = holder as UserViewHolderWithoutCheck
                with(holder2.binding) {
                    tvId.text = item.id.toString()
                    tvName.text = item.name
                    tvPhoneNumber.text = item.phoneNumber
                    root.setOnClickListener {
                        clickListener.onItemClick(item)
                    }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (showCheck) {
            return SHOW_CHECK
        } else {
            return NO_SHOW_CHECK
        }
    }

    class UserViewHolder(val binding: ItemRecyclerViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    class UserViewHolderWithoutCheck(val binding: ItemRvWithoutCheckBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        const val SHOW_CHECK = 0
        const val NO_SHOW_CHECK = 1
    }
}

interface ClickListener {
    fun onItemClick(user: User)
    fun onCheckBoxClick(user: User)
}
