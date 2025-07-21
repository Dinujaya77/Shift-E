package com.example.shift_e.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shift_e.data.repository.UserRepository
import com.example.shift_e.model.UserData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val _userData = MutableStateFlow(UserData())
    val userData: StateFlow<UserData> = _userData

    fun loadUserData() {
        viewModelScope.launch {
            val result = UserRepository.fetchUserData()
            result?.let { _userData.value = it }
        }
    }
}
