package com.dut.trashdetect.ui.history

import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dut.trashdetect.base.BaseActivity
import com.dut.trashdetect.base.BaseInput
import com.dut.trashdetect.base.ViewModelProviderFactory
import com.dut.trashdetect.common.RESULT_DOC
import com.dut.trashdetect.databinding.ActivityHistoryBinding
import com.dut.trashdetect.model.UserResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class HistoryActivity : BaseActivity<ActivityHistoryBinding, HistoryViewModel>() {
    private lateinit var historyItemAdapter: HistoryItemAdapter
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseFireStore = FirebaseFirestore.getInstance()
    private val results = arrayListOf<UserResult>()

    override fun getLazyBinding() = lazy { ActivityHistoryBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = viewModels<HistoryViewModel> {
        ViewModelProviderFactory(BaseInput.NoInput)
    }

    override fun setupInit() {
        initViews()
        initListener()
        observe()
        initData()
    }

    private fun initData() {
        firebaseAuth.uid?.let { uid ->
            results.clear()
            val docRef = firebaseFireStore.collection(RESULT_DOC)
                .whereEqualTo("userId", uid)
            docRef.get()
                .addOnSuccessListener { snapshot ->
                    snapshot?.let { documents ->
                        documents.forEach { document ->
                            results.add(document.toObject(UserResult::class.java))
                        }
                        results.sortByDescending { it.createdAt }
                    }
                    binding.ivEmpty.isVisible = results.isEmpty()
                    historyItemAdapter.notifyDataSetChanged()
                }
        }
    }

    private fun initViews() {
        binding.apply {
            rvHistory.apply {
                historyItemAdapter = HistoryItemAdapter(results)
                adapter = historyItemAdapter
                layoutManager = LinearLayoutManager(
                    this@HistoryActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                val dividerItemDecoration = DividerItemDecoration(
                    this@HistoryActivity,
                    LinearLayoutManager.VERTICAL
                )
                addItemDecoration(dividerItemDecoration)
            }
        }
    }

    private fun initListener() {
        binding.apply {
            btnBack.setOnClickListener {
                finish()
            }
        }
    }

    private fun observe() {

    }
}
