package com.zhd.dompet.dialog

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.zhd.dompet.databinding.DialogFilterCalendarBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min

class FilterCalendarDialog : DialogFragment() {
    private var _binding: DialogFilterCalendarBinding? = null
    private val binding
        get() = _binding!!

    private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    var dateStart: Date? = null
    var dateEnd: Date? = null
    var onShowAllListener: () -> Unit = {}
    var onSaveListener: (dateStart: Date?, dateEnd: Date?) -> Unit = { _, _ -> }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DialogFilterCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val width = (resources.displayMetrics.widthPixels * 0.9).toInt()
        dialog?.window?.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.inputStartDate.setText(dateStart?.let { dateFormat.format(it) })
        binding.inputEndDate.setText(dateEnd?.let { dateFormat.format(it) })

        binding.buttonShowAll.setOnClickListener {
            onShowAllListener()
            dismiss()
        }

        binding.buttonSave.setOnClickListener {
            onSaveListener(dateStart, dateEnd)
            dismiss()
        }

        binding.inputStartDate.setOnClickListener {
            showCalendar(null, dateStart) {
                val calendar = Calendar.getInstance()
                calendar.time = it
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                dateStart = calendar.time

                binding.inputStartDate.setText(dateFormat.format(dateStart!!))
            }
        }

        binding.inputEndDate.setOnClickListener {
            showCalendar(dateStart, dateEnd) {
                val calendar = Calendar.getInstance()
                calendar.time = it
                calendar.set(Calendar.HOUR_OF_DAY, 23)
                calendar.set(Calendar.MINUTE, 59)
                calendar.set(Calendar.SECOND, 59)
                calendar.set(Calendar.MILLISECOND, 999)
                dateEnd = calendar.time

                binding.inputEndDate.setText(dateFormat.format(dateEnd!!))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showCalendar(minDate: Date?, _date: Date?, onResult: (Date) -> Unit) {
        val date = _date ?: Date()
        val calendar = Calendar.getInstance()
        calendar.time = date
        val dialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                onResult(calendar.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        minDate?.let { dialog.datePicker.minDate = it.time }
        dialog.show()
    }


}