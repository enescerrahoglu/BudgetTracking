package com.enescerrahoglu.budgettracking.user
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

class UserViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")
    val userModel = MutableLiveData<UserModel>()

    fun setUser(user: UserModel) {
        userModel.value = user
    }

    fun getUser() : UserModel? {
        return userModel.value
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

    fun createUser() {
        println("userModel.value?.phoneNumber")
        userModel.value?.let {
            userModel.value = UserModel(it.phoneNumber, null, null,0.0, Timestamp.now())
            usersCollection.document(it.phoneNumber).set(it)
                .addOnSuccessListener {
                    println("Success")
                }
                .addOnFailureListener { e ->
                    println("Fail: ${e.localizedMessage}")
                }
        }
    }
}