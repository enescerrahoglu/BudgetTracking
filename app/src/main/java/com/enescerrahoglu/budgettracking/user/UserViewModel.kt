package com.enescerrahoglu.budgettracking.user
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson

class UserViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")
    val userModel = MutableLiveData<UserModel>()
    val isLoading = MutableLiveData<Boolean>()

    fun setLoading(loadingStatus : Boolean){
        isLoading.value = loadingStatus
    }

    fun setUser(user: UserModel) {
        userModel.value = user
    }

    fun hasUser(phoneNumber: String, callback: (Boolean) -> Unit) {
        val query = usersCollection.whereEqualTo("phoneNumber", phoneNumber)
        query.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val documents = task.result?.documents
                if (!documents.isNullOrEmpty()) {
                    println("Current user!")
                    callback.invoke(true)
                }else{
                    callback.invoke(false)
                }
            }
        }
    }

    fun createUser(callback: (Boolean) -> Unit) {
        userModel.value?.let {
            usersCollection.document(it.phoneNumber!!).set(it)
                .addOnSuccessListener {
                    println("Success")
                    callback.invoke(true)
                }
                .addOnFailureListener { e ->
                    println("Fail: ${e.localizedMessage}")
                    callback.invoke(false)
                }
        }
    }

    fun updateUser(callback: (Boolean) -> Unit) {
        userModel.value?.let {
            usersCollection.document(it.phoneNumber).set(userModel.value!!)
                .addOnSuccessListener {
                    println("Update success")
                    callback.invoke(true)
                }
                .addOnFailureListener { e ->
                    println("Update failed: ${e.localizedMessage}")
                    callback.invoke(false)
                }
        }

    }

    fun getUser(phoneNumber: String, callback: (UserModel?) -> Unit) {
        val query = usersCollection.whereEqualTo("phoneNumber", phoneNumber)
        query.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val documents = task.result?.documents
                if (!documents.isNullOrEmpty()) {
                    val document = documents[0]
                    val gson = Gson()
                    val user = gson.fromJson(document.data.toString(), UserModel::class.java)
                    callback.invoke(user)
                } else {
                    callback.invoke(null)
                }
            } else {
                callback.invoke(null)
            }
        }
    }
}