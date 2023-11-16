package com.example.astonrecyclerview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.astonrecyclerview.databinding.FragmentAddUserBinding


class AddUserFragment : Fragment() {
    companion object {
        const val ID = "Id"
    }

    lateinit var listUsers: MutableList<User>
    private var _binding: FragmentAddUserBinding? = null
    private val binding get() = _binding!!
    private val repository: UserRepository
        get() = (requireContext().applicationContext as App).repository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listUsers = repository.getDataList()
        val idOldUser = arguments?.let {
            it.getInt(ID)
        }
        if (idOldUser != null) {
            changeUserInfo(idOldUser)
        } else {
            createNewUser()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun changeUserInfo(userId: Int) {
        with(binding) {
            val indextOldUser = listUsers.indexOfFirst { it.id == userId }
            val userOld: User = listUsers.elementAt(indextOldUser)
            label.text = "Внесите изменения"
            idText.text = userId.toString()
            writeName.setText(userOld.name)
            writePhone.setText(userOld.phoneNumber)
            save.setOnClickListener {
                val name = writeName.text.toString()
                val phoneNumber = writePhone.text.toString()
                if (name.isNotBlank() && phoneNumber.isNotBlank()) {
                    repository.updateInfoUser(User(userId, name, phoneNumber, false))
                    findNavController().popBackStack()
                } else {
                    Toast.makeText(requireContext(), "Внесите данные", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            cansel.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun createNewUser() {
        with(binding) {
            var id = listUsers.size + 1
            listUsers.forEach {
                while (id == it.id) {
                    id++
                }
            }
            idText.text = id.toString()
            save.setOnClickListener {
                val name = writeName.text.toString()
                val phoneNumber = writePhone.text.toString()
                if (name.isNotBlank() && phoneNumber.isNotBlank()) {
                    repository.addUser(User(id, name, phoneNumber, false))
                    findNavController().popBackStack()
                } else {
                    Toast.makeText(requireContext(), "Внесите данные", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            cansel.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}