package com.icoo.ssgsag_android.ui.main.myPage.career.datePicker

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.View
import android.widget.NumberPicker
import com.icoo.ssgsag_android.R

import java.util.Calendar

class CustomDatePickerDialog : DialogFragment() {

    private var listener: DatePickerDialog.OnDateSetListener? = null
    var cal = Calendar.getInstance()

    private var mYear: Int = 0
    private var mMonth: Int = 0

    fun setListener(listener: DatePickerDialog.OnDateSetListener) {
        this.listener = listener
    }

    fun setValue(year: Int, month: Int) {
        mYear = year
        mMonth = month
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity!!.layoutInflater

        val dialog = inflater.inflate(R.layout.custom_date_picker, null)

        val monthPicker = dialog.findViewById<View>(R.id.picker_month) as NumberPicker
        val yearPicker = dialog.findViewById<View>(R.id.picker_year) as NumberPicker

        monthPicker.minValue = 1
        monthPicker.maxValue = 12
        monthPicker.value = mMonth


        yearPicker.minValue =
            MIN_YEAR
        yearPicker.maxValue =
            MAX_YEAR
        yearPicker.value = mYear

        builder.setView(dialog)
            .setPositiveButton("확인") { dialog, id ->
                listener!!.onDateSet(
                    null,
                    yearPicker.value,
                    monthPicker.value,
                    0
                )
            }
            .setNegativeButton("취소") { dialog, id -> this@CustomDatePickerDialog.dialog?.cancel() }

        return builder.create()
    }

    companion object {

        private val MAX_YEAR = 2099
        private val MIN_YEAR = 1980
    }
}