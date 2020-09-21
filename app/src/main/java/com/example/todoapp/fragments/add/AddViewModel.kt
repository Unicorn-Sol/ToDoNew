package com.example.todoapp.fragments.add

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.todoapp.notification.NotificationSchedule
import java.util.*
import java.util.concurrent.TimeUnit

class AddViewModel : ViewModel() {


    var title = MutableLiveData<String?>()

    var description = MutableLiveData<String?>()


     fun showDatePickerAndTimePicker(context: Context) {
        val calender = Calendar.getInstance()
        val year = calender.get(Calendar.YEAR)
        val month = calender.get(Calendar.MONTH)
        val day = calender.get(Calendar.DAY_OF_MONTH)

        val hour = calender.get(Calendar.HOUR_OF_DAY)
        val minute = calender.get(Calendar.MINUTE)


        DatePickerDialog(
            context,
            { view, year, month, dayOfMonth ->

                calender.set(year, month, dayOfMonth)

                TimePickerDialog(
                    context,
                     { view, hourOfDay, minute ->
                        calender.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        calender.set(Calendar.MINUTE, minute)

                        Toast.makeText(context, "Notification set at ${calender.get(
                            Calendar.HOUR_OF_DAY)} : ${calender.get(Calendar.MINUTE)}", Toast.LENGTH_SHORT).show()

                        Log.e(
                            "TAG",
                            "showDatePickerAndTimePicker:${calender.get(Calendar.HOUR_OF_DAY)},${
                                calender.get(Calendar.MINUTE)
                            } "
                        )
                        val customTime = calender.timeInMillis
                        val currentTime = System.currentTimeMillis()
                        val timeDelay = customTime - currentTime
                        Log.e("TAG", "showDatePickerAndTimePicker: $timeDelay")
                        if (timeDelay > 0) {
                            scheduleNotification(timeDelay, "this is tag", "\"${title.value}\" task to be done now.")
                        }
                    },
                    hour,
                    minute,
                    false
                ).show()

            },
            year,
            month,
            day
        ).show()




    }

    private fun scheduleNotification(timeDelay: Long, tag: String, body: String) {

        val data = Data.Builder().putString("body", body)

        val work = OneTimeWorkRequestBuilder<NotificationSchedule>()
            .setInitialDelay(timeDelay, TimeUnit.MILLISECONDS)
            .setConstraints(Constraints.Builder().setTriggerContentMaxDelay(1000, TimeUnit.MILLISECONDS).build()) // API Level 24
            .setInputData(data.build())
            .addTag(tag)
            .build()

        WorkManager.getInstance().enqueue(work)

    }

    fun doLogic(s: Editable) {

        title.value = s.toString()


    }
    fun doLogic2(s: Editable) {

        description.value = s.toString()


    }

}