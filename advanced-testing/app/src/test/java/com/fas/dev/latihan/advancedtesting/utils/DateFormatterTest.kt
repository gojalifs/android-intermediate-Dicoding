package com.fas.dev.latihan.advancedtesting.utils

import org.junit.Assert
import org.junit.Test
import java.time.format.DateTimeParseException

class DateFormatterTest {
    @Test
    fun `given correct ISO  8601 then format locally`() {
        val currentDate = "2022-02-02T10:10:10Z"
        Assert.assertEquals(
            "02 Feb 2022 | 17:10",
            DateFormatter.formatDate(currentDate, "Asia/Jakarta")
        )
        Assert.assertEquals(
            "02 Feb 2022 | 18:10",
            DateFormatter.formatDate(currentDate, "Asia/Makassar")
        )
        Assert.assertEquals(
            "02 Feb 2022 | 19:10",
            DateFormatter.formatDate(currentDate, "Asia/Jayapura")
        )
    }

    @Test
    fun `given wrong ISO 8601 format should throw error`(){
        val wrongFormat = "2022-02-02T10:10"
        Assert.assertThrows(DateTimeParseException::class.java){
            DateFormatter.formatDate(wrongFormat, "Asia/Bandung")
        }
    }
}