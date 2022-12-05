package com.dut.trashdetect.ui.history

import androidx.lifecycle.MutableLiveData
import com.dut.trashdetect.base.BaseInput
import com.dut.trashdetect.base.BaseViewModel
import com.dut.trashdetect.model.UserResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HistoryViewModel(val input: BaseInput.NoInput): BaseViewModel(input) {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseFireStore = FirebaseFirestore.getInstance()
    private val _results = MutableLiveData<ArrayList<UserResult>>()

    init {

    }

}
