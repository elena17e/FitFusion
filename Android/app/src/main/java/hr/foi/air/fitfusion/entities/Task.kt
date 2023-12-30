package hr.foi.air.fitfusion.entities

data class Post(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val timestamp: Long = 0,
    val authorFirstName: String = "",
    val authorLastName: String = ""
)