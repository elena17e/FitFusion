package hr.foi.air.fitfusion.entities

data class Post(
    var id: String ?= null,
    var title: String ?= null,
    var content: String ?= null,
    var timestamp: Long = 0,
    var authorFirstName: String ?= null,
    var authorLastName: String ?= null
)