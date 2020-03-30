package com.icoo.ssgsag_android

import org.junit.Test

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