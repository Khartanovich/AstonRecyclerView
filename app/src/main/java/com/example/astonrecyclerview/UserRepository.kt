package com.example.astonrecyclerview

import android.util.Log
import io.github.serpro69.kfaker.Faker

class UserRepository {
    private var dataList = mutableListOf<User>()
    private val faker = Faker()

    init {
        createListUser()
    }

    fun getDataList(): MutableList<User> {
        return dataList
    }

    fun changeUserList(newListUser: List<User>?) {
        if (newListUser != null) {
            dataList = newListUser.toMutableList()
        }
    }

    fun addUser(newUser: User) {
        dataList.add(newUser)
    }

    fun updateInfoUser(newUser: User) {
        val indexPosition = dataList.indexOfFirst { newUser.id == it.id }
        dataList.removeAt(indexPosition)
        dataList.add(indexPosition, newUser)
    }

    fun deleteUser() {
        dataList = ArrayList(dataList)
        dataList.removeAll { it.isCheckedForDelete }
    }

    fun clearFlagDelete() {
        dataList = ArrayList(dataList)
        dataList.map {
            if (it.isCheckedForDelete) {
                it.isCheckedForDelete = false
            }
        }
    }

    fun getListIndexDelete(): List<Int> {
        val listIndexDelete = mutableListOf<Int>()
        dataList.forEachIndexed { index, user ->
            if (user.isCheckedForDelete) {
                listIndexDelete.add(index)
            }
        }
        return listIndexDelete
    }

    private fun createListUser() {
        faker.unique.configuration {
            enable(faker::name)
            enable(faker::phoneNumber)
        }
        val listName = List(100) { faker.name.name() }
        val phoneNumber = faker.phoneNumber.phoneNumber()
        for (id in 1..100) {
            dataList.add(User(id, listName[id - 1], phoneNumber, false))
        }
    }
}