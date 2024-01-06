package hr.foi.air.fitfusion.adapters

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter

object CalendarUtils {
    //lateinit var selectedDate: LocalDate
    var selectedDate: LocalDate? = null
    fun formattedDate(date: LocalDate?): String {
        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
        return date!!.format(formatter)
    }

    fun formattedTime(time: LocalTime): String {
        val formatter = DateTimeFormatter.ofPattern("hh:mm:ss a")
        return time.format(formatter)
    }

    fun monthYearFromDate(date: LocalDate?): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date?.format(formatter) ?: "Unknown Date"
    }

    /*fun daysInMonthArray(date: LocalDate): ArrayList<LocalDate?> {
        val daysInMonthArray = ArrayList<LocalDate?>()
        val yearMonth = YearMonth.from(date)
        val daysInMonth = yearMonth.lengthOfMonth()
        val firstOfMonth = selectedDate!!.withDayOfMonth(1)
        val dayOfWeek = firstOfMonth.dayOfWeek.value
        for (i in 1..42) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) daysInMonthArray.add(null) else daysInMonthArray.add(
                LocalDate.of(
                    selectedDate!!.year, selectedDate!!.month, i - dayOfWeek
                )
            )
        }
        return daysInMonthArray
    }*/

    fun daysInMonthArray(date: LocalDate?): ArrayList<LocalDate> {
        val daysInMonthArray = ArrayList<LocalDate>()
        val yearMonth = YearMonth.from(date)

        val daysInMonth = yearMonth.lengthOfMonth()

        val firstOfMonth = selectedDate!!.withDayOfMonth(1)
        val dayOfWeek = firstOfMonth.dayOfWeek.value ?: 0

        for (i in 1..42) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add(LocalDate.MIN)
            } else {
                daysInMonthArray.add(LocalDate.of(selectedDate!!.year, selectedDate!!.month, i - dayOfWeek))
            }
        }
        return daysInMonthArray
    }

    fun daysInWeekArray(date: LocalDate?): ArrayList<LocalDate> {
        val days = ArrayList<LocalDate>()
        var current = mondayForDate(date!!)
        val endDate = current!!.plusWeeks(1)
        while (current != null && current.isBefore(endDate)) {
            days.add(current)
            current = current.plusDays(1)
        }
        return days
    }

    private fun mondayForDate(current: LocalDate): LocalDate? {
        var current = current
        val oneWeekAgo = current.minusWeeks(1)
        while (current.isAfter(oneWeekAgo)) {
            if (current.dayOfWeek == DayOfWeek.MONDAY) return current
            current = current.minusDays(1)
        }
        return null
    }
}