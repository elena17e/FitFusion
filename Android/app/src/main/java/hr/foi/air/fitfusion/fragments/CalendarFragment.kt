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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        monthYearText = view.findViewById(R.id.monthYearTV)
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView)
        if (CalendarUtils.selectedDate == null) {CalendarUtils.selectedDate = LocalDate.now()}

        setMonthView()
        btnBackCl = view.findViewById(R.id.btnBackCl)
        btnForwardCl = view.findViewById(R.id.btnForwardCl)
        btnBackCl.setOnClickListener {
            previousMonthAction()
        }
        btnForwardCl.setOnClickListener {
            nextMonthAction()
        }

    }

    private fun previousMonthAction() {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate!!.minusMonths(1)
        setMonthView()
    }

    private fun nextMonthAction() {
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
            setMonthView()
        }
    }


    override fun onItemClick(position: Int, date: LocalDate?) {
        if (date != null) {
            CalendarUtils.selectedDate = date
            setMonthView()
            startActivity(Intent(requireContext(), WeekViewActivity::class.java))
        }
    }

}
