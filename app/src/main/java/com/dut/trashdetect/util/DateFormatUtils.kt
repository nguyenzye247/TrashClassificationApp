package com.dut.trashdetect.util

import android.annotation.SuppressLint
import java.text.DateFormat
import java.text.SimpleDateFormat

object DateFormatUtils {
    private const val APP_DATE_FORMAT_PATTERN = "dd MMM yyyy, HH:mm a"

    fun formatDate(timeInMillis: Long): String {
        return SimpleDateFormat.getDateInstance(DateFormat.SHORT).format(timeInMillis)
    }

    @SuppressLint("SimpleDateFormat")
    fun formatDateTime(timeInMillis: Long): String =
        SimpleDateFormat(APP_DATE_FORMAT_PATTERN).format(timeInMillis)
}
