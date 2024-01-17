package hr.foi.air.fitfusion.entities

data class Training(
    var type: String?=null,
    var date: String?=null,
    var time: String?=null,
    var participantsId: Map<String, String>?=null
)