package hr.foi.air.fitfusion.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.foi.air.fitfusion.R
import hr.foi.air.fitfusion.adapters.CalendarAdapter
import hr.foi.air.fitfusion.adapters.CalendarAdapter2
import hr.foi.air.fitfusion.adapters.CalendarUtils
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter


class CalendarFragment : Fragment(), CalendarAdapter.OnItemListener {
    private lateinit var monthYearText: TextView
    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var selectedDate: LocalDate
    private lateinit var btnBackCl : Button
    private lateinit var btnForwardCl : Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        monthYearText = view.findViewById(R.id.monthYearTV)
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView)
        selectedDate = LocalDate.now()
        setMonthView()
        btnBackCl = view.findViewById(R.id.btnBackCl)
        btnForwardCl = view.findViewById(R.id.btnForwardCl)
        btnBackCl.setOnClickListener {
            previousMonthAction(btnBackCl)
        }
        btnForwardCl.setOnClickListener {
            nextMonthAction(btnForwardCl)
        }
    }

    fun previousMonthAction(view: View?) {
        selectedDate = selectedDate.minusMonths(1)
        setMonthView()
    }

    fun nextMonthAction(view: View?) {
        selectedDate = selectedDate.plusMonths(1)
        setMonthView()
    }
    private fun setMonthView() {
        monthYearText.text = monthYearFromDate(selectedDate)
        val daysInMonth = daysInMonthArray(selectedDate)
        val calendarAdapter = CalendarAdapter(daysInMonth, this)
        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(context, 7)
        calendarRecyclerView.layoutManager = layoutManager
        calendarRecyclerView.adapter = calendarAdapter
    }

    private fun daysInMonthArray(date: LocalDate): ArrayList<String> {
        val daysInMonthArray = ArrayList<String>()
        val yearMonth = YearMonth.from(date)
        val daysInMonth = yearMonth.lengthOfMonth()
        val firstOfMonth = selectedDate.withDayOfMonth(1)
        val dayOfWeek = firstOfMonth.dayOfWeek.value
        for (i in 1..42) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add("")
            } else {
                daysInMonthArray.add((i - dayOfWeek).toString())
            }
        }
        return daysInMonthArray
    }

    private fun monthYearFromDate(date: LocalDate): String? {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date.format(formatter)
    }

    override fun onItemClick(position: Int, dayText: String) {
        if (dayText.isNotEmpty()) {
            val message = "Selected Date $dayText ${monthYearFromDate(selectedDate)}"
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    /*override fun onItemClick(position: Int, date: LocalDate?) {
        CalendarUtils.selectedDate = date
        setMonthView()
    }*/

}
