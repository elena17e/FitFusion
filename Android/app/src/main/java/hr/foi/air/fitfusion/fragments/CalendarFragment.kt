package hr.foi.air.fitfusion.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.foi.air.fitfusion.R
import hr.foi.air.fitfusion.WeekViewActivity
import hr.foi.air.fitfusion.adapters.CalendarAdapter
import hr.foi.air.fitfusion.adapters.CalendarUtils
import java.time.LocalDate


class CalendarFragment : Fragment(), CalendarAdapter.OnItemListener {

    private var monthYearText: TextView? = null
    private var calendarRecyclerView: RecyclerView? = null
    private lateinit var btnBackCl : Button
    private lateinit var btnForwardCl : Button
    private lateinit var btnWeeklyCl : Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Log.d("CalendarFragment", "Initializing selectedDate")
        //CalendarUtils.selectedDate = LocalDate.now()
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        monthYearText = view.findViewById(R.id.monthYearTV)
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView)
        if (CalendarUtils.selectedDate == null) {CalendarUtils.selectedDate = LocalDate.now()}
        //CalendarUtils.selectedDate = LocalDate.now()
        setMonthView()
        btnBackCl = view.findViewById(R.id.btnBackCl)
        btnForwardCl = view.findViewById(R.id.btnForwardCl)
        btnWeeklyCl = view.findViewById(R.id.btnWeekly)
        btnBackCl.setOnClickListener {
            previousMonthAction(btnBackCl)
        }
        btnForwardCl.setOnClickListener {
            nextMonthAction(btnForwardCl)
        }
        btnWeeklyCl.setOnClickListener {
            weeklyAction(btnWeeklyCl)
        }
    }

    fun previousMonthAction(view: View?) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate!!.minusMonths(1)
        setMonthView()
    }

    fun nextMonthAction(view: View?) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate!!.plusMonths(1)
        setMonthView()
    }
    private fun setMonthView() {
        monthYearText!!.text = CalendarUtils.monthYearFromDate(CalendarUtils.selectedDate)
        val daysInMonth : ArrayList<LocalDate?> = CalendarUtils.daysInMonthArray(CalendarUtils.selectedDate)
        val calendarAdapter = CalendarAdapter(daysInMonth, this)
        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(context, 7)
        calendarRecyclerView!!.layoutManager = layoutManager
        calendarRecyclerView!!.adapter = calendarAdapter
    }

    override fun onResume() {
        super.onResume()
        if (CalendarUtils.selectedDate != null) {
            setMonthView() // This will refresh the month view with the updated date
        }
    }

    /*private fun daysInMonthArray(date: LocalDate): ArrayList<String> {
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
    }*/

    /*private fun monthYearFromDate(date: LocalDate): String? {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date.format(formatter)
    }

    override fun onItemClick(position: Int, dayText: String) {
        if (dayText.isNotEmpty()) {
            val message = "Selected Date $dayText ${monthYearFromDate(selectedDate)}"
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }*/

    override fun onItemClick(position: Int, date: LocalDate?) {
        if (date != null) {
            CalendarUtils.selectedDate = date
            setMonthView()
        }
    }

    fun weeklyAction(view: View) {
        startActivity(Intent(requireContext(), WeekViewActivity::class.java))
    }
}
