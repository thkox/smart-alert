package eu.tkacas.smartalert.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
class AccountViewModel : ViewModel() {

    private val _email = MutableStateFlow<String>("")
    val email: StateFlow<String> get() = _email

    private val _firstName = MutableStateFlow<String>("")
    val firstName: StateFlow<String> get() = _firstName

    private val _lastName = MutableStateFlow<String>("")
    val lastName: StateFlow<String> get() = _lastName

    private val _password = MutableStateFlow<String>("")
    val password: StateFlow<String> get() = _password

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    init {
        fetchEmail()
        fetchUserDetails()
        fetchPassword()
    }

    private fun fetchEmail() {
        viewModelScope.launch {
            val user = Firebase.auth.currentUser
            _email.value = user?.email ?: ""
        }
    }

    private fun fetchUserDetails() {
        viewModelScope.launch {
            val userId = Firebase.auth.currentUser?.uid
            if (userId != null) {
                _isLoading.value = true
                val databaseRef = Firebase.database.reference.child("users").child(userId)
                databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val firstName = snapshot.child("firstName").getValue(String::class.java)
                        Log.d("AccountViewModel", "First Name Retrieved: $firstName")
                        _firstName.value = firstName ?: ""
                        val lastName = snapshot.child("lastName").getValue(String::class.java)
                        Log.d("AccountViewModel", "Last Name Retrieved: $lastName")
                        _lastName.value = lastName ?: ""
                        _isLoading.value = false
                    }
                    override fun onCancelled(error: DatabaseError) {
                         // Handle onCancelled
                        _isLoading.value = false
                    }
                })
            }
        }
    }

    private fun fetchPassword() {
        viewModelScope.launch {
            val user = Firebase.auth.currentUser
            _password.value = user?.providerData?.get(1)?.providerId ?: ""
        }
    }

    fun changePassword(newPassword: String) {
        val user = Firebase.auth.currentUser
        user?.updatePassword(newPassword)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("AccountViewModel", "User password updated.")
            } else {
                // An error occurred, handle it
                Log.d("AccountViewModel", "Failed to update user password.")
            }
        }
    }

}