package hr.foi.air.fitfusion.helpers

import hr.foi.air.fitfusion.entities.Task
import hr.foi.air.fitfusion.entities.TaskCategory
import java.util.Date


class MockDataLoader {
    fun getDemoData() :List<Task> = listOf(
        Task("Submit seminar paper", Date(), TaskCategory("RAMPU", "#000080"), false),
        Task("Prepare for exercises", Date(), TaskCategory("RPP", "#FF0000"), false),
        Task("Rally a project team", Date(), TaskCategory("RAMPU", "#000080"), false),
        Task("Make up a project idea", Date(), TaskCategory("RWA", "#CCCCCC"), false)
    )
}
