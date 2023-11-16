package com.example.astonrecyclerview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.DOWN
import androidx.recyclerview.widget.ItemTouchHelper.END
import androidx.recyclerview.widget.ItemTouchHelper.START
import androidx.recyclerview.widget.ItemTouchHelper.UP
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.astonrecyclerview.databinding.FragmentContactsBinding
import java.util.Collections

class ContactsFragment : Fragment() {

    companion object {
        const val ID = "Id"
        fun newInstance() = ContactsFragment()
    }

    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!
    private val userAdapter = UserRVAdapter(object : ClickListener {
        override fun onItemClick(user: User) {
            val bundle = Bundle().apply {
                putInt(ID, user.id)
            }
            findNavController().navigate(R.id.action_contactsFragment_to_addUserFragment, bundle)
        }

        override fun onCheckBoxClick(user: User) {
            repository.updateInfoUser(user)
        }

    })

    private val repository: UserRepository
        get() = (requireContext().applicationContext as App).repository

    private val simpleItemTouchCallback =
        object : ItemTouchHelper.SimpleCallback(UP or DOWN or START or END, 0) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val adapter = recyclerView.adapter as UserRVAdapter
                val from = viewHolder.adapterPosition
                val to = target.adapterPosition
                Collections.swap(repository.getDataList(), from, to)
                adapter.notifyItemMoved(from, to)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            rvUser.adapter = userAdapter
            rvUser.addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    LinearLayoutManager.VERTICAL
                )
            )
            val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
            itemTouchHelper.attachToRecyclerView(rvUser)
            userAdapter.setData(repository.getDataList())

            btAdd.setOnClickListener {
                findNavController().navigate(R.id.action_contactsFragment_to_addUserFragment)
            }
            btnDelete.setOnClickListener {
                repository.deleteUser()
                userAdapter.setData(repository.getDataList())
            }
            btnCansel.setOnClickListener {
                repository.clearFlagDelete()
                userAdapter.setData(repository.getDataList())
                userAdapter.notifyDataSetChanged()
                checkBoxTrash.isChecked = false
            }
            checkBoxTrash.setOnCheckedChangeListener { compoundButton, isCheked ->
                if (isCheked) {
                    userAdapter.showCheck()
                    userAdapter.notifyDataSetChanged()
                    btAdd.visibility = View.GONE
                    btnCansel.visibility = View.VISIBLE
                    btnDelete.visibility = View.VISIBLE
                } else {
                    userAdapter.showCheck()
                    userAdapter.notifyDataSetChanged()
                    btAdd.visibility = View.VISIBLE
                    btnCansel.visibility = View.GONE
                    btnDelete.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}