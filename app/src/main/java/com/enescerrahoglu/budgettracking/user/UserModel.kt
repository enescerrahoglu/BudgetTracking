package com.enescerrahoglu.budgettracking.user

import com.google.firebase.Timestamp

data class UserModel(val phoneNumber: String, val firstName: String?, val lastName: String?, val monthlyBudget: Double, val joinDate: Timestamp?) {
}