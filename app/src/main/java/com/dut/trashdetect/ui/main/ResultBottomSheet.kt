package com.dut.trashdetect.ui.main

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dut.trashdetect.databinding.BottomSheetResultBinding
import com.dut.trashdetect.model.Result
import com.dut.trashdetect.util.ScreenUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ResultBottomSheet(
    private val probabilities: HashMap<String, Float>
) : BottomSheetDialogFragment() {
    private val binding by lazy { BottomSheetResultBinding.inflate(layoutInflater) }
    private lateinit var resultAdapter: ResultAdapter

    companion object {
        const val TAG = "ResultBottomSheet_Tag"
        const val HEIGHT_FACTOR = 0.7
        fun newInstance(probabilities: HashMap<String, Float>) =
            ResultBottomSheet(probabilities)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)
        bottomSheetBehavior.isDraggable = false
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        activity?.let {
            val maxHeight = HEIGHT_FACTOR * ScreenUtils.getScreenHeight(it)
            binding.csBottomSheet.minHeight = maxHeight.toInt()
        }
        initViews()
        initListeners()
        observe()
    }

    private fun initViews() {
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
            )
        }
        binding.apply {
            val results = arrayListOf<Result>()
            probabilities.mapValues { entry ->
                results.add(Result(entry.key, entry.value))
            }
            results.sortByDescending { it.percentage }
            resultAdapter = ResultAdapter(results)
            rvResults.adapter = resultAdapter
            rvResults.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
        }
    }

    private fun initListeners() {
        binding.btnClose.setOnClickListener {
            dismiss()
        }
    }

    private fun observe() {

    }
}
