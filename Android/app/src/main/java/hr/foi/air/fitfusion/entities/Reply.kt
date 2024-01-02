package hr.foi.air.fitfusion.entities

data class Reply(
    var id: String ?= null,
    var postId: String ?= null,
    var content: String ?= null,
    var timestamp: Long = 0,
    var authorFirstName: String ?= null,
    var authorLastName: String ?= null
)
