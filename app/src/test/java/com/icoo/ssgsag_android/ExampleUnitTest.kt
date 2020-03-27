package com.icoo.ssgsag_android

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.AutoCompleteTextView
import android.widget.EditText
import com.icoo.ssgsag_android.ui.main.review.club.write.ClubReviewWriteActivity
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {

        val nameList = listOf("jin", "won", "chan", "sion")
        val newNameList = nameList.map { name -> name.toUpperCase() }

        nameList.forEach{println("친구의 이름 -> $it")}
        newNameList.forEach{println("친구의 이름 -> $it")}

    }
}